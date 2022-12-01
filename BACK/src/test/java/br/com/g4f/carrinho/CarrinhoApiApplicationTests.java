package br.com.g4f.carrinho;

import br.com.g4f.carrinho.service.impl.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(value = {
        CarrinhoCompraServiceImplTest.class,
        CategoriaServiceImplTest.class,
        PedidoServiceImplTest.class,
        ProductInPedidoServiceImplTest.class,
        ProdutoServiceImplTest.class,
        UsuarioServiceImplTest.class
})
public class CarrinhoApiApplicationTests {

}

