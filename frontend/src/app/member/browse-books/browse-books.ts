import { Component, OnInit } from '@angular/core';
import { BookService } from '../../core/services/book';
import { CirculationService } from '../../core/services/circulation';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-browse-books',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './browse-books.html',
  styleUrl: './browse-books.css'
})
export class BrowseBooksComponent implements OnInit {

  books: any[] = [];
  errorMessage = '';
  successMessage = '';
  memberId = Number(localStorage.getItem('memberId'));

  constructor(
    private bookService: BookService,
    private circulationService: CirculationService
  ) {}

  ngOnInit() {
    this.loadBooks();
  }

  loadBooks() {
    this.bookService.getAllBooks().subscribe({
      next: (res: any) => {
        this.books = res;
      },
      error: () => {
        this.errorMessage = 'Failed to load books';
      }
    });
  }

  borrowBook(bookId: number) {
    this.errorMessage = '';
    this.successMessage = '';

    this.circulationService.checkout(this.memberId, bookId).subscribe({
      next: () => {
        this.successMessage = 'Book checked out successfully';
        this.loadBooks();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Failed to check out book';
      }
    });
  }
}