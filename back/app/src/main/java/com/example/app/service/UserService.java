package com.example.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.example.app.model.User;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class UserService {
    private final Firestore firestore;
    private static final String COLLECTION_NAME = "users";

    public UserService(FirebaseApp firebaseApp) {
        this.firestore = FirestoreClient.getFirestore(firebaseApp);
    }

    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
        List<User> users = new ArrayList<>();
        var documents = firestore.collection(COLLECTION_NAME).get().get().getDocuments();
        
        for (QueryDocumentSnapshot document : documents) {
            User user = document.toObject(User.class);
            user.setId(document.getId());
            users.add(user);
        }
        
        return users;
    }

    public User createUser(User user) throws ExecutionException, InterruptedException {
        // Remove ID if present to let Firestore generate it
        user.setId(null);
        
        var docRef = firestore.collection(COLLECTION_NAME).document();
        user.setId(docRef.getId());
        
        docRef.set(user).get();
        return user;
    }

    public User updateUser(User user) throws ExecutionException, InterruptedException {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null for update");
        }
        
        firestore.collection(COLLECTION_NAME)
                .document(user.getId())
                .set(user)
                .get();
        
        return user;
    }

    public void deleteUser(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME)
                .document(id)
                .delete()
                .get();
    }

    public User getUser(String id) throws ExecutionException, InterruptedException {
        var document = firestore.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get();
        
        if (!document.exists()) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        
        User user = document.toObject(User.class);
        user.setId(document.getId());
        return user;
    }
} 