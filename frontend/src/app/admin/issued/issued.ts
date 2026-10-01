import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CirculationService } from '../../core/services/circulation';

@Component({
  selector: 'app-issued',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './issued.html',
  styleUrl: './issued.css'
})
export class IssuedComponent implements OnInit {

  issuedBooks: any[] = [];
  errorMessage = '';

  constructor(
    private circulationService: CirculationService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadIssuedBooks();
  }

  loadIssuedBooks() {
    this.circulationService.getIssuedBooks().subscribe({
      next: (res: any) => {
        this.issuedBooks = [...res];
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load issued books';
      }
    });
  }
}