import { TestBed } from '@angular/core/testing';

import { Fine } from './fine';

describe('Fine', () => {
  let service: Fine;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Fine);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
