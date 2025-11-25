package pe.edu.upeu.CafeSnoopy.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate; // ⬅️ IMPORTANTE: No olvides importar esto

@Entity
@Table(name = "Usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "nombres", length = 150)
    private String nombres;

    @Column(name = "apellidos", length = 150)
    private String apellidos;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @Column(name = "rol", length = 50)
    private String rol = "VENDEDOR";

    // ✅ NUEVO CAMPO: Fecha de registro
    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro = LocalDate.now(); // Se guarda la fecha actual automáticamente
}