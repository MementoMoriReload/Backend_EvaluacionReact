package com.example.Edificio.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(ApiException.class)
        ResponseEntity<Map<String, Object>> api(ApiException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(Map.of("", e.getMessage()));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        ResponseEntity<Map<String, Object>> validacion(MethodArgumentNotValidException e) {
            Map<String, String> errores = new LinkedHashMap<>();
            e.getBindingResult().getFieldErrors()
                    .forEach(f -> errores.putIfAbsent(f.getField(), f.getDefaultMessage()));
            return ResponseEntity.badRequest()
                    .body(Map.of("", errores));
        }
}
