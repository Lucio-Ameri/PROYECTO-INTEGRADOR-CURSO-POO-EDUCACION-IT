package com.fintech.app.repository;

import com.fintech.app.model.Usuario;
import com.fintech.app.repository.memory.RepositorioUsuarioEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestRepositorioUsuario {
    private RepositorioUsuarioEnMemoria listado;

    @BeforeEach
    public void iniciarListado(){
        listado = new RepositorioUsuarioEnMemoria();
    }

    private Usuario crearUsuario(int ID, String email, String DNI){
        return new Usuario(ID, "Pepito", "Pepon", email, "Password1234!", DNI);
    }

    @Test
    public void TestGuardarUsuarioDeberiaDevolverElMismoUsuario(){
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");

        Usuario guardado = listado.guardar(usuario);

        assertEquals(usuario, guardado);
    }

    @Test
    public void TestGuardarUsuarioLanzaExcepcionCuandoElUsuarioEsNull(){
        assertThrows(IllegalArgumentException.class, () -> listado.guardar(null));
    }

    @Test
    public void TestGuardarUsuarioConUnEmailRepetidoLanzaExcepcion(){
        Usuario usuarioOriginal = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Usuario emailRepetido = crearUsuario(2, "Pepito@gmail.com", "11111111");

        listado.guardar(usuarioOriginal);
        assertThrows(IllegalArgumentException.class, () -> listado.guardar(emailRepetido));
    }

    @Test
    public void TestGuardarUsuarioConUnDNIRepetidoLanzaExcepcion(){
        Usuario usuarioOriginal = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Usuario DNIRepetido = crearUsuario(2, "Pepito23@gmail.com", "12345678");

        listado.guardar(usuarioOriginal);
        assertThrows(IllegalArgumentException.class, () -> listado.guardar(DNIRepetido));
    }

    @Test //No tira error cuando tiene mismo email y mismo DNI, ya que tiene el mismo ID.
    public void TestGuardarUsuarioConMismoIDActualizaElYaExistenteSinErrores(){
        Usuario usuarioOriginal = crearUsuario(1, "lucio@gmail.com", "12345678");
        Usuario usuarioNuevo = crearUsuario(1, "lucio.nuevo@gmail.com", "11111111");

        listado.guardar(usuarioOriginal);
        listado.guardar(usuarioNuevo);

        Optional<Usuario> resultado = listado.buscarPorID(1);

        assertTrue(resultado.isPresent());
        assertEquals(usuarioNuevo, resultado.get());
        assertEquals("lucio.nuevo@gmail.com", resultado.get().getEmail());
        assertEquals("11111111", resultado.get().getDNI());
        assertEquals(1, listado.listarUsuarios().size());
    }

    @Test
    public void TestBuscarPorIDCuandoExisteElUsuarioLoDevuelve(){
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");
        listado.guardar(usuario);

        Optional<Usuario> encontrado = listado.buscarPorID(1);
        assertTrue(encontrado.isPresent());
        assertEquals(usuario, encontrado.get());
    }

    @Test
    public void TestBuscarPorIDCuandoNoExisteElUsuarioDevuelveEmpty(){
        assertTrue(listado.buscarPorID(29).isEmpty());
    }

    @Test
    public void TestBuscarPorEmailCuandoExisteElEmailLoDevuelve(){
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");
        listado.guardar(usuario);

        Optional<Usuario> encontrado = listado.buscarPorEmail("Pepito@gmail.com");
        assertTrue(encontrado.isPresent());
        assertEquals(usuario, encontrado.get());
    }

    @Test
    public void TestBuscarPorEmailCuandoNoExisteElEmailDevuelveEmpty(){
        assertTrue(listado.buscarPorEmail("NoExiste@gmail.com").isEmpty());
    }

    @Test
    public void TestBuscarPorEmailLanzaExcepcionCuandoElEmailEsNull(){
        assertThrows(IllegalArgumentException.class, () -> listado.buscarPorEmail(null));
    }

    @Test
    public void TestBuscarPorDNICuandoExisteElDNILoDevuelve(){
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");
        listado.guardar(usuario);

        Optional<Usuario> encontrado = listado.buscarPorDNI("12345678");
        assertTrue(encontrado.isPresent());
        assertEquals(usuario, encontrado.get());
    }

    @Test
    public void TestBuscarPorDNICuandoNoExisteElDNIDevuelveEmpty(){
        assertTrue(listado.buscarPorDNI("12345678").isEmpty());
    }

    @Test
    public void TestBuscarPorDNILanzaExcepcionCuandoElDNIEsNull(){
        assertThrows(IllegalArgumentException.class, () -> listado.buscarPorDNI(null));
    }

    @Test
    public void TestListarUsuariosDevuelveUnaListaCompletaDeEllosSiEsQueHay(){
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Usuario usuario2 = crearUsuario(2, "Pepito2@gmail.com", "11111111");
        Usuario usuario3 = crearUsuario(3, "Pepit3@gmail.com", "22222222");

        listado.guardar(usuario);
        listado.guardar(usuario2);
        listado.guardar(usuario3);

        List<Usuario> usuariosEncontrados = listado.listarUsuarios();

        assertEquals(3, usuariosEncontrados.size());
        assertTrue(usuariosEncontrados.contains(usuario));
        assertTrue(usuariosEncontrados.contains(usuario2));
        assertTrue(usuariosEncontrados.contains(usuario3));
    }

    @Test
    public void TestListarUsuariosDevuelveUnaListaVaciaDeEllosSiEsQueNoHay(){
        assertTrue(listado.listarUsuarios().isEmpty());
    }

    @Test
    public void TestEliminarPorIDCuandoExisteElUsuarioSeElimina(){
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");

        listado.guardar(usuario);
        listado.eliminarPorID(1);

        assertTrue(listado.listarUsuarios().isEmpty());
    }

    @Test
    public void TestEliminarPorIDCuandoNoExisteElUsuarioNoSeRompeElCodigo(){
        assertDoesNotThrow(() -> listado.eliminarPorID(99));
    }

    @Test
    public void TestExistePorEmailDevuelveTrueSiEsQueLoEncontramos(){
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");

        listado.guardar(usuario);
        assertTrue(listado.existePorEmail("Pepito@gmail.com"));
    }

    @Test
    public void TestExistePorEmailDevuelveFalseSiEsQueNoLoEncontramos(){
        assertFalse(listado.existePorEmail("Pepito@gmail.com"));
    }

    @Test
    public void TestExistePorEmailLanzaExcepcionSiSePasaUnNull(){
        assertThrows(IllegalArgumentException.class, () -> listado.existePorEmail(null));
    }

    @Test
    public void TestExistePorDNIDevuelveTrueSiEsQueLoEncontramos(){
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");

        listado.guardar(usuario);
        assertTrue(listado.existePorDNI("12345678"));
    }

    @Test
    public void TestExistePorDNIDevuelveFalseSiEsQueNoLoEncontramos(){
        assertFalse(listado.existePorEmail("12345678"));
    }

    @Test
    public void TestExistePorDNILanzaExcepcionSiSePasaUnNull(){
        assertThrows(IllegalArgumentException.class, () -> listado.existePorDNI(null));
    }
}
