package logica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoriaTest {

    @Test
    void crearCategoriaConNombreValido() {
        Categoria categoria = new Categoria("Economy");
        assertEquals("Economy", categoria.getNombre());
    }

    @Test
    void crearCategoriaConNombreVacio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Categoria(""));
        assertEquals("El nombre no puede estar vacío", exception.getMessage());
    }

    @Test
    void crearCategoriaConNombreNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Categoria(null));
        assertEquals("El nombre no puede ser nulo", exception.getMessage());
    }

    @Test
    void actualizarCategoriaConNombreValido() {
        Categoria categoria = new Categoria("Economy");
        categoria.setNombre("Business");
        assertEquals("Business", categoria.getNombre());
    }

    @Test
    void actualizarCategoriaANombreVacio() {
        Categoria categoria = new Categoria("Economy");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> categoria.setNombre(""));
        assertEquals("El nombre no puede estar vacío", exception.getMessage());
    }

    @Test
    void actualizarCategoriaANombreNull() {
        Categoria categoria = new Categoria("Economy");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> categoria.setNombre(null));
        assertEquals("El nombre no puede ser nulo", exception.getMessage());
    }
}
