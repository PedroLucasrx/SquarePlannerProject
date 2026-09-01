import { Component } from '@angular/core';
import { RouterLink, Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

    constructor(
        public authService: AuthService,
        private router: Router
    ) {}

    sair(): void {

        this.authService.logout();

        this.router.navigate(['/login']);

    }

}