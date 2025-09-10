package DataTypes;

public class DtUsuario {
    private String nickname;
    private String nombre;
    private String email;

    public DtUsuario() {
    }

    public DtUsuario(String nickname, String nombre, String email) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
    }

    // --- Getters y Setters ---
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "DtUsuario{" +
                "nickname='" + nickname + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
