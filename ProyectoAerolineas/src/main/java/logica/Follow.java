package logica;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    @JoinColumn(name = "seguidor_id", referencedColumnName = "nickname", nullable = false)
    private Usuario seguidor;

    // Usuario que es seguido
    @ManyToOne
    @JoinColumn(name = "seguido_id", referencedColumnName = "nickname", nullable = false)
    private Usuario seguido;

    private LocalDateTime fecha;

    // Constructor vacío (obligatorio)
    public Follow() {}

    // Constructor útil
    public Follow(Usuario seguidor, Usuario seguido) {
        this.seguidor = seguidor;
        this.seguido = seguido;
        this.fecha = LocalDateTime.now();
    }

    // Getters correctos
    public Long getId() {
        return id;
    }

    public Usuario getSeguidor() {
        return seguidor;
    }

    public Usuario getSeguido() {
        return seguido;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    // Setters (Hibernate los usa)
    public void setSeguidor(Usuario seguidor) {
        this.seguidor = seguidor;
    }

    public void setSeguido(Usuario seguido) {
        this.seguido = seguido;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String obtenerNicknameSeguido() {
        return seguido.getNickname();
    }

    public String obtenerNicknameSeguidor() {
        return seguidor.getNickname();
    }
}
