import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FindOrderComponent } from './find-order.component';

describe('FindOrderComponent', () => {
  let component: FindOrderComponent;
  let fixture: ComponentFixture<FindOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FindOrderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FindOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
