package Logica;

import DataTypes.DtRutaVuelo;
import DataTypes.DtVuelo;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rutas_vuelo")
public class RutaVuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "aerolinea_id")
    private Aerolinea aerolinea;

    private String ciudadOrigen;
    private String ciudadDestino;
    private String hora;
    private LocalDate fechaAlta;

    private double costoTurista;
    private double costoEjecutivo;
    private double costoEquipajeExtra;

    @ElementCollection
    @CollectionTable(name = "ruta_categorias", joinColumns = @JoinColumn(name = "ruta_id"))
    @Column(name = "categoria")
    private List<String> categorias = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ruta_vuelo_id")
    private List<Vuelo> vuelos = new ArrayList<>();

    public RutaVuelo() {} // constructor vacío para JPA

    public RutaVuelo(String nombre, String descripcion, Aerolinea aerolinea, String ciudadOrigen, String ciudadDestino, String hora, LocalDate fechaAlta, double costoTurista, double costoEjecutivo, double costoEquipajeExtra, List<String> categorias) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.aerolinea = aerolinea;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.hora = hora;
        this.fechaAlta = fechaAlta;
        this.costoTurista = costoTurista;
        this.costoEjecutivo = costoEjecutivo;
        this.costoEquipajeExtra = costoEquipajeExtra;
        this.categorias = categorias;
    }

    // ===== Getters y Setters =====
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Aerolinea getAerolinea() { return aerolinea; }
    public String getCiudadOrigen() { return ciudadOrigen; }
    public String getCiudadDestino() { return ciudadDestino; }
    public String getHora() { return hora; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public double getCostoTurista() { return costoTurista; }
    public double getCostoEjecutivo() { return costoEjecutivo; }
    public double getCostoEquipajeExtra() { return costoEquipajeExtra; }
    public List<String> getCategorias() { return categorias; }
    public List<Vuelo> getVuelos() { return vuelos; }

    public void agregarVuelo(Vuelo vuelo) {
        vuelos.add(vuelo);
    }

    public Vuelo getVuelo(String nombreVuelo) {
        for (Vuelo v : vuelos) {
            if (v.getNombre().equals(nombreVuelo)) return v;
        }
        return null;
    }

    @Override
    public String toString() { return nombre; }

    public List<DtVuelo> getDtVuelos() {
        List<DtVuelo> dtVuelos = new ArrayList<>();
        for (Vuelo v : vuelos) {
            new DtRutaVuelo(
                    v.getRutaVuelo().getNombre(),
                    v.getRutaVuelo().getDescripcion(),
                    v.getRutaVuelo().getAerolinea() != null ? v.getRutaVuelo().getAerolinea().getNombre() : null,
                    v.getRutaVuelo().getCiudadOrigen(),
                    v.getRutaVuelo().getCiudadDestino(),
                    v.getRutaVuelo().getHora(),
                    v.getRutaVuelo().getFechaAlta(),
                    v.getRutaVuelo().getCostoTurista(),
                    v.getRutaVuelo().getCostoEjecutivo(),
                    v.getRutaVuelo().getCostoEquipajeExtra(),
                    v.getRutaVuelo().getCategorias(),
                    new ArrayList<>() // o puedes pasar null o una lista de DtVuelo si corresponde
            );
        }
        return dtVuelos;
    }

    public DataTypes.DtRutaVuelo getDtRutaVuelo() {
        // Obtener el nombre de la aerolínea (o null si no hay)
        String nombreAerolinea = (aerolinea != null) ? aerolinea.getNombre() : null;

        // Obtener la lista de nombres de vuelos
        List<String> nombresVuelos = new ArrayList<>();
        for (Vuelo v : vuelos) {
            nombresVuelos.add(v.getNombre());
        }

        // Obtener la lista de DtVuelo
        List<DtVuelo> dtVuelos = getDtVuelos();

        return new DataTypes.DtRutaVuelo(
                nombre,
                descripcion,
                nombreAerolinea,
                ciudadOrigen,
                ciudadDestino,
                hora,
                fechaAlta,
                costoTurista,
                costoEjecutivo,
                costoEquipajeExtra,
                categorias,
                dtVuelos
        );
    }


}
