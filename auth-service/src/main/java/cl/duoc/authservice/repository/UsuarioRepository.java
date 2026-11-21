package cl.duoc.authservice.repository;

import cl.duoc.authservice.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByRutOrEmail(String rut, String email);
    boolean existsByRutOrEmail(String rut, String email);
    
}