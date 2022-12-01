import {ProdutoCompra} from "./produtoCompra";

export class ProductInfo {
    productId: string;
    productName: string;
    productPrice: number;
    productStock: number;
    productDescription: string;
    productIcon: string;
    productStatus: number; // 0: onsale 1: offsale
    categoryType: number;
    createTime: string;
    updateTime: string;


    constructor(ProdutoCompra?: ProdutoCompra) {
        if (ProdutoCompra) {
            this.productId = ProdutoCompra.productId;
            this.productName = ProdutoCompra.productName;
            this.productPrice = ProdutoCompra.productPrice;
            this.productStock = ProdutoCompra.productStock;
            this.productDescription = ProdutoCompra.productDescription;
            this.productIcon = ProdutoCompra.productIcon;
            this.categoryType = ProdutoCompra.categoryType;
            this.productStatus = 0;
        } else {
            this.productId = '';
            this.productName = '';
            this.productPrice = 20;
            this.productStock = 100;
            this.productDescription = '';
            this.productIcon = '';
            this.categoryType = 0;
            this.productStatus = 0;
        }
    }
}

