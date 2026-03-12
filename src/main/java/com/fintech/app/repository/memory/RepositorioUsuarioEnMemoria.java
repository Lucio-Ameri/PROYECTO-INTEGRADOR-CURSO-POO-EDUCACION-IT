package com.fintech.app.repository.memory;

import com.fintech.app.model.Usuario;
import com.fintech.app.repository.RepositorioUsuario;

import java.util.*;

public class RepositorioUsuarioEnMemoria implements RepositorioUsuario {
    private final Map<Integer, Usuario> listadoDeUsuarios;

    public RepositorioUsuarioEnMemoria(){
        this.listadoDeUsuarios = new LinkedHashMap<Integer, Usuario>();
    }

    private String normalizarEmail(String email){
        return email.trim().toLowerCase(Locale.ROOT);
    }

    @Override
    public Usuario guardar(Usuario usuario){
        if(usuario == null){
            throw new IllegalArgumentException("NO SE PUEDE GUARDAR UN NULL");
        }

        Optional<Usuario> usuarioConMismoEmail = buscarPorEmail(usuario.getEmail());
        if(usuarioConMismoEmail.isPresent() && usuarioConMismoEmail.get().getUsuarioID() != usuario.getUsuarioID()){
            throw new IllegalArgumentException("NO SE PUEDE GUARDAR OTRO USUARIO CON EL MISMO EMAIL");
        }

        Optional<Usuario> usuarioConMismoDNI = buscarPorDNI(usuario.getDNI());
        if(usuarioConMismoDNI.isPresent() && usuarioConMismoDNI.get().getUsuarioID() != usuario.getUsuarioID()){
            throw new IllegalArgumentException("NO SE PUEDE GUARDAR OTRO USUARIO CON EL MISMO DNI");
        }

        listadoDeUsuarios.put(usuario.getUsuarioID(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorID(int ID){
        return Optional.ofNullable(listadoDeUsuarios.get(ID));
    }

    @Override
    public  Optional<Usuario> buscarPorEmail(String email){
        if(email == null){
            throw new IllegalArgumentException("NO SE PUEDE BUSCAR UN EMAIL NULL");
        }

        String buscado = normalizarEmail(email);
        return listadoDeUsuarios.values().stream().filter(usuario -> normalizarEmail(usuario.getEmail()).equals(buscado)).findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorDNI(String DNI){
        if(DNI == null){
            throw new IllegalArgumentException("NO SE PUEDE BUSCAR UN DNI NULL");
        }

        return listadoDeUsuarios.values().stream().filter(usuario -> usuario.getDNI().equals(DNI)).findFirst();
    }

    @Override
    public List<Usuario> listarUsuarios(){
        return new ArrayList<Usuario>(listadoDeUsuarios.values());
    }

    @Override
    public void eliminarPorID(int usuarioID){
        listadoDeUsuarios.remove(usuarioID);
    }

    @Override
    public boolean existePorEmail(String email){
        if(email == null){
            throw new IllegalArgumentException("EL EMAIL A BUSCAR NO PUEDE SER NULL");
        }

        String buscado = normalizarEmail(email);
        return listadoDeUsuarios.values().stream().anyMatch(usuario -> normalizarEmail(usuario.getEmail()).equals(buscado));
    }

    @Override
    public boolean existePorDNI(String DNI){
        if(DNI == null){
            throw new IllegalArgumentException("EL DNI A BUSCAR NO PUEDE SER NULL");
        }

        return listadoDeUsuarios.values().stream().anyMatch(usuario -> usuario.getDNI().equals(DNI));
    }
}
