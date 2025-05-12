package com.example.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.app.factory.AdminUserFactory;
import com.example.app.factory.GuestUserFactory;
import com.example.app.factory.UserFactory;
import com.example.app.model.User;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class UserController {

    private final Firestore firestore;
    private static final String COLLECTION_NAME = "users";

    public UserController(FirebaseApp firebaseApp) {
        this.firestore = FirestoreClient.getFirestore(firebaseApp);
    }

    @GetMapping
    public List<User> getAllUsers() {
        try {
            List<User> users = new ArrayList<>();
            var documents = firestore.collection(COLLECTION_NAME).get().get().getDocuments();
            
            for (QueryDocumentSnapshot document : documents) {
                User user = document.toObject(User.class);
                user.setId(document.getId());
                users.add(user);
            }
            
            return users;
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al obtener usuarios: " + e.getMessage());
        }
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        try {
            UserFactory factory;
            if ("admin".equals(user.getRole())) {
                factory = new AdminUserFactory();
            } else {
                factory = new GuestUserFactory();
            }
            
            User createdUser = factory.createUser(user.getEmail(), user.getPassword(), user.getFormData());
            var docRef = firestore.collection(COLLECTION_NAME).document();
            createdUser.setId(docRef.getId());
            
            docRef.set(createdUser).get();
            return createdUser;
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al crear usuario: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user) {
        try {
            UserFactory factory;
            if ("admin".equals(user.getRole())) {
                factory = new AdminUserFactory();
            } else {
                factory = new GuestUserFactory();
            }
            
            User updatedUser = factory.createUser(user.getEmail(), user.getPassword(), user.getFormData());
            updatedUser.setId(id);
            
            firestore.collection(COLLECTION_NAME)
                    .document(id)
                    .set(updatedUser)
                    .get();
            
            return updatedUser;
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al actualizar usuario: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            firestore.collection(COLLECTION_NAME)
                    .document(id)
                    .delete()
                    .get();
            return ResponseEntity.ok().build();
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al eliminar usuario: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        try {
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
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al obtener usuario: " + e.getMessage());
        }
    }
} 