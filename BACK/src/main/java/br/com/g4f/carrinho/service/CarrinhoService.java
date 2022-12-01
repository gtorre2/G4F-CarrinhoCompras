package br.com.g4f.carrinho.service;

import br.com.g4f.carrinho.entity.Cart;
import br.com.g4f.carrinho.entity.ProductInOrder;
import br.com.g4f.carrinho.entity.User;

import java.util.Collection;

public interface CarrinhoService {
    Cart getCart(User user);

    void mergeLocalCart(Collection<ProductInOrder> productInOrders, User user);

    void delete(String itemId, User user);

    void checkout(User user);
}
