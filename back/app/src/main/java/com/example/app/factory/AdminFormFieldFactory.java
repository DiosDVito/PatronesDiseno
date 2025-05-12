package com.example.app.factory;

import com.example.app.model.FormField;
import com.example.app.model.GenericField;
import java.util.List;

public class AdminFormFieldFactory implements FormFieldFactory {
    @Override
    public List<FormField> createFormFields() {
        return List.of(
            new GenericField("text", "Username", true),
            new GenericField("email", "Email", true),
            new GenericField("password", "Admin Key", true)
        );
    }
} 