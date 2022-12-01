package br.com.g4f.carrinho.service.impl;

import br.com.g4f.carrinho.entity.OrderMain;
import br.com.g4f.carrinho.entity.ProductInOrder;
import br.com.g4f.carrinho.entity.ProductInfo;
import br.com.g4f.carrinho.enums.PedidoStatusEnum;
import br.com.g4f.carrinho.exception.ValidacaoCustomizada;
import br.com.g4f.carrinho.repository.PedidoRepository;
import br.com.g4f.carrinho.repository.InformacaoProdutoRepository;
import br.com.g4f.carrinho.service.ProdutoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private InformacaoProdutoRepository informacaoProdutoRepository;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private PedidoServiceImpl orderService;

    private OrderMain orderMain;

    private ProductInfo productInfo;

    @Before
    public void setUp() {
        orderMain = new OrderMain();
        orderMain.setOrderId(1L);
        orderMain.setOrderStatus(PedidoStatusEnum.NEW.getCode());

        ProductInOrder productInOrder = new ProductInOrder();
        productInOrder.setProductId("1");
        productInOrder.setCount(10);

        Set<ProductInOrder> set = new HashSet<>();
        set.add(productInOrder);

        orderMain.setProducts(set);

        productInfo = new ProductInfo();
        productInfo.setProductStock(10);
    }

    @Test
    public void finishSuccessTest() {
        when(pedidoRepository.findByOrderId(orderMain.getOrderId())).thenReturn(orderMain);

        OrderMain orderMainReturn = orderService.finish(orderMain.getOrderId());

        assertThat(orderMainReturn.getOrderId(), is(orderMain.getOrderId()));
        assertThat(orderMainReturn.getOrderStatus(), is(PedidoStatusEnum.FINISHED.getCode()));
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void finishStatusCanceledTest() {
        orderMain.setOrderStatus(PedidoStatusEnum.CANCELED.getCode());

        when(pedidoRepository.findByOrderId(orderMain.getOrderId())).thenReturn(orderMain);

        OrderMain orderMainReturn = orderService.finish(orderMain.getOrderId());

        assertThat(orderMainReturn.getOrderId(), is(orderMain.getOrderId()));
        assertThat(orderMainReturn.getOrderStatus(), is(PedidoStatusEnum.FINISHED.getCode()));
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void finishStatusFinishedTest() {
        orderMain.setOrderStatus(PedidoStatusEnum.FINISHED.getCode());

        when(pedidoRepository.findByOrderId(orderMain.getOrderId())).thenReturn(orderMain);

        OrderMain orderMainReturn = orderService.finish(orderMain.getOrderId());

        assertThat(orderMainReturn.getOrderId(), is(orderMain.getOrderId()));
        assertThat(orderMainReturn.getOrderStatus(), is(PedidoStatusEnum.FINISHED.getCode()));
    }

    @Test
    public void cancelSuccessTest() {
        when(pedidoRepository.findByOrderId(orderMain.getOrderId())).thenReturn(orderMain);
        when(informacaoProdutoRepository.findByProductId(orderMain.getProducts().iterator().next().getProductId())).thenReturn(productInfo);

        OrderMain orderMainReturn = orderService.cancel(orderMain.getOrderId());

        assertThat(orderMainReturn.getOrderId(), is(orderMain.getOrderId()));
        assertThat(orderMainReturn.getOrderStatus(), is(PedidoStatusEnum.CANCELED.getCode()));
        assertThat(orderMainReturn.getProducts().iterator().next().getCount(), is(10));
    }

    @Test
    public void cancelNoProduct() {
        when(pedidoRepository.findByOrderId(orderMain.getOrderId())).thenReturn(orderMain);
        orderMain.setProducts(new HashSet<>());

        OrderMain orderMainReturn = orderService.cancel(orderMain.getOrderId());

        assertThat(orderMainReturn.getOrderId(), is(orderMain.getOrderId()));
        assertThat(orderMainReturn.getOrderStatus(), is(PedidoStatusEnum.CANCELED.getCode()));
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void cancelStatusCanceledTest() {
        orderMain.setOrderStatus(PedidoStatusEnum.CANCELED.getCode());

        when(pedidoRepository.findByOrderId(orderMain.getOrderId())).thenReturn(orderMain);

        orderService.cancel(orderMain.getOrderId());
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void cancelStatusFinishTest() {
        orderMain.setOrderStatus(PedidoStatusEnum.FINISHED.getCode());

        when(pedidoRepository.findByOrderId(orderMain.getOrderId())).thenReturn(orderMain);

        orderService.cancel(orderMain.getOrderId());
    }
}
