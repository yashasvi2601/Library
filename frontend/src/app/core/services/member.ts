import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MemberService {

  private baseUrl = `${environment.apiUrl}/members`;

  constructor(private http: HttpClient) {}

  getMyProfile(): Observable<any> {
    return this.http.get(`${this.baseUrl}/me`);
  }

  getAllMembers(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

  getMemberById(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createMember(member: any): Observable<any> {
    return this.http.post(this.baseUrl, member);
  }

  updateMember(id: number, member: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, member);
  }

  deleteMember(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}