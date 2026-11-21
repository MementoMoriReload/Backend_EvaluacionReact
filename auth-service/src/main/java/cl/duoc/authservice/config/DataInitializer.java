package cl.duoc.authservice.config;

import cl.duoc.authservice.model.Usuario;
import cl.duoc.authservice.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarUsuarios(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            crear(repo, encoder, "223162485", "Admin Sistema", "admin@duoc.cl", "1234", "Administrador");
            crear(repo, encoder, "719676767", "Guardia Seguridad", "guardia@duoc.cl", "1234", "Guardia");
            crear(repo, encoder, "267824045", "Empleado Ejemplo", "empleado@duoc.cl", "1234", "Empleado");
        };
    }

    private void crear(UsuarioRepository repo, PasswordEncoder encoder,
                       String rut, String nombre, String email, String pass, String rol) {
        if (repo.existsByRutOrEmail(rut, email)) return;
        Usuario u = new Usuario();
        u.setRut(rut);
        u.setNombre(nombre);
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(pass));
        u.setRol(rol);
        repo.save(u);
    }
}