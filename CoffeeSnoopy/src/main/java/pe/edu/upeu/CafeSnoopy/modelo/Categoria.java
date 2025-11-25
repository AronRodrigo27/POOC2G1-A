package pe.edu.upeu.CafeSnoopy.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Categoria")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(name = "nombre_categoria", unique = true, length = 100, nullable = false)
    private String nombreCategoria;
}