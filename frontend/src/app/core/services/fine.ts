import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FineService {

  private baseUrl = `${environment.apiUrl}/fines`;

  constructor(private http: HttpClient) {}

  getMyPendingFines(): Observable<any> {
    return this.http.get(`${this.baseUrl}/my`);
  }

  getPendingFines(): Observable<any> {
    return this.http.get(`${this.baseUrl}/pending`);
  }

  getFineHistory(): Observable<any> {
    return this.http.get(`${this.baseUrl}/history`);
  }

  clearFine(fineId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/pay`, { fineId });
  }

  deleteFine(fineId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${fineId}`);
  }
}