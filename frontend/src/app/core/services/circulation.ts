import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CirculationService {

  private baseUrl = `${environment.apiUrl}/circulation`;

  constructor(private http: HttpClient) {}

  getIssuedBooks() {
    return this.http.get(`${this.baseUrl}/issued`);
  }
  getMyBooks(memberId: number) {
    return this.http.get(`${this.baseUrl}/member/${memberId}`);
  }

  checkout(memberId: number, bookId: number) {
    return this.http.post(`${this.baseUrl}/checkout`, { memberId, bookId });
  }

  returnBook(txnId: number) {
    return this.http.post(`${this.baseUrl}/return`, { txnId });
  }

  renewBook(txnId: number) {
    return this.http.post(`${this.baseUrl}/renew`, { txnId });
  }
}