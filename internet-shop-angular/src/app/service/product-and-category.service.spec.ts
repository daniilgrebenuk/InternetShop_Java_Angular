import { TestBed } from '@angular/core/testing';

import { ProductAndCategoryService } from './product-and-category.service';

describe('ProductAndCategoryService', () => {
  let service: ProductAndCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductAndCategoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
