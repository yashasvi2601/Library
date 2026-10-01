import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FineService } from '../../core/services/fine';

@Component({
  selector: 'app-pay-fine',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pay-fine.html',
  styleUrl: './pay-fine.css'
})
export class PayFineComponent implements OnInit {

  fines: any[] = [];
  errorMessage = '';
  successMessage = '';

  constructor(private fineService: FineService) {}

  ngOnInit() {
    this.loadFines();
  }

  loadFines() {
    this.fineService.getMyPendingFines().subscribe({
      next: (res: any) => {
        this.fines = [...res];
      },
      error: () => {
        this.errorMessage = 'Failed to load your fines';
      }
    });
  }

  payFine(fineId: number) {
    this.errorMessage = '';
    this.successMessage = '';

    this.fineService.clearFine(fineId).subscribe({
      next: () => {
        this.successMessage = 'Fine paid successfully';
        this.loadFines();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Failed to pay fine';
      }
    });
  }
}
