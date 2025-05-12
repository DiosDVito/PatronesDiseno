package com.example.app.factory;

import com.example.app.model.FormField;
import java.util.List;

public interface FormFieldFactory {
    /**
     * Método abstracto que debe ser implementado por las fábricas concretas
     * para crear los campos específicos del formulario según el tipo de usuario.
     */
    List<FormField> createFormFields();
} 