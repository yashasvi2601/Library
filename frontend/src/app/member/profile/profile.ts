import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MemberService } from '../../core/services/member';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class ProfileComponent implements OnInit {

  profile: any = null;
  errorMessage = '';

  constructor(private memberService: MemberService) {}

  ngOnInit() {
    this.memberService.getMyProfile().subscribe({
      next: (res: any) => {
        this.profile = res;
      },
      error: () => {
        this.errorMessage = 'Failed to load profile';
      }
    });
  }
}
