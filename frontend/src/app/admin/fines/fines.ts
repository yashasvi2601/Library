import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FineService } from '../../core/services/fine';

@Component({
  selector: 'app-fines',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './fines.html',
  styleUrl: './fines.css'
})
export class FinesComponent implements OnInit {

  activeTab: 'pending' | 'history' = 'pending';

  pendingFines: any[] = [];
  historyFines: any[] = [];
  errorMessage = '';

  constructor(
    private fineService: FineService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadPendingFines();
    this.loadFineHistory();
  }

  loadPendingFines() {
    this.fineService.getPendingFines().subscribe({
      next: (res: any) => {
        this.pendingFines = [...res];
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load pending fines';
      }
    });
  }

  loadFineHistory() {
    this.fineService.getFineHistory().subscribe({
      next: (res: any) => {
        this.historyFines = [...res];
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load fine history';
      }
    });
  }

  switchTab(tab: 'pending' | 'history') {
    this.activeTab = tab;
  }

  clearFine(fineId: number) {
    this.fineService.clearFine(fineId).subscribe({
      next: () => {
        this.loadPendingFines();
        this.loadFineHistory();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Failed to clear fine';
      }
    });
  }

  deleteFine(fineId: number) {
    if (!confirm('Delete this fine permanently?')) return;

    this.fineService.deleteFine(fineId).subscribe({
      next: () => {
        this.loadPendingFines();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Failed to delete fine';
      }
    });
  }
}