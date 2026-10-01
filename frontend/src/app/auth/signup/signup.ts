import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [FormsModule, RouterModule,CommonModule],
  templateUrl: './signup.html',
  styleUrl: './signup.css'
})
export class SignupComponent {
  signupData = {
    name: '',
    email: '',
    password: ''
  };

  errorMessage = '';
  successMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSignup() {
    this.errorMessage = '';
    this.successMessage = '';
    this.signupData.email = this.signupData.email.toLowerCase().trim();

    if (
      !this.signupData.name.trim() ||
      !this.signupData.email.trim() ||
      !this.signupData.password.trim()
    ) {
      this.errorMessage = 'All fields are required';
      return;
    }

    this.authService.signup(this.signupData).subscribe({
      next: () => {
        this.successMessage = 'Signup successful. Redirecting to login...';

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1000);
      },
      error: (err) => {
        this.errorMessage =
          err?.error || 'Signup failed. Email may already exist';
      }
    });
  }
}