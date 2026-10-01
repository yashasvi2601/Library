import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private baseUrl = `${environment.apiUrl}/books`;

  constructor(private http: HttpClient) {}

  getAllBooks() {
    return this.http.get(this.baseUrl);
  }

  addBook(book: any) {
    return this.http.post(`${this.baseUrl}/add`, book);
  }

  deleteBook(id: number) {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  searchBooks(title: string) {
    return this.http.get(`${this.baseUrl}/search?title=${title}`);
  }

  updateBook(id: number, book: any) {
    return this.http.put(`${this.baseUrl}/${id}`, book);
  }
}