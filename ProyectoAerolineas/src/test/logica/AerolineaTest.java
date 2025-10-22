package logica;

import DataTypes.DtRutaVuelo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AerolineaTest {

    private Aerolinea aerolinea;

    @BeforeEach
    void setUp() {
        // Inicializa la aerolínea antes de cada test
        aerolinea = new Aerolinea(
                "aeroTest",
                "Aerolinea Test",
                "test@aero.com",
                "12345",
                "Aerolinea de prueba",
                "https://aerotest.com",
                "imagen.png"
        );
    }

    // --- Test de constructor y getters ---
    @Test
    void constructorYGettersDeberianRetornarDatosCorrectos() {
        // Verifica que el constructor inicializa correctamente los campos
        // Se espera que cada getter retorne el valor asignado en el constructor
        assertEquals("aeroTest", aerolinea.getNickname());
        assertEquals("Aerolinea Test", aerolinea.getNombre());
        assertEquals("test@aero.com", aerolinea.getEmail());
        assertEquals("Aerolinea de prueba", aerolinea.getDescripcion());
        assertEquals("https://aerotest.com", aerolinea.getSitioWeb());
        assertNotNull(aerolinea.getFechaAlta(), "La fecha de alta debería ser asignada automáticamente");
    }

    // --- Test de setters ---
    @Test
    void settersDeberianActualizarCamposCorrectamente() {
        // Verifica que los setters modifican los campos correctamente
        // Se espera que los valores asignados sean los mismos que retornen los getters
        aerolinea.setDescripcion("Nueva descripción");
        aerolinea.setSitioWeb("https://nuevaweb.com");

        assertEquals("Nueva descripción", aerolinea.getDescripcion());
        assertEquals("https://nuevaweb.com", aerolinea.getSitioWeb());
    }

    // --- Test agregar rutas ---
    @Test
    void agregarRutaVueloDeberiaAgregarCorrectamente() {
        // Crea una ruta de prueba y la agrega a la aerolínea
        // Se espera que la ruta exista en el mapa interno de rutas
        RutaVuelo ruta = crearRutaDePrueba("Montevideo - Madrid");
        aerolinea.agregarRutaVuelo(ruta);

        Map<String, RutaVuelo> mapa = aerolinea.getRutasVueloMap();
        assertTrue(mapa.containsKey("Montevideo - Madrid"));
        assertEquals(ruta, mapa.get("Montevideo - Madrid"));
    }

    // --- Test obtención de rutas en formato DTO ---
    @Test
    void getRutasVueloDeberiaRetornarListaDeDtRutaVuelo() {
        // Agrega una ruta y verifica que getRutasVuelo retorne correctamente la lista de DTOs
        // Se espera que todos los campos del DTO correspondan a la ruta agregada
        RutaVuelo ruta = crearRutaDePrueba("Montevideo - Madrid");
        aerolinea.agregarRutaVuelo(ruta);

        List<DtRutaVuelo> rutas = aerolinea.getRutasVuelo();
        assertEquals(1, rutas.size());

        DtRutaVuelo dto = rutas.get(0);
        assertEquals("Montevideo - Madrid", dto.getNombre());
        assertEquals("Vuelo directo a Madrid", dto.getDescripcion());
        assertEquals("Aerolinea Test", dto.getAerolinea());
        assertEquals("Montevideo", dto.getCiudadOrigen());
        assertEquals("Madrid", dto.getCiudadDestino());
        assertEquals("INGRESADA", dto.getEstado());
    }

    @Test
    void getDtRutasVueloDeberiaRetornarListaCompleta() {
        // Agrega dos rutas y verifica que getDtRutasVuelo retorne ambos DTOs
        RutaVuelo ruta1 = crearRutaDePrueba("Montevideo - Madrid");
        RutaVuelo ruta2 = crearRutaDePrueba("Madrid - Roma");

        aerolinea.agregarRutaVuelo(ruta1);
        aerolinea.agregarRutaVuelo(ruta2);

        List<DtRutaVuelo> dtList = aerolinea.getDtRutasVuelo();
        assertEquals(2, dtList.size());
        assertTrue(dtList.stream().anyMatch(dt -> dt.getNombre().equals("Montevideo - Madrid")));
        assertTrue(dtList.stream().anyMatch(dt -> dt.getNombre().equals("Madrid - Roma")));
    }

    // --- Test validación de datos inválidos ---
    @Test
    void agregarRutaNullDeberiaLanzarExcepcion() {
        // Se espera que intentar agregar una ruta null lance IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> aerolinea.agregarRutaVuelo(null));
    }

    @Test
    void crearAerolineaConNicknameNullDeberiaLanzarExcepcion() {
        // Se espera que crear una aerolínea con nickname null lance IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () ->
                new Aerolinea(null, "Nombre", "email@aero.com", "123", "desc", "https://web.com", "img.png"));
    }

    @Test
    void crearAerolineaConNombreNullDeberiaLanzarExcepcion() {
        // Se espera que crear una aerolínea con nombre null lance IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () ->
                new Aerolinea("nick", null, "email@aero.com", "123", "desc", "https://web.com", "img.png"));
    }

    // --- Test igualdad y hashCode ---
    @Test
    void aerolineasConMismoNicknameDeberianSerIguales() {
        // Dos aerolíneas con el mismo nickname se consideran iguales
        // Se espera que equals y hashCode coincidan
        Aerolinea otra = new Aerolinea(
                "aeroTest",
                "Aerolinea Diferente",
                "otro@aero.com",
                "6789",
                "Descripción",
                "https://otra.com",
                "img2.png"
        );

        assertEquals(aerolinea, otra);
        assertEquals(aerolinea.hashCode(), otra.hashCode());
    }

    // --- Test rutas con datos límite ---
    @Test
    void rutasConMismoNombrePeroDistintoDestinoNoDeberianSerIguales() {
        // Rutas con el mismo nombre pero distintos destinos no deberían considerarse iguales
        RutaVuelo ruta1 = crearRutaDePrueba("Montevideo - Madrid");
        RutaVuelo ruta2 = crearRutaDePrueba("Montevideo - París");

        assertNotEquals(ruta1, ruta2);
    }

    // --- Método auxiliar para crear rutas de prueba ---
    private RutaVuelo crearRutaDePrueba(String nombre) {
        // Crea una ruta de vuelo con datos de prueba para los tests
        RutaVuelo ruta = new RutaVuelo();
        ruta.setNombre(nombre);
        ruta.setDescripcion("Vuelo directo a " + nombre.split(" - ")[1]);
        ruta.setDescripcionCorta("Corta descripción");
        ruta.setAerolinea(aerolinea);
        ruta.setCiudadOrigen("Montevideo");
        ruta.setCiudadDestino(nombre.split(" - ")[1]);
        ruta.setHora("10:30");
        ruta.setFechaAlta(LocalDate.of(2025, 10, 20));
        ruta.setCostoTurista(500.0);
        ruta.setCostoEjecutivo(1000.0);
        ruta.setCostoEquipajeExtra(50.0);
        return ruta;
    }
}
