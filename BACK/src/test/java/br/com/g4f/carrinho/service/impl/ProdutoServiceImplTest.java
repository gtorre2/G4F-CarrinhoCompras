package br.com.g4f.carrinho.service.impl;

import br.com.g4f.carrinho.entity.ProductInfo;
import br.com.g4f.carrinho.enums.StatusProdutoEnum;
import br.com.g4f.carrinho.exception.ValidacaoCustomizada;
import br.com.g4f.carrinho.repository.InformacaoProdutoRepository;
import br.com.g4f.carrinho.service.CategoriaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ProdutoServiceImplTest {

    @InjectMocks
    private ProdutoServiceImpl productService;

    @Mock
    private InformacaoProdutoRepository informacaoProdutoRepository;

    @Mock
    private CategoriaService categoriaService;

    private ProductInfo productInfo;

    @Before
    public void setUp() {
        productInfo = new ProductInfo();
        productInfo.setProductId("1");
        productInfo.setProductStock(10);
        productInfo.setProductStatus(1);
    }

    @Test
    public void increaseStockTest() {
        when(informacaoProdutoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        productService.increaseStock("1", 10);

        Mockito.verify(informacaoProdutoRepository, Mockito.times(1)).save(productInfo);
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void increaseStockExceptionTest() {
        productService.increaseStock("1", 10);
    }

    @Test
    public void decreaseStockTest() {
        when(informacaoProdutoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        productService.decreaseStock("1", 9);

        Mockito.verify(informacaoProdutoRepository, Mockito.times(1)).save(productInfo);
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void decreaseStockValueLesserEqualTest() {
        when(informacaoProdutoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        productService.decreaseStock("1", 10);
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void decreaseStockExceptionTest() {
        productService.decreaseStock("1", 10);
    }

    @Test
    public void offSaleTest() {
        productInfo.setProductStatus(StatusProdutoEnum.UP.getCode());

        when(informacaoProdutoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        productService.offSale("1");

        Mockito.verify(informacaoProdutoRepository, Mockito.times(1)).save(productInfo);
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void offSaleStatusDownTest() {
        productInfo.setProductStatus(StatusProdutoEnum.DOWN.getCode());

        when(informacaoProdutoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        productService.offSale("1");
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void offSaleProductNullTest() {
        when(informacaoProdutoRepository.findByProductId(productInfo.getProductId())).thenReturn(null);

        productService.offSale("1");
    }

    @Test
    public void onSaleTest() {
        productInfo.setProductStatus(StatusProdutoEnum.DOWN.getCode());

        when(informacaoProdutoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        productService.onSale("1");

        Mockito.verify(informacaoProdutoRepository, Mockito.times(1)).save(productInfo);
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void onSaleStatusUpTest() {
        productInfo.setProductStatus(StatusProdutoEnum.UP.getCode());

        when(informacaoProdutoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        productService.onSale("1");
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void onSaleProductNullTest() {
        when(informacaoProdutoRepository.findByProductId(productInfo.getProductId())).thenReturn(null);

        productService.offSale("1");
    }

    @Test
    public void updateTest() {
        productService.update(productInfo);

        Mockito.verify(informacaoProdutoRepository, Mockito.times(1)).save(productInfo);
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void updateProductStatusBiggerThenOneTest() {
        productInfo.setProductStatus(2);

        productService.update(productInfo);
    }

    @Test
    public void deleteTest() {
        when(informacaoProdutoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        productService.delete("1");

        Mockito.verify(informacaoProdutoRepository, Mockito.times(1)).delete(productInfo);
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void deleteProductNullTest() {
        productService.delete("1");
    }
}
