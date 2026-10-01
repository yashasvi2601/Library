import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MemberService } from '../../core/services/member';

@Component({
  selector: 'app-members',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './members.html',
  styleUrl: './members.css'
})
export class MembersComponent implements OnInit {

  members: any[] = [];
  errorMessage = '';

  showModal = false;
  selectedMemberId: number | null = null;

  memberForm = {
    name: '',
    email: '',
    maxBooksAllowed: 5
  };

  constructor(
    private memberService: MemberService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadMembers();
  }

  loadMembers() {
    this.memberService.getAllMembers().subscribe({
      next: (res: any) => {
        this.members = [...res];
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load members';
      }
    });
  }

  openEditModal(member: any) {
    this.selectedMemberId = member.memberId;
    this.errorMessage = '';
    this.memberForm = {
      name: member.name,
      email: member.email,
      maxBooksAllowed: member.maxBooksAllowed
    };
    this.showModal = true;
  }

  updateMember() {
    if (!this.selectedMemberId) return;

    this.memberService
      .updateMember(this.selectedMemberId, this.memberForm)
      .subscribe({
        next: () => {
          this.closeModal();
          this.loadMembers();
        },
        error: (err) => {
          this.errorMessage = err?.error?.message || 'Failed to update member';
        }
      });
  }

  deleteMember(id: number) {
    if (!confirm('Delete this member?')) return;

    this.memberService.deleteMember(id).subscribe({
      next: () => {
        this.loadMembers();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Failed to delete member';
      }
    });
  }

  closeModal() {
    this.showModal = false;
  }
}