import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BookService } from '../../core/services/book';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-books',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './books.html',
  styleUrl: './books.css'
})
export class BooksComponent implements OnInit {

  books: any[] = [];
  searchText = '';
  errorMessage = '';

  showModal = false;
  isEditMode = false;
  selectedBookId: number | null = null;
  searchTimeout: any;

  bookForm = {
    isbn: '',
    title: '',
    author: '',
    genre: '',
    totalCopies: null
  };

  constructor(private bookService: BookService, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.loadBooks();
  }

  loadBooks() {
    this.bookService.getAllBooks().subscribe({
      next: (res: any) => {
        // force fresh reference
        this.books = [...res];

        // force UI refresh
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load books';
      }
    });
  }

  searchBooks(force = false) {
    clearTimeout(this.searchTimeout);

    const text = this.searchText.trim();

    if (!text) {
      this.loadBooks();
      return;
    }

    // if Enter key pressed, call immediately
    if (force) {
      this.runSearch(text);
      return;
    }

    // wait until user stops typing
    this.searchTimeout = setTimeout(() => {
      if (text.length >= 2) {
        this.runSearch(text);
      }
    }, 400);
  }

  private runSearch(text: string) {
    this.bookService.searchBooks(text).subscribe({
      next: (res: any) => {
        this.books = [...res];
      },
      error: () => {
        this.errorMessage = 'Search failed';
      }
    });
  }

  openAddModal() {
    this.isEditMode = false;
    this.showModal = true;
    this.errorMessage = '';

    this.bookForm = {
      isbn: '',
      title: '',
      author: '',
      genre: '',
      totalCopies: null
    };
  }

  openEditModal(book: any) {
    this.isEditMode = true;
    this.showModal = true;
    this.errorMessage = '';
    this.selectedBookId = book.bookId;
    this.bookForm = { ...book };
  }

  saveBook() {
    const request = this.isEditMode && this.selectedBookId
      ? this.bookService.updateBook(this.selectedBookId, this.bookForm)
      : this.bookService.addBook(this.bookForm);

    request.subscribe({
      next: () => {
        this.closeModal();
        this.loadBooks();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Failed to save book';
      }
    });
  }

  deleteBook(id: number) {
    if (!confirm('Delete this book?')) return;

    this.bookService.deleteBook(id).subscribe({
      next: () => {
        this.loadBooks();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Failed to delete book';
      }
    });
  }

  closeModal() {
    this.showModal = false;
  }
}