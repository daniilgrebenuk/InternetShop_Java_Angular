import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Product} from "../model/product";
import {ProductAndCategoryService} from "../service/product-and-category.service";

@Component({
  selector: 'app-product-profile',
  templateUrl: './product-profile.component.html',
  styleUrls: ['./product-profile.component.css']
})
export class ProductProfileComponent implements OnInit {

  product: Product;
  id: string;

  constructor(private rout: ActivatedRoute, private productService: ProductAndCategoryService) { }

  ngOnInit(): void {
    this.initIdAndProduct();

  }

  private initIdAndProduct() {
    this.rout.params.subscribe(
      par => {
        this.id = par['id'];
        this.initProduct();
      },
      error => {

      }
    )
  }

  private initProduct() {
    this.productService.getProductById(this.id).subscribe(
      data=>{
        this.product = data;
      },
      error => {
        console.error(error.message);
      }
    )
  }
}
