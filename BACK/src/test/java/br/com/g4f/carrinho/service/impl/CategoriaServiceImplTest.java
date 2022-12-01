package br.com.g4f.carrinho.service.impl;

import br.com.g4f.carrinho.entity.ProductCategory;
import br.com.g4f.carrinho.exception.ValidacaoCustomizada;
import br.com.g4f.carrinho.repository.CategoriaProdutoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CategoriaServiceImplTest {

    @InjectMocks
    private CategoriaServiceImpl categoryService;

    @Mock
    private CategoriaProdutoRepository categoriaProdutoRepository;

    @Test
    public void findByCategoryTypeTest() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(1);

        Mockito.when(categoriaProdutoRepository.findByCategoryType(productCategory.getCategoryId())).thenReturn(productCategory);

        categoryService.findByCategoryType(productCategory.getCategoryId());

        Mockito.verify(categoriaProdutoRepository, Mockito.times(1)).findByCategoryType(productCategory.getCategoryId());
    }

    @Test(expected = ValidacaoCustomizada.class)
    public void findByCategoryTypeExceptionTest() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryId(1);

        Mockito.when(categoriaProdutoRepository.findByCategoryType(productCategory.getCategoryId())).thenReturn(null);

        categoryService.findByCategoryType(productCategory.getCategoryId());
    }
}
