package cl.duoc.authservice.service;

import cl.duoc.authservice.dto.*;
import cl.duoc.authservice.exception.ApiException;
import cl.duoc.authservice.model.Rol;
import cl.duoc.authservice.model.Usuario;
import cl.duoc.authservice.repository.UsuarioRepository;
import cl.duoc.authservice.security.JwtService;
import io.jsonwebtoken.Jwt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class AuthService {
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UsuarioRepository repository, PasswordEncoder encoder, JwtService jwt) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L}]+(?:[ '-][\\p{L}]+)*$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@duoc\\.cl$");


    public List<UsuarioResponse> getAllUsers() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Usuario find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Usuario %d no encontrado", +id)));
    }

    public UsuarioResponse getUserById(Long id) {
        return mapToDTO(find(id));
    }

    public UsuarioResponse saveUser(RegistroRequest u) {
        String name = u.getNombre() == null ? "" : u.getNombre().trim();
        String email = u.getEmail() == null ? "" : u.getEmail().trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new RuntimeException("El correo electrónico debe pertenecer al dominio @duoc.cl");
        }

        if (repository.findByEmail(email).isPresent() || repository.findByRut(u.getRut()).isPresent()) {
            throw new RuntimeException("El usuario ya está registrado");
        }

        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new RuntimeException("El nombre solo puede contener letras");
        }

        Usuario user = new Usuario();
        user.setRut(u.getRut());
        user.setNombre(name);
        user.setEmail(email);
        user.setPasswordHash(encoder.encode(u.getPass()));
        user.setRol(u.getRol()
                != null ? u.getRol() : Rol.EMPLEADO);

        return mapToDTO(repository.save(user));
    }

    public AuthResponse login(LoginRequest login){
        String username = login.getUsername() == null ? "" : login.getUsername().trim();

        Usuario u = repository.findByRut(username)
                .or(() -> repository.findByEmail(username))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        if (!encoder.matches(login.getPassword(), u.getPasswordHash())){
            throw ApiException.unauthorized("Correo o contraseña incorrectas");
        }
        String token = jwt.generar(u);
        UsuarioResponse dto = mapToDTO(u);

        return new AuthResponse(
                token,
                "Bearer",
                jwt.getExpiresInSeconds(),
                dto
        );

    }

    private UsuarioResponse mapToDTO(Usuario u){
        return new UsuarioResponse(
                u.getRut(),
                u.getNombre(),
                u.getEmail(),
                u.getRol());
    }

}
