import { Routes } from '@angular/router';

import { LayoutComponent } from './admin/layout/layout';
import { DashboardComponent } from './admin/dashboard/dashboard';
import { BooksComponent } from './admin/books/books';
import { MembersComponent } from './admin/members/members';
import { IssuedComponent } from './admin/issued/issued';
import { FinesComponent } from './admin/fines/fines';
import { LoginComponent } from './auth/login/login';
import { SignupComponent } from './auth/signup/signup';
import { adminGuard } from './core/guards/admin-guard';
import { staffGuard } from './core/guards/staff-guard';

import { MemberLayoutComponent } from './member/layout/layout';
import { MemberDashboardComponent } from './member/dashboard/dashboard';
import { BrowseBooksComponent } from './member/browse-books/browse-books';
import { PayFineComponent } from './member/pay-fine/pay-fine';
import { ProfileComponent } from './member/profile/profile';
import { memberGuard } from './core/guards/member-guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },

  {
    path: 'admin',
    component: LayoutComponent,
    // shared staff area: ADMIN and LIBRARIAN - dashboard stats are ADMIN-only (see child guard)
    canActivate: [staffGuard],
    children: [
      { path: '', redirectTo: 'books', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent, canActivate: [adminGuard] },
      { path: 'books', component: BooksComponent },
      { path: 'members', component: MembersComponent },
      { path: 'issued', component: IssuedComponent },
      { path: 'fines', component: FinesComponent }
    ]
  },
  {
    path: 'member',
    component: MemberLayoutComponent,
    canActivate: [memberGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: MemberDashboardComponent },
      { path: 'books', component: BrowseBooksComponent },
      { path: 'pay-fine', component: PayFineComponent },
      { path: 'profile', component: ProfileComponent }
    ]
  },

  { path: '', redirectTo: '/login', pathMatch: 'full' }
];