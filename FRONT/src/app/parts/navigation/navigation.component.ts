import {Component, OnDestroy, OnInit} from '@angular/core';
import {UsuarioService} from "../../services/usuario.service";
import {Subscription} from "rxjs";
import {JwtResponse} from "../../response/JwtResponse";
import {Router} from "@angular/router";
import {Role} from "../../enum/Role";

@Component({
    selector: 'app-navigation',
    templateUrl: './navigation.component.html',
    styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit, OnDestroy {


    currentUserSubscription: Subscription;
    name$;
    name: string;
    currentUser: JwtResponse;
    root = '/';
    Role = Role;

    constructor(private UsuarioService: UsuarioService,
                private router: Router,
    ) {

    }


    ngOnInit() {
        this.name$ = this.UsuarioService.name$.subscribe(aName => this.name = aName);
        this.currentUserSubscription = this.UsuarioService.currentUser.subscribe(user => {
            this.currentUser = user;
            if (!user || user.role == Role.Customer) {
                this.root = '/';
            } else {
                this.root = '/seller';
            }
        });
    }

    ngOnDestroy(): void {
        this.currentUserSubscription.unsubscribe();
        // this.name$.unsubscribe();
    }

    logout() {
        this.UsuarioService.logout();
        // this.router.navigate(['/login'], {queryParams: {logout: 'true'}} );
    }

}
