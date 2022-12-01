import {AfterContentChecked, Component, OnInit} from '@angular/core';
import {ProductInfo} from "../../models/informacaoProduto";
import {ProdutoService} from "../../services/produto.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-editar-produto',
    templateUrl: './editar-produto.component.html',
    styleUrls: ['./editar-produto.component.css']
})
export class EditarProdutoComponent implements OnInit, AfterContentChecked {

    produto = new ProductInfo();

    constructor(private produtoService: ProdutoService,
                private route: ActivatedRoute,
                private router: Router) {
    }

    productId: string;
    isEdit = false;

    ngOnInit() {
        this.productId = this.route.snapshot.paramMap.get('id');
        if (this.productId) {
            this.isEdit = true;
            this.produtoService.getDetail(this.productId).subscribe(prod => this.produto = prod);
        }

    }

    update() {
        this.produtoService.update(this.produto).subscribe(prod => {
                if (!prod) throw new Error();
                this.router.navigate(['/seller']);
            },
            err => {
            });

    }

    onSubmit() {
        if (this.productId) {
            this.update();
        } else {
            this.add();
        }
    }

    add() {
        this.produtoService.create(this.produto).subscribe(prod => {
                if (!prod) throw new Error;
                this.router.navigate(['/']);
            },
            e => {
            });
    }

    ngAfterContentChecked(): void {
        console.log(this.produto);
    }
}
