package Logica;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CiudadTest {

    // Test que verifica que el constructor por defecto inicializa todos los campos en null
    @Test
    void constructorPorDefectoInicializaCamposANull() {
        Ciudad c = new Ciudad();
        assertNull(c.getId());
        assertNull(c.getNombre());
        assertNull(c.getPais());
        assertNull(c.getAeropuerto());
        assertNull(c.getDescripcion());
        assertNull(c.getSitioWeb());
        assertNull(c.getFechaAlta());
    }

    // Test que verifica que el constructor con nombre y país asigna correctamente esos campos
    // y deja los demás en null
    @Test
    void constructorConNombreYPaisAsignaCamposCorrectamente() {
        Ciudad c = new Ciudad("Montevideo", "Uruguay");
        assertNull(c.getId());
        assertEquals("Montevideo", c.getNombre());
        assertEquals("Uruguay", c.getPais());
        assertNull(c.getAeropuerto());
        assertNull(c.getDescripcion());
        assertNull(c.getSitioWeb());
        assertNull(c.getFechaAlta());
    }

    // Test que verifica que el constructor completo asigna correctamente todos los campos
    @Test
    void constructorCompletoAsignaTodosLosCampos() {
        LocalDate fecha = LocalDate.of(2020, 1, 15);
        Ciudad c = new Ciudad("Madrid", "España", "Adolfo Suárez", "Capital española", "https://madrid.es", fecha);
        assertEquals("Madrid", c.getNombre());
        assertEquals("España", c.getPais());
        assertEquals("Adolfo Suárez", c.getAeropuerto());
        assertEquals("Capital española", c.getDescripcion());
        assertEquals("https://madrid.es", c.getSitioWeb());
        assertEquals(fecha, c.getFechaAlta());
    }

    // Test que verifica que los setters modifican correctamente los campos
    @Test
    void settersModificanCamposCorrectamente() {
        Ciudad c = new Ciudad();
        c.setNombre("Buenos Aires");
        c.setPais("Argentina");
        c.setAeropuerto("Ezeiza");
        c.setDescripcion("Capital");
        c.setSitioWeb("");
        LocalDate fecha = LocalDate.now();
        c.setFechaAlta(fecha);

        assertEquals("Buenos Aires", c.getNombre());
        assertEquals("Argentina", c.getPais());
        assertEquals("Ezeiza", c.getAeropuerto());
        assertEquals("Capital", c.getDescripcion());
        assertEquals("", c.getSitioWeb());
        assertEquals(fecha, c.getFechaAlta());
    }

    // Test que verifica que los campos opcionales pueden aceptar null sin problemas
    @Test
    void camposOpcionalesAceptanNull() {
        Ciudad c = new Ciudad("Córdoba", "Argentina");
        c.setAeropuerto(null);
        c.setDescripcion(null);
        c.setSitioWeb(null);
        c.setFechaAlta(null);

        assertNull(c.getAeropuerto());
        assertNull(c.getDescripcion());
        assertNull(c.getSitioWeb());
        assertNull(c.getFechaAlta());
    }

    // Test que verifica que los campos opcionales pueden aceptar strings vacíos
    @Test
    void camposOpcionalesAceptanCadenasVacias() {
        Ciudad c = new Ciudad("Lima", "Perú");
        c.setAeropuerto("");
        c.setDescripcion("");
        c.setSitioWeb("");

        assertEquals("", c.getAeropuerto());
        assertEquals("", c.getDescripcion());
        assertEquals("", c.getSitioWeb());
    }

    // Test que verifica que dos instancias diferentes con los mismos datos no son la misma referencia
    @Test
    void instanciasDiferentesNoSonLaMismaReferencia() {
        Ciudad a = new Ciudad("Lima", "Perú");
        Ciudad b = new Ciudad("Lima", "Perú");
        assertNotSame(a, b);
    }

    // Test que verifica que crear una ciudad con nombre null lanza excepción
    @Test
    void crearCiudadConNombreNullLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Ciudad(null, "Argentina"));
    }

    // Test que verifica que crear una ciudad con país null lanza excepción
    @Test
    void crearCiudadConPaisNullLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new Ciudad("Montevideo", null));
    }

    // Test que verifica que dos instancias idénticas de ciudad son iguales por valor
    @Test
    void instanciasIdenticasSonIgualesPorValor() {
        LocalDate fecha = LocalDate.now();
        Ciudad a = new Ciudad("Quito", "Ecuador", "Mariscal", "Descripcion", "", fecha);
        Ciudad b = new Ciudad("Quito", "Ecuador", "Mariscal", "Descripcion", "", fecha);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    // Test que verifica que el constructor completo puede aceptar null en campos opcionales
    @Test
    void constructorCompletoAceptaNullEnOpcionales() {
        Ciudad c = new Ciudad("Valparaíso", "Chile", null, null, null, null);
        assertEquals("Valparaíso", c.getNombre());
        assertEquals("Chile", c.getPais());
        assertNull(c.getAeropuerto());
        assertNull(c.getDescripcion());
        assertNull(c.getSitioWeb());
        assertNull(c.getFechaAlta());
    }
}
