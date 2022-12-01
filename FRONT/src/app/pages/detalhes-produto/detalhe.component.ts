import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { ProductInfo } from "src/app/models/informacaoProduto";
import { ProdutoCompra } from "src/app/models/produtoCompra";
import { CarrinhoService } from "src/app/services/carrinho.service";
import { ProdutoService } from "src/app/services/produto.service";

@Component({
  selector: 'app-detalhe',
  templateUrl: './detalhe.component.html',
  styleUrls: ['./detalhe.component.css']
})
export class DetalheComponent implements OnInit {
  title: string;
  count: number;
  informacoesProduto: ProductInfo;

  constructor(
      private produtoService: ProdutoService,
      private carrinhoService: CarrinhoService,
      private route: ActivatedRoute,
      private router: Router
  ) {}

  ngOnInit() {
    this.buscarProduto();
    this.count = 1;
  }

  buscarProduto(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.produtoService.getDetail(id).subscribe(
        prod => {
          this.informacoesProduto = prod;
        },
        _ => console.log('Falha ao adicioanr o produto ao carrinho')
    );
  }

  adicionarCarrinho() {
    this.carrinhoService
        .adicionarItem(new ProdutoCompra(this.informacoesProduto, this.count))
        .subscribe(
            res => {
              if (!res) {
                throw new Error();
              }
              this.router.navigateByUrl('/carrinho');
            },
            _ => console.log('Falha ao adicioanr o produto ao carrinho')
        );
  }

  validarEstoque() {
    const max = this.informacoesProduto.productStock;
    if (this.count > max) {
      this.count = max;
    } else if (this.count < 1) {
      this.count = 1;
    }
  }


}