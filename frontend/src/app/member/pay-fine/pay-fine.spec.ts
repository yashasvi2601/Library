import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayFine } from './pay-fine';

describe('PayFine', () => {
  let component: PayFine;
  let fixture: ComponentFixture<PayFine>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PayFine],
    }).compileComponents();

    fixture = TestBed.createComponent(PayFine);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
