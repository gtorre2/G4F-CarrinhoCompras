package br.com.g4f.carrinho.service.impl;


import br.com.g4f.carrinho.entity.OrderMain;
import br.com.g4f.carrinho.entity.ProductInOrder;
import br.com.g4f.carrinho.entity.ProductInfo;
import br.com.g4f.carrinho.enums.PedidoStatusEnum;
import br.com.g4f.carrinho.enums.ResultEnum;
import br.com.g4f.carrinho.exception.ValidacaoCustomizada;
import br.com.g4f.carrinho.repository.PedidoRepository;
import br.com.g4f.carrinho.repository.ProdutoPedidoRepository;
import br.com.g4f.carrinho.repository.InformacaoProdutoRepository;
import br.com.g4f.carrinho.repository.UsuarioRepository;
import br.com.g4f.carrinho.service.PedidoService;
import br.com.g4f.carrinho.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoServiceImpl implements PedidoService {
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    InformacaoProdutoRepository informacaoProdutoRepository;
    @Autowired
    ProdutoService produtoService;
    @Autowired
    ProdutoPedidoRepository produtoPedidoRepository;

    @Override
    public Page<OrderMain> findAll(Pageable pageable) {
        return pedidoRepository.findAllByOrderByOrderStatusAscCreateTimeDesc(pageable);
    }

    @Override
    public Page<OrderMain> findByStatus(Integer status, Pageable pageable) {
        return pedidoRepository.findAllByOrderStatusOrderByCreateTimeDesc(status, pageable);
    }

    @Override
    public Page<OrderMain> findByBuyerEmail(String email, Pageable pageable) {
        return pedidoRepository.findAllByBuyerEmailOrderByOrderStatusAscCreateTimeDesc(email, pageable);
    }

    @Override
    public Page<OrderMain> findByBuyerPhone(String phone, Pageable pageable) {
        return pedidoRepository.findAllByBuyerPhoneOrderByOrderStatusAscCreateTimeDesc(phone, pageable);
    }

    @Override
    public OrderMain findOne(Long orderId) {
        OrderMain orderMain = pedidoRepository.findByOrderId(orderId);
        if(orderMain == null) {
            throw new ValidacaoCustomizada(ResultEnum.ORDER_NOT_FOUND);
        }
        return orderMain;
    }

    @Override
    @Transactional
    public OrderMain finish(Long orderId) {
        OrderMain orderMain = findOne(orderId);
        if(!orderMain.getOrderStatus().equals(PedidoStatusEnum.NEW.getCode())) {
            throw new ValidacaoCustomizada(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMain.setOrderStatus(PedidoStatusEnum.FINISHED.getCode());
        pedidoRepository.save(orderMain);
        return pedidoRepository.findByOrderId(orderId);
    }

    @Override
    @Transactional
    public OrderMain cancel(Long orderId) {
        OrderMain orderMain = findOne(orderId);
        if(!orderMain.getOrderStatus().equals(PedidoStatusEnum.NEW.getCode())) {
            throw new ValidacaoCustomizada(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMain.setOrderStatus(PedidoStatusEnum.CANCELED.getCode());
        pedidoRepository.save(orderMain);

        // Restore Stock
        Iterable<ProductInOrder> products = orderMain.getProducts();
        for(ProductInOrder productInOrder : products) {
            ProductInfo productInfo = informacaoProdutoRepository.findByProductId(productInOrder.getProductId());
            if(productInfo != null) {
                produtoService.increaseStock(productInOrder.getProductId(), productInOrder.getCount());
            }
        }
        return pedidoRepository.findByOrderId(orderId);

    }
}
