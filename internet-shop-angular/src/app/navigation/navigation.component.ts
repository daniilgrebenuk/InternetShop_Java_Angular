import {Component, OnInit} from '@angular/core';
import {UserService} from "../service/user.service";
import {TokenStorageService} from "../service/token-storage.service";
import {Product} from "../model/product";
import {environment} from "../../environments/environment";
import {Router} from "@angular/router";
import {Order} from "../model/order";
import {OrderService} from "../service/order.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})

export class NavigationComponent implements OnInit {

  public products: Product[];
  public isLoggedIn: boolean = true;
  public role: string;
  public username: string;
  public orders: Order[];

  constructor(
    private userService: UserService,
    private tokenStorageService: TokenStorageService,
    private rout: Router,
    private orderService: OrderService
  ) {
  }

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe(
      data => {
        this.role = data.role;
        this.username = data.username;
        this.isLoggedIn = true;
      },
      error => {
        this.isLoggedIn = false;
      }
    )
  }

  logOut(): void {
    this.tokenStorageService.signOut();
    location.reload();
  }

  onBasketClick() {
    this.products = JSON.parse(localStorage.getItem(environment.basketKey));
  }

  onClearButton(){
    localStorage.removeItem(environment.basketKey);
  }

  removeProductFromBasket(product: Product) {
    let products: Product[] = JSON.parse(localStorage.getItem(environment.basketKey));
    let index = -1;
    for (let i = 0; i < products.length; i++) {
      if (products[i].id == product.id){
        index = i;
        break;
      }
    }

    if (index > -1){
      products.splice(index, 1);
    }

    this.products = products;
    localStorage.setItem(environment.basketKey, JSON.stringify(products));
  }

  redirectToProduct(product: Product) {
    this.rout.navigate(['/product/'+product.id]).then();
  }

  onBuyClick() {
    this.rout.navigate(['/order']).then();
  }

  redirectToOrder(order: Order) {
    this.rout.navigate(['/find-order/'+order.id]).then();
  }

  initOrders() {
    this.orderService.getRecentOrders().subscribe(
      data=>{
        this.orders = data;
      },
      error => {
        console.error(error.message);
      }
    )
  }


  onSearch(form: NgForm) {
    this.rout.navigate(['/products/search/'+form.value.search]).then();
  }
}
