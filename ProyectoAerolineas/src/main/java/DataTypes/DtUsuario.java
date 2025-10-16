package DataTypes;

import java.time.LocalDate;

public class DtUsuario {
    private String nickname;
    private String nombre;
    private String email;
    private String imagenUrl;
    private LocalDate fechaAlta;

    public DtUsuario() {
    }

    public DtUsuario(String nickname, String nombre, String email, LocalDate fechaAlta) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
        this.imagenUrl = null;
        this.fechaAlta = fechaAlta;
    }

    public DtUsuario(String nickname, String nombre, String email, String imagenUrl, LocalDate fechaAlta) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
        this.imagenUrl = imagenUrl;
        this.fechaAlta = fechaAlta;
    }

    // --- Getters y Setters ---
    public String getNickname() { return nickname; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getImagenUrl() { return imagenUrl; }
    public LocalDate getFechaAlta() { return fechaAlta; }

    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    @Override
    public String toString() {
        return this.nombre;
    }
}