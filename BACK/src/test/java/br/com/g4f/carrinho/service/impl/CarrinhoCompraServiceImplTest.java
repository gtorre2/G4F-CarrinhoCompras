package br.com.g4f.carrinho.service.impl;

import br.com.g4f.carrinho.entity.Cart;
import br.com.g4f.carrinho.entity.ProductInOrder;
import br.com.g4f.carrinho.entity.User;
import br.com.g4f.carrinho.exception.ValidacaoCustomizada;
import br.com.g4f.carrinho.repository.CarrinhoRepository;
import br.com.g4f.carrinho.repository.PedidoRepository;
import br.com.g4f.carrinho.repository.ProdutoPedidoRepository;
import br.com.g4f.carrinho.service.ProdutoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
public class CarrinhoCompraServiceImplTest {

    @InjectMocks
    private CarrinhoServiceImpl cartService;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private ProdutoPedidoRepository produtoPedidoRepository;

    @Mock
    private CarrinhoRepository carrinhoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    private User user;

    private ProductInOrder productInOrder;

    private Set<ProductInOrder> set;

    private Cart cart;

    @Before
    public void setUp() {
        user = new User();
        cart = new Cart();

        user.setEmail("email@email.com");
        user.setName("Nome");
        user.setPhone("1234567890");
        user.setAddress("Teste");

        productInOrder = new ProductInOrder();
        productInOrder.setProductId("1");
        productInOrder.setCount(10);
        productInOrder.setProductPrice(BigDecimal.valueOf(1));

        set = new HashSet<>();
        set.add(productInOrder);

        cart.setProducts(set);

        user.setCart(cart);
    }

    @Test
    public void mergeLocalCartTest() {
        cartService.mergeLocalCart(set, user);

        Mockito.verify(carrinhoRepository, Mockito.times(1)).save(cart);
        Mockito.verify(produtoPedidoRepository, Mockito.times(1)).save(productInOrder);
    }

    @Test
    public void mergeLocalCartTwoProductTest() {
        ProductInOrder productInOrder2 = new ProductInOrder();
        productInOrder2.setProductId("2");
        productInOrder2.setCount(10);

        user.getCart().getProducts().add(productInOrder2);

        cartService.mergeLocalCart(set, user);

        Mockito.verify(carrinhoRepository, Mockito.times(1)).save(cart);
        Mockito.verify(produtoPedidoRepository, Mockito.times(1)).save(productInOrder);
        Mockito.verify(produtoPedidoRepository, Mockito.times(1)).save(productInOrder2);
    }

    @Test
    public void mergeLocalCartNoProductTest() {
        user.getCart().setProducts(new HashSet<>());

        cartService.mergeLocalCart(set, user);

        Mockito.verify(carrinhoRepository, Mockito.times(1)).save(cart);
        Mockito.verify(produtoPedidoRepository, Mockito.times(1)).save(productInOrder);
    }

    @Test
    public void deleteTest() {
        cartService.delete("1", user);

        Mockito.verify(produtoPedidoRepository, Mockito.times(1)).deleteById(productInOrder.getId());
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void deleteNoProductTest() {
        cartService.delete("", user);
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void deleteNoUserTest() {
        cartService.delete("1", null);
    }

    @Test
    public void checkoutTest() {
        cartService.checkout(user);

        Mockito.verify(produtoPedidoRepository, Mockito.times(1)).save(productInOrder);
    }
}
