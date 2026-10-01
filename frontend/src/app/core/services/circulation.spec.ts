import { TestBed } from '@angular/core/testing';

import { Circulation } from './circulation';

describe('Circulation', () => {
  let service: Circulation;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Circulation);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
