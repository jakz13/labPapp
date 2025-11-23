package logica;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.util.Objects;
import java.time.LocalDate;

/**
 * Clase base para todos los usuarios del sistema (clientes y aerolíneas).
 * Contiene datos comunes como nickname, nombre, email, fecha de alta y credenciales.
 */
//@MappedSuperclass
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuario")
public abstract class Usuario {

    /** Nickname único que identifica al usuario en el sistema. */
    @Id
    @Column(unique = true, nullable = false)
    private String nickname;

    /** Nombre completo o razón social del usuario. */
    private String nombre;

    /** Email de contacto del usuario (único). */
    @Column(unique = true, nullable = false)
    private String email;

    /** Fecha en que el usuario fue dado de alta en el sistema. */
    private LocalDate fechaAlta;

    /** Hash de la contraseña (almacenado en Base64, por ejemplo). */
    @Column(name = "password_hash")
    private String passwordHash;

    /** Salt asociado al hash de la contraseña. */
    @Column(name = "password_salt")
    private String passwordSalt;

    /** URL opcional de la imagen o avatar del usuario. */
    @Column(name = "imagen_url")
    private String imagenUrl;

    public Usuario() {
        this.nickname = "";
        this.nombre = "";
        this.email = "";
        this.imagenUrl = null; // Opcional - puede ser null
    }

    /**
     * Crea un usuario con credenciales y datos básicos.
     * @param nickname identificador único
     * @param nombre nombre o razón social
     * @param email dirección de correo
     * @param password contraseña en texto plano (se hashing interno)
     * @param fechaAlta fecha de alta (si es null se asigna la fecha actual)
     * @param imagenUrl URL opcional de la imagen
     */
    public Usuario(String nickname, String nombre, String email, String password, LocalDate fechaAlta, String imagenUrl) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
        this.setImagenUrl(imagenUrl); // Por defecto sin imagen
        this.passwordSalt = PasswordManager.generarSalt();
        this.passwordHash = PasswordManager.hashPassword(password, this.passwordSalt);
        this.fechaAlta = LocalDate.now();
    }

    // ===== Getters y Setters =====
    /** Devuelve el nickname del usuario. */
    public String getNickname() { return nickname; }
    /** Devuelve el nombre del usuario. */
    public String getNombre() { return nombre; }
    /** Devuelve el email del usuario. */
    public String getEmail() { return email; }
    /** Devuelve la URL de la imagen del usuario. */
    public String getImagenUrl() { return imagenUrl; }
    /** Devuelve la fecha de alta. */
    public LocalDate getFechaAlta() { return fechaAlta; }
    /** Devuelve el hash de la contraseña. */
    public String getPasswordHash() { return passwordHash; }
    /** Devuelve el salt de la contraseña. */
    public String getPasswordSalt() { return passwordSalt; }
    /** Actualiza el email del usuario. */
    public void setEmail(String email) { this.email = email; }
    /** Actualiza el nombre del usuario. */
    public void setNombre(String nombre) { this.nombre = nombre; }
    /** Actualiza la URL de la imagen del usuario. */
    public void setImagenUrl(String imagenUrl) {
        if (imagenUrl == null) {
            this.imagenUrl = null;
        } else {
            String v = imagenUrl.trim();
            this.imagenUrl = v.isEmpty() ? null : v;
        }
    }
    /**
     * Actualiza la contraseña del usuario generando un nuevo salt y hash.
     * @param password nueva contraseña en texto plano
     */
    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        this.passwordSalt = PasswordManager.generarSalt();
        this.passwordHash = PasswordManager.hashPassword(password, this.passwordSalt);
    }

    /**
     * Verifica si la contraseña en texto plano coincide con la almacenada.
     * @param password contraseña en texto plano a verificar
     * @return true si coincide, false en caso contrario
     */
    public boolean verificarPassword(String password) {
        return PasswordManager.verificarPassword(password, this.passwordSalt, this.passwordHash);
    }


    // ===== Seguimiento (follow) helpers =====
    // Las implementaciones concretas están en Cliente y Aerolinea.
    public abstract java.util.Set<Usuario> getSeguidores();
    public abstract java.util.Set<Usuario> getSiguiendo();
    public abstract boolean estaSiguiendoA(Usuario otro);
    public abstract void seguirA(Usuario otro);
    public abstract void dejarDeSeguirA(Usuario otro);

    public void agregarSeguidor(Usuario seguidor) {
        this.getSeguidores().add(seguidor);
    }

    public void removeSeguidor(Usuario seguidor) {
        if (seguidor == null) return;

        // sacar de la lista de seguidores del target
        getSeguidores().remove(seguidor);

        // sacar al target de la lista de “siguiendo” del follower
        seguidor.getSiguiendo().remove(this);
    }


    @Override
    public abstract String toString();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(nickname, usuario.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }
}