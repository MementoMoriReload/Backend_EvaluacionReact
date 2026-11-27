package cl.duoc.authservice.controller;

import cl.duoc.authservice.dto.AuthResponse;
import cl.duoc.authservice.dto.LoginRequest;
import cl.duoc.authservice.dto.RegistroRequest;
import cl.duoc.authservice.dto.UsuarioResponse;
import cl.duoc.authservice.model.Usuario;
import cl.duoc.authservice.repository.UsuarioRepository;
import cl.duoc.authservice.security.JwtService;
import cl.duoc.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Auth", description = "Registro y login")
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService service;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(AuthService service, PasswordEncoder encoder, JwtService jwt) {
        this.service = service;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @Operation(summary = "Registrar un nuevo usuario")
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registro(@Valid @RequestBody RegistroRequest r) {
        UsuarioResponse created  = service.saveUser(r);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Iniciar sesión")
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest r) {
        return service.login(r);
    }
}