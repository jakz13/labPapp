package Logica;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Usuario {

    @Id
    @Column(unique = true, nullable = false)
    private String nickname;  // ✅ clave primaria

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    public Usuario() {
        this.nickname = "";
        this.nombre = "";
        this.email = "";
    }

    public Usuario(String nickname, String nombre, String email) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
    }

    // ===== Getters y Setters =====
    public String getNickname() { return nickname; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return nickname;
    }
}
