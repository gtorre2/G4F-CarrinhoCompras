package br.com.g4f.carrinho.repository;

import br.com.g4f.carrinho.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrinhoRepository extends JpaRepository<Cart, Integer> {
}
