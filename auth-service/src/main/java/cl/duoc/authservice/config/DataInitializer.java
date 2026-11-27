package cl.duoc.authservice.config;

import cl.duoc.authservice.model.Rol;
import cl.duoc.authservice.model.Usuario;
import cl.duoc.authservice.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Configuration
public class DataInitializer implements  CommandLineRunner {
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        crear("223162485", "Admin Sistema", "admin@duoc.cl", Rol.ADMINISTRADOR);
        crear("719676767", "Guardia Seguridad", "guardia@duoc.cl", Rol.GUARDIA);
        crear("267824045", "Empleado Ejemplo", "empleado@duoc.cl", Rol.EMPLEADO);
    }

    private void crear(String rut, String nombre, String email, Rol rol) {
        if (repo.findByEmail(email).isPresent() || repo.findByRut(rut).isPresent()) {
            return;
        }
        Usuario u = new Usuario();
        u.setRut(rut);
        u.setNombre(nombre);
        u.setEmail(email);
        u.setPasswordHash(encoder.encode("1234"));
        u.setRol(rol);
        repo.save(u);
    }
}