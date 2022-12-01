import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {PedidoService} from "../../services/pedido.service";
import {Pedido} from "../../models/pedido";
import {ActivatedRoute} from "@angular/router";

@Component({
    selector: 'app-detalhe-pedido',
    templateUrl: './detalhe-pedido.component.html',
    styleUrls: ['./detalhe-pedido.component.css']
})
export class DetalhePedidoComponent implements OnInit {

    constructor(private pedidoService: PedidoService,
                private route: ActivatedRoute) {
    }

    pedido$: Observable<Pedido>;

    ngOnInit() {
        this.pedido$ = this.pedidoService.show(this.route.snapshot.paramMap.get('id'));
    }

}