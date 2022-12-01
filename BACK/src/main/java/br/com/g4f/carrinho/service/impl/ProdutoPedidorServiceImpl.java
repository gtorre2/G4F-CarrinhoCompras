package br.com.g4f.carrinho.service.impl;

import br.com.g4f.carrinho.entity.ProductInOrder;
import br.com.g4f.carrinho.entity.User;
import br.com.g4f.carrinho.repository.ProdutoPedidoRepository;
import br.com.g4f.carrinho.service.ProdutoPedidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class ProdutoPedidorServiceImpl implements ProdutoPedidorService {

    @Autowired
    ProdutoPedidoRepository produtoPedidoRepository;

    @Override
    @Transactional
    public void update(String itemId, Integer quantity, User user) {
        var op = user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        op.ifPresent(productInOrder -> {
            productInOrder.setCount(quantity);
            produtoPedidoRepository.save(productInOrder);
        });

    }

    @Override
    public ProductInOrder findOne(String itemId, User user) {
        var op = user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        AtomicReference<ProductInOrder> res = new AtomicReference<>();
        op.ifPresent(res::set);
        return res.get();
    }
}
