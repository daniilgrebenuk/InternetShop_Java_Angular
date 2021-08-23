import {Component, OnInit} from '@angular/core';
import {Product} from "../model/product";
import {ProductAndCategoryService} from "../service/product-and-category.service";
import {UserService} from "../service/user.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {map} from "rxjs/operators";
import {NgForm} from "@angular/forms";
import {environment} from "../../environments/environment";


@Component({
  selector: 'app-product-page',
  templateUrl: './product-page.component.html',
  styleUrls: ['./product-page.component.css']
})
export class ProductPageComponent implements OnInit {
  public products: Product[];
  public role: String;
  public id: string;

  constructor(
    private productService: ProductAndCategoryService,
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.initRole();
    this.initCategoryIdAndProducts();
  }


  private initCategoryIdAndProducts() {
    this.activatedRoute.params.subscribe(
      par => {

        this.id = par['id'];
        const search = par['name'];
        this.initProduct(this.id, search);
      },
      error => {

      }
    )
  }

  private initProduct(id: string, name: string) {
    if (id != undefined) {
      this.initAllProductsByCategoryId(id);
    } else if(name != undefined) {
      this.initAllProductsBySearch(name);
    }else{
      this.initAllProducts();
    }
  }

  private initAllProducts() {
    this.productService.getAllProducts().subscribe(
      data => {
        this.products = data;
      },
      error => {

      }
    );
  }

  private initAllProductsByCategoryId(id: string) {
    this.productService.getAllProductsByCategoryId(id).subscribe(
      data => {
        this.products = data;
      },
      error => {

      }
    );
  }

  private initRole() {
    this.userService.getCurrentUser().subscribe(
      data => {
        this.role = data.role;
      },
      error => {

      }
    );
  }

  addProductToBasket(product: Product){
    let products: Product[] = JSON.parse(localStorage.getItem(environment.basketKey));
    if (products == null){
      products = [];
    }
    products.push(product);
    localStorage.setItem(environment.basketKey, JSON.stringify(products));
  }

  addNewProduct(newForm: NgForm) {
    let temp = newForm.value;
    this.productService.getCategoryById(temp.categoryId).subscribe(
      data => {
        this.addNewProductWithCategory(new Product(data, temp.name, temp.price, temp.imageUrl, temp.isAvailable));
      },
      error => {
        console.error(error.message);
      }
    )
  }

  private addNewProductWithCategory(product: Product) {
    console.log(product)
    this.productService.addNewProduct(product).subscribe(
      data => {
        console.log(data);
        location.reload();
      },
      error => {
        console.error(error.message);
      }
    );
  }

  openProduct(product: Product) {
    this.router.navigate(['/product/' + product.id]).then(r  => {});
  }

  private initAllProductsBySearch(name: string) {
    this.productService.getAllProductByNameContains(name).subscribe(
      data => {
        this.products = data;
      },
      error => {

      }
    );
  }

}
