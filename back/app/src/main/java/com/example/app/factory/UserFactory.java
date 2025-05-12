package com.example.app.factory;

import java.util.Map;

import com.example.app.model.User;

public interface UserFactory {
    User createUser(String email, String password, Map<String, Object> formData);
} 