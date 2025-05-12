package com.example.app.factory;

import com.example.app.model.FormField;
import com.example.app.model.GenericField;
import java.util.List;

public class GuestFormFieldFactory implements FormFieldFactory {
    @Override
    public List<FormField> createFormFields() {
        return List.of(
            new GenericField("text", "Nickname", false),
            new GenericField("text", "Interests", false),
            new GenericField("textarea", "Comments", false)
        );
    }
} 