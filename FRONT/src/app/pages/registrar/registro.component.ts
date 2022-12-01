import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Usuario } from "../../models/usuario";
import { UsuarioService } from "../../services/usuario.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent implements OnInit {

  usuario: Usuario;

  constructor(private location: Location,
    private usuarioService: UsuarioService,
    private router: Router) {
    this.usuario = new Usuario();

  }

  ngOnInit() {
  }

  onSubmit() {
    this.usuarioService.registrar(this.usuario).subscribe(u => {
      this.router.navigate(['/login']);
    },
      e => { });
  }

}
