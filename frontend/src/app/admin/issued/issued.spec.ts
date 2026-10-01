import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Issued } from './issued';

describe('Issued', () => {
  let component: Issued;
  let fixture: ComponentFixture<Issued>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Issued],
    }).compileComponents();

    fixture = TestBed.createComponent(Issued);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
