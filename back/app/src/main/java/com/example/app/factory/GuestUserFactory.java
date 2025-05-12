package com.example.app.factory;

import java.util.Map;

import com.example.app.model.User;

public class GuestUserFactory implements UserFactory {
    @Override
    public User createUser(String email, String password, Map<String, Object> formData) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("guest");
        
        // Asegurarse de que los campos específicos de guest estén presentes
        Map<String, Object> guestFormData = Map.of(
            "nickname", formData.getOrDefault("nickname", ""),
            "interests", formData.getOrDefault("interests", "")
        );
        user.setFormData(guestFormData);
        
        return user;
    }
} 