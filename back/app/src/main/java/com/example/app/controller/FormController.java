package com.example.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.app.factory.AdminFormFieldFactory;
import com.example.app.factory.FormFieldFactory;
import com.example.app.factory.GuestFormFieldFactory;
import com.example.app.model.FormField;
import com.example.app.model.LoginRequest;
import com.example.app.service.FirebaseFormService;

@RestController
@RequestMapping("/form")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class FormController {

    private final FirebaseFormService firebaseFormService;

    public FormController(FirebaseFormService firebaseFormService) {
        this.firebaseFormService = firebaseFormService;
    }

    // Recibe un objeto JSON con la información de inicio de sesión (correo y contraseña)
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        if ("admin@email.com".equals(email) && "admin123".equals(password)) {
            return Map.of("role", "admin");
        } else if ("guest@email.com".equals(email) && "guest123".equals(password)) {
            return Map.of("role", "guest");
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }
    }

    // Este método maneja las solicitudes GET a la ruta /form/{role}, donde {role} es un parámetro en la URL
    @GetMapping("/{role}")
    public List<FormField> getForm(@PathVariable String role) {
        try {
            // Primero intentamos obtener la configuración desde Firebase
            List<FormField> firebaseFields = firebaseFormService.getFormFieldsFromFirebase(role);
            
            // Si hay campos en Firebase, los retornamos
            if (!firebaseFields.isEmpty()) {
                return firebaseFields;
            }

            // Si no hay campos en Firebase, usamos la implementación por defecto
            FormFieldFactory factory;
            switch (role.toLowerCase()) {
                case "admin":
                    factory = new AdminFormFieldFactory();
                    break;
                case "guest":
                    factory = new GuestFormFieldFactory();
                    break;
                default:
                    throw new IllegalArgumentException("Rol no válido");
            }

            return factory.createFormFields();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Error al obtener la configuración del formulario: " + e.getMessage());
        }
    }
}
