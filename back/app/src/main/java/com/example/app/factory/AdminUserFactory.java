package com.example.app.factory;

import java.util.Map;

import com.example.app.model.User;

public class AdminUserFactory implements UserFactory {
    @Override
    public User createUser(String email, String password, Map<String, Object> formData) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("admin");
        
        // Asegurarse de que los campos específicos de admin estén presentes
        Map<String, Object> adminFormData = Map.of(
            "username", formData.getOrDefault("username", ""),
            "adminKey", formData.getOrDefault("adminKey", "")
        );
        user.setFormData(adminFormData);
        
        return user;
    }
} 