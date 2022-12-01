package br.com.g4f.carrinho.service.impl;


import br.com.g4f.carrinho.entity.ProductInfo;
import br.com.g4f.carrinho.enums.StatusProdutoEnum;
import br.com.g4f.carrinho.enums.ResultEnum;
import br.com.g4f.carrinho.exception.ValidacaoCustomizada;
import br.com.g4f.carrinho.repository.InformacaoProdutoRepository;
import br.com.g4f.carrinho.service.CategoriaService;
import br.com.g4f.carrinho.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    InformacaoProdutoRepository informacaoProdutoRepository;

    @Autowired
    CategoriaService categoriaService;

    @Override
    public ProductInfo findOne(String productId) {

        ProductInfo productInfo = informacaoProdutoRepository.findByProductId(productId);
        return productInfo;
    }

    @Override
    public Page<ProductInfo> findUpAll(Pageable pageable) {
        return informacaoProdutoRepository.findAllByProductStatusOrderByProductIdAsc(StatusProdutoEnum.UP.getCode(),pageable);
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return informacaoProdutoRepository.findAllByOrderByProductId(pageable);
    }

    @Override
    public Page<ProductInfo> findAllInCategory(Integer categoryType, Pageable pageable) {
        return informacaoProdutoRepository.findAllByCategoryTypeOrderByProductIdAsc(categoryType, pageable);
    }

    @Override
    @Transactional
    public void increaseStock(String productId, int amount) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new ValidacaoCustomizada(ResultEnum.PRODUCT_NOT_EXIST);

        int update = productInfo.getProductStock() + amount;
        productInfo.setProductStock(update);
        informacaoProdutoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public void decreaseStock(String productId, int amount) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new ValidacaoCustomizada(ResultEnum.PRODUCT_NOT_EXIST);

        int update = productInfo.getProductStock() - amount;
        if(update <= 0) throw new ValidacaoCustomizada(ResultEnum.PRODUCT_NOT_ENOUGH );

        productInfo.setProductStock(update);
        informacaoProdutoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public ProductInfo offSale(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new ValidacaoCustomizada(ResultEnum.PRODUCT_NOT_EXIST);

        if (productInfo.getProductStatus() == StatusProdutoEnum.DOWN.getCode()) {
            throw new ValidacaoCustomizada(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //更新
        productInfo.setProductStatus(StatusProdutoEnum.DOWN.getCode());
        return informacaoProdutoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public ProductInfo onSale(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new ValidacaoCustomizada(ResultEnum.PRODUCT_NOT_EXIST);

        if (productInfo.getProductStatus() == StatusProdutoEnum.UP.getCode()) {
            throw new ValidacaoCustomizada(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //更新
        productInfo.setProductStatus(StatusProdutoEnum.UP.getCode());
        return informacaoProdutoRepository.save(productInfo);
    }

    @Override
    public ProductInfo update(ProductInfo productInfo) {

        // if null throw exception
        categoriaService.findByCategoryType(productInfo.getCategoryType());
        if(productInfo.getProductStatus() > 1) {
            throw new ValidacaoCustomizada(ResultEnum.PRODUCT_STATUS_ERROR);
        }


        return informacaoProdutoRepository.save(productInfo);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return update(productInfo);
    }

    @Override
    public void delete(String productId) {
        ProductInfo productInfo = findOne(productId);
        if (productInfo == null) throw new ValidacaoCustomizada(ResultEnum.PRODUCT_NOT_EXIST);
        informacaoProdutoRepository.delete(productInfo);

    }


}
