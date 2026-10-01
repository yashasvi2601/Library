import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AdminService } from '../../core/services/admin';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {

  stats: any = {
    totalBooks: 0,
    totalMembers: 0,
    issuedBooks: 0,
    totalFines: 0
  };

  errorMessage = '';

  constructor(
    private adminService: AdminService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadStats();
  }

  loadStats() {
    this.adminService.getDashboardStats().subscribe({
      next: (res: any) => {
        // fresh object reference
        this.stats = { ...res };

        // force UI repaint
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load dashboard stats';
      }
    });
  }
}