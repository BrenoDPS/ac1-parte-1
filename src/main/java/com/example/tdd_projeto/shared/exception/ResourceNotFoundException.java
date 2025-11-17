package com.example.tdd_projeto.shared.exception;

public class ResourceNotFoundException extends BusinessException {
    
    public ResourceNotFoundException(String resourceName, Long id) {
        super("RESOURCE_NOT_FOUND", 
              String.format("%s com ID %d não encontrado", resourceName, id));
    }
    
    public ResourceNotFoundException(String message) {
        super("RESOURCE_NOT_FOUND", message);
    }
}