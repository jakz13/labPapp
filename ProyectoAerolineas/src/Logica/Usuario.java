public abstract class Usuario {
    private String nickname;
    private String nombre;
    private String email;

    public Usuario(String nickname, String nombre, String email) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario() {
        this.nickname = "";
        this.nombre = "";
        this.email = "";
    }
    public String getNickname() { return nickname; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
}
