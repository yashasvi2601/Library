import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinesComponent } from './fines';

describe('Fine', () => {
  let component: FinesComponent;
  let fixture: ComponentFixture<FinesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinesComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FinesComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
