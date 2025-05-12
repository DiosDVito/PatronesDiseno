package com.example.app.config;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class FirebaseInitializer {

    @Bean
    public CommandLineRunner initializeFirestore() {
        return args -> {
            Firestore firestore = FirestoreClient.getFirestore();

            // Configuración para admin
            Map<String, Object> adminConfig = Map.of(
                "fields", List.of(
                    Map.of(
                        "type", "text",
                        "label", "Username",
                        "required", true
                    ),
                    Map.of(
                        "type", "email",
                        "label", "Email",
                        "required", true
                    ),
                    Map.of(
                        "type", "password",
                        "label", "Admin Key",
                        "required", true
                    )
                )
            );

            // Configuración para guest
            Map<String, Object> guestConfig = Map.of(
                "fields", List.of(
                    Map.of(
                        "type", "text",
                        "label", "Nickname",
                        "required", false
                    ),
                    Map.of(
                        "type", "text",
                        "label", "Interests",
                        "required", false
                    ),
                    Map.of(
                        "type", "textarea",
                        "label", "Comments",
                        "required", false
                    )
                )
            );

            // Crear los documentos
            firestore.collection("formConfigs")
                    .document("admin")
                    .set(adminConfig)
                    .get();

            firestore.collection("formConfigs")
                    .document("guest")
                    .set(guestConfig)
                    .get();

            System.out.println("Estructura de Firebase inicializada correctamente");
        };
    }
} 