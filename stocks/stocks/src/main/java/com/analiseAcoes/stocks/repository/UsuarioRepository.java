package com.analiseAcoes.stocks.repository;

import java.util.Optional;

import com.analiseAcoes.stocks.model.Usuario;

public interface UsuarioRepository {

    Optional<Usuario> findById(Long id);

     default Boolean verificarSeEPro(Long usuarioId) {
        if (usuarioId == null) return false;
        return findById(usuarioId).map(Usuario::isProAtivo).orElse(false);
    }
}
