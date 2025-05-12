package com.example.app.model;

public interface FormField {
    /**
     * Retorna el tipo del campo de formulario (text, email, password, etc.)
     */
    String getType();

    /**
     * Retorna la etiqueta o nombre del campo de formulario
     */
    String getLabel();

    /**
     * Indica si el campo es obligatorio
     */
    boolean isRequired();
} 