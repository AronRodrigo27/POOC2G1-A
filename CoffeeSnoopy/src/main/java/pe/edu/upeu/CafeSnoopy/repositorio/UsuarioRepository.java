package pe.edu.upeu.CafeSnoopy.repositorio;

import pe.edu.upeu.CafeSnoopy.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate; // ⬅️ IMPORTANTE
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);

    // Para contar usuarios activos/inactivos
    long countByEstado(Boolean estado);

    // ✅ NUEVO: Para contar usuarios registrados HOY
    long countByFechaRegistro(LocalDate fecha);
}