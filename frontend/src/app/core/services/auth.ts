import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface AuthResponse {
  token: string;
  role: string;
  email: string;
  memberId?: number | null;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {}

  login(data: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, data);
  }

  signup(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/signup`, data);
  }

  getRole(): string | null {
    return localStorage.getItem('role');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): Observable<string> {
    return new Observable(observer => {
      this.http.post(
        `${this.baseUrl}/logout`,
        {},
        { responseType: 'text' }
      ).subscribe({
        next: (res) => {
          localStorage.clear();
          observer.next(res);
          observer.complete();
        },
        error: (err) => {
          localStorage.clear();
          observer.error(err);
        }
      });
    });
  }
}