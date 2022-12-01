package br.com.g4f.carrinho.service;

import br.com.g4f.carrinho.entity.ProductInOrder;
import br.com.g4f.carrinho.entity.User;

public interface ProdutoPedidorService {
    void update(String itemId, Integer quantity, User user);
    ProductInOrder findOne(String itemId, User user);
}
