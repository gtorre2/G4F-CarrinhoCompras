package br.com.g4f.carrinho.service;


import br.com.g4f.carrinho.entity.User;

import java.util.Collection;

public interface UsuarioService {
    User findOne(String email);

    Collection<User> findByRole(String role);

    User save(User user);

    User update(User user);
}
