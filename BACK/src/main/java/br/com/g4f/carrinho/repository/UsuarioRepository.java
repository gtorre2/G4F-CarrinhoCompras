package br.com.g4f.carrinho.repository;


import br.com.g4f.carrinho.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UsuarioRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    Collection<User> findAllByRole(String role);

}
