package br.com.g4f.carrinho.service.impl;

import br.com.g4f.carrinho.entity.Cart;
import br.com.g4f.carrinho.entity.OrderMain;
import br.com.g4f.carrinho.entity.ProductInOrder;
import br.com.g4f.carrinho.entity.User;
import br.com.g4f.carrinho.enums.ResultEnum;
import br.com.g4f.carrinho.exception.ValidacaoCustomizada;
import br.com.g4f.carrinho.repository.CarrinhoRepository;
import br.com.g4f.carrinho.repository.PedidoRepository;
import br.com.g4f.carrinho.repository.ProdutoPedidoRepository;
import br.com.g4f.carrinho.repository.UsuarioRepository;
import br.com.g4f.carrinho.service.CarrinhoService;
import br.com.g4f.carrinho.service.ProdutoService;
import br.com.g4f.carrinho.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
public class CarrinhoServiceImpl implements CarrinhoService {
    @Autowired
    ProdutoService produtoService;
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ProdutoPedidoRepository produtoPedidoRepository;
    @Autowired
    CarrinhoRepository carrinhoRepository;
    @Autowired
    UsuarioService usuarioService;

    @Override
    public Cart getCart(User user) {
        return user.getCart();
    }

    @Override
    @Transactional
    public void mergeLocalCart(Collection<ProductInOrder> productInOrders, User user) {
        Cart finalCart = user.getCart();
        productInOrders.forEach(productInOrder -> {
            Set<ProductInOrder> set = finalCart.getProducts();
            Optional<ProductInOrder> old = set.stream().filter(e -> e.getProductId().equals(productInOrder.getProductId())).findFirst();
            ProductInOrder prod;
            if (old.isPresent()) {
                prod = old.get();
                prod.setCount(productInOrder.getCount() + prod.getCount());
            } else {
                prod = productInOrder;
                prod.setCart(finalCart);
                finalCart.getProducts().add(prod);
            }
            produtoPedidoRepository.save(prod);
        });
        carrinhoRepository.save(finalCart);

    }

    @Override
    @Transactional
    public void delete(String itemId, User user) {
        if(itemId.equals("") || user == null) {
            throw new ValidacaoCustomizada(ResultEnum.ORDER_STATUS_ERROR);
        }

        var op = user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        op.ifPresent(productInOrder -> {
            productInOrder.setCart(null);
            produtoPedidoRepository.deleteById(productInOrder.getId());
        });
    }

    @Override
    @Transactional
    public void checkout(User user) {
        OrderMain order = new OrderMain(user);
        pedidoRepository.save(order);

        user.getCart().getProducts().forEach(productInOrder -> {
            productInOrder.setCart(null);
            productInOrder.setOrderMain(order);
            produtoService.decreaseStock(productInOrder.getProductId(), productInOrder.getCount());
            produtoPedidoRepository.save(productInOrder);
        });

    }
}
