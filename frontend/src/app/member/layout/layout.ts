import { Component } from '@angular/core';
import { Router, RouterModule, RouterOutlet} from '@angular/router';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-member-layout',
  standalone: true,
  imports: [RouterModule,RouterOutlet],
  templateUrl: './layout.html',
  styleUrl: './layout.css'
})
export class MemberLayoutComponent {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        localStorage.clear();
        this.router.navigate(['/login']);
      },
      error: () => {
        localStorage.clear();
        this.router.navigate(['/login']);
      }
    });
  }
}