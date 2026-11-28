package cl.duoc.authservice.service;

import cl.duoc.authservice.dto.*;
import cl.duoc.authservice.exception.ApiException;
import cl.duoc.authservice.model.Rol;
import cl.duoc.authservice.model.Usuario;
import cl.duoc.authservice.repository.UsuarioRepository;
import cl.duoc.authservice.security.JwtService;
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
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@(duoc\\.cl|profesor\\.duoc\\.cl|gmail\\.com)$");

    public List<UsuarioResponse> getAllUsers() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Usuario find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> ApiException.notFound(String.format("Usuario %d no encontrado", id)));
    }

    public UsuarioResponse getUserById(Long id) {
        return mapToDTO(find(id));
    }

    public UsuarioResponse saveUser(RegistroRequest u) {
        String name = u.getNombre() == null ? "" : u.getNombre().trim();
        String email = u.getEmail() == null ? "" : u.getEmail().trim().toLowerCase();
        String rut = u.getRut() == null ? "" : u.getRut().trim();

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw ApiException.badRequest("Correo inválido (use @duoc.cl, @profesor.duoc.cl o @gmail.com)");
        }

        if (repository.findByEmail(email).isPresent() || repository.findByRut(rut).isPresent()) {
            throw ApiException.badRequest("El usuario ya está registrado");
        }

        if (!NAME_PATTERN.matcher(name).matches()) {
            throw ApiException.badRequest("El nombre solo puede contener letras");
        }

        Usuario user = new Usuario();
        user.setRut(rut);
        user.setNombre(name);
        user.setEmail(email);
        user.setPasswordHash(encoder.encode(u.getPass()));
        user.setRol(Rol.EMPLEADO);

        return mapToDTO(repository.save(user));
    }

    public AuthResponse login(LoginRequest login) {
        String username = login.getUsername() == null ? "" : login.getUsername().trim();

        Usuario u = repository.findByRut(username)
                .or(() -> repository.findByEmail(username.toLowerCase()))
                .orElseThrow(() -> ApiException.unauthorized("Correo o contraseña incorrectas"));

        if (!encoder.matches(login.getPassword(), u.getPasswordHash())) {
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

    private UsuarioResponse mapToDTO(Usuario u) {
        return new UsuarioResponse(
                u.getRut(),
                u.getNombre(),
                u.getEmail(),
                u.getRol());
    }
}