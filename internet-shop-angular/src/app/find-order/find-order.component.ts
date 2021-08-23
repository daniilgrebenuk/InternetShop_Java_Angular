import { Component, OnInit } from '@angular/core';
import {Product} from "../model/product";
import {ActivatedRoute, Router} from "@angular/router";
import {NgForm} from "@angular/forms";
import {OrderService} from "../service/order.service";
import {ProductAndCategoryService} from "../service/product-and-category.service";

@Component({
  selector: 'app-find-order',
  templateUrl: './find-order.component.html',
  styleUrls: ['./find-order.component.css']
})
export class FindOrderComponent implements OnInit {

  products: Product[];
  orderId: string;

  constructor(
    private rout: Router,
    private activatedRoute: ActivatedRoute,
    private productService: ProductAndCategoryService
  ) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(
      par => {
        this.orderId = par['id'];
        if (this.orderId != undefined) {
          this.initProducts(this.orderId);
        }
      },
      error => {

      }
    )
  }

  redirectToProduct(product: Product) {
    this.rout.navigate(['/product/'+product.id]).then();
  }

  initProducts(id: string) {
    this.productService.getProductsByOrderId(id).subscribe(
      data=>{
        console.log(data)
        this.orderId = id;
        this.products = data;
      },
      error => {
        console.error(error.message);
      }
    );
  }
}
