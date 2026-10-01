import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CirculationService } from '../../core/services/circulation';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-member-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class MemberDashboardComponent implements OnInit {
  books: any[] = [];
  errorMessage = '';

  stats = {
    totalIssued: 0,
    totalFine: 0
  };

  memberId = Number(localStorage.getItem('memberId'));

  constructor(
    private circulationService: CirculationService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadBooks();
  }

  loadBooks() {
    this.memberId = Number(localStorage.getItem('memberId'));

    if (!this.memberId) {
      this.errorMessage = 'No member profile found for this account';
      return;
    }

    this.circulationService.getMyBooks(this.memberId).subscribe({
      next: (res: any) => {
        this.books = [...res]; // fresh reference

        this.stats.totalIssued = this.books.length;
        this.stats.totalFine = this.books.reduce(
          (sum: number, book: any) => sum + (Number(book.currentFine) || 0),
          0
        );
      },
      error: () => {
        this.errorMessage = 'Failed to load your books';
      }
    });
  }

  returnBook(txnId: number) {
    this.errorMessage = '';

    this.circulationService.returnBook(txnId).subscribe({
      next: () => {
        this.loadBooks();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Failed to return book';
      }
    });
  }

  renewBook(txnId: number) {
    this.errorMessage = '';

    this.circulationService.renewBook(txnId).subscribe({
      next: () => {
        this.loadBooks();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Failed to renew book';
      }
    });
  }
}