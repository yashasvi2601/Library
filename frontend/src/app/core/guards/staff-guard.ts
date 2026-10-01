import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth';

// Admin dashboard/management area shared by ADMIN and LIBRARIAN staff.
export const staffGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  const role = auth.getRole();
  if (role === 'ADMIN' || role === 'LIBRARIAN') {
    return true;
  }

  router.navigate(['/login']);
  return false;
};
