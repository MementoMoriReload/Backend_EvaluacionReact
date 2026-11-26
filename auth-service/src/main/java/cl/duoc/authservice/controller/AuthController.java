package cl.duoc.authservice.controller;

import cl.duoc.authservice.dto.LoginRequest;
import cl.duoc.authservice.dto.RegistroRequest;
import cl.duoc.authservice.dto.UsuarioResponse;
import cl.duoc.authservice.model.Usuario;
import cl.duoc.authservice.repository.UsuarioRepository;
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
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public AuthController(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Operation(summary = "Registrar un nuevo usuario")
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registro(@Valid @RequestBody RegistroRequest r) {
        if (repo.existsByRutOrEmail(r.rut(), r.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "RUT o correo ya registrado");
        }
        Usuario u = new Usuario();
        u.setRut(r.rut());
        u.setNombre(r.nombre());
        u.setEmail(r.email());
        u.setPasswordHash(encoder.encode(r.pass()));
        u.setRol("Empleado"); // el rol nunca viene del cliente
        repo.save(u);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(u));
    }

    @Operation(summary = "Iniciar sesión")
    @PostMapping("/login")
    public UsuarioResponse login(@Valid @RequestBody LoginRequest r) {
        Usuario u = repo.findByRutOrEmail(r.username(), r.username())
                .filter(x -> encoder.matches(r.password(), x.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "RUT/Usuario o contraseña incorrectos."));
        return toDto(u);
    }

    private UsuarioResponse toDto(Usuario u) {
        return new UsuarioResponse(u.getRut(), u.getNombre(), u.getEmail(), u.getRol());
    }
}