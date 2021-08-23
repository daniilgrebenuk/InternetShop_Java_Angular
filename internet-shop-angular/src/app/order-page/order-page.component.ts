import {Component, OnInit} from '@angular/core';
import {NgForm} from "@angular/forms";
import {OrderService} from "../service/order.service";
import {Order} from "../model/order";
import {environment} from "../../environments/environment";
import {Product} from "../model/product";

@Component({
  selector: 'app-order-page',
  templateUrl: './order-page.component.html',
  styleUrls: ['./order-page.component.css']
})
export class OrderPageComponent implements OnInit {

  order: Order;
  orderId: string;
  isSuccessful: boolean;
  products: Product[];

  constructor(private orderService: OrderService) {
  }

  ngOnInit(): void {
    this.initCurrentOrder();
    this.products = JSON.parse(localStorage.getItem(environment.basketKey));
  }

  private initCurrentOrder() {
    this.orderService.getCurrentOrder().subscribe(
      data=>{
        this.order = data;
      },
      error => {
        console.error(error.message);
      }
    );
  }

  onSubmit(newForm: NgForm) {
    this.order = newForm.value;
    this.order.products = this.products;

    this.orderService.addOrder(this.order).subscribe(
      data=>{
        this.orderId = data.orderId;
        this.isSuccessful = true;
        localStorage.removeItem(environment.basketKey);
      },
      error => {
        console.error(error.message);
        this.isSuccessful = false;
      }
    );
  }
}
