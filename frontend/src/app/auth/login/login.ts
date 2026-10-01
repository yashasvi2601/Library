import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterModule,CommonModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  loginData = {
    email: '',
    password: ''
  };

  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onLogin(loginForm: NgForm) {
    this.errorMessage = '';
    this.loginData.email = this.loginData.email.toLowerCase().trim();

    if (
      !this.loginData.email?.trim() ||
      !this.loginData.password?.trim()
    ) {
      this.errorMessage = 'Email and password are required';
      loginForm.resetForm();
      return;
    }

    this.authService.login(this.loginData).subscribe({
      next: (res) => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('role', res.role);
        localStorage.setItem('email', res.email);
        if (res.role === 'MEMBER' && res.memberId) {
          localStorage.setItem('memberId', String(res.memberId));
        }

        if (res.role === 'ADMIN') {
          this.router.navigate(['/admin/dashboard']);
        } else if (res.role === 'LIBRARIAN') {
          this.router.navigate(['/admin/books']);
        } else {
          this.router.navigate(['/member/dashboard']);
        }
      },
      error: (err) => {
        this.errorMessage =
          err?.error?.message || 'Invalid email or password';

        // guaranteed reset
        loginForm.resetForm();
      }
    });
  }
}