package com.example.app.service;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.example.app.model.FormField;
import com.example.app.model.GenericField;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseFormService {
    private final Firestore firestore;

    public FirebaseFormService(FirebaseApp firebaseApp) {
        this.firestore = FirestoreClient.getFirestore(firebaseApp);
    }

    public List<FormField> getFormFieldsFromFirebase(String userType) throws ExecutionException, InterruptedException {
        var formConfig = firestore.collection("formConfigs")
                .document(userType)
                .get()
                .get()
                .getData();

        if (formConfig == null) {
            return List.of(); // Retorna lista vacía si no hay configuración
        }

        List<FormField> fields = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> fieldsConfig = (List<Map<String, Object>>) formConfig.get("fields");

        if (fieldsConfig != null) {
            for (Map<String, Object> fieldConfig : fieldsConfig) {
                fields.add(new GenericField(
                    (String) fieldConfig.get("type"),
                    (String) fieldConfig.get("label"),
                    (Boolean) fieldConfig.get("required")
                ));
            }
        }

        return fields;
    }
} 