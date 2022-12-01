import {Component, OnInit} from '@angular/core';
import {UsuarioService} from "../../services/usuario.service";
import {Usuario} from "../../models/usuario";
import {Router} from "@angular/router";
import {Role} from "../../enum/Role";

@Component({
    selector: 'app-detalhes-usuario',
    templateUrl: './detalhes-usuario.component.html',
    styleUrls: ['./detalhes-usuario.component.css']
})
export class DetalhesUsuarioComponent implements OnInit {

    constructor(private usuarioService: UsuarioService,
                private router: Router) {
    }

    usuario = new Usuario();

    ngOnInit() {
        const account = this.usuarioService.currentUserValue.account;

        this.usuarioService.get(account).subscribe( u => {
            this.usuario = u;
            this.usuario.password = '';
        }, e => {

        });
    }

    onSubmit() {
        this.usuarioService.update(this.usuario).subscribe(u => {
            this.usuarioService.nameTerms.next(u.name);
            let url = '/';
            if (this.usuario.role != Role.Customer) {
                url = '/comprador';
            }
            this.router.navigateByUrl(url);
        }, _ => {})
    }

}