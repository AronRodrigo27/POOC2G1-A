package pe.edu.upeu.CafeSnoopy.repositorio;

import pe.edu.upeu.CafeSnoopy.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {}