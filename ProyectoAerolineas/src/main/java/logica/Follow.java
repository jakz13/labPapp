package logica;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "follow",
        uniqueConstraints = @UniqueConstraint(columnNames = {"seguidor_id", "seguido_id"})
)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Usuario que sigue
    @ManyToOne
    @JoinColumn(name = "seguidor_id", nullable = false)
    private Usuario seguidor;

    // Usuario que es seguido
    @ManyToOne
    @JoinColumn(name = "seguido_id", nullable = false)
    private Usuario seguido;

    private LocalDateTime fecha = LocalDateTime.now();

    // Constructor vacío
    public Follow() {}

    // Constructor útil
    public Follow(Usuario seguidor, Usuario seguido) {
        this.seguidor = seguidor;
        this.seguido = seguido;
        this.fecha = LocalDateTime.now();
    }

    // getters y setters
}


