import {Component, OnInit} from '@angular/core';
import {Category} from "../model/category";
import {UserService} from "../service/user.service";
import {ProductAndCategoryService} from "../service/product-and-category.service";
import {NgForm} from "@angular/forms";
import {Product} from "../model/product";

@Component({
  selector: 'app-category-page',
  templateUrl: './category-page.component.html',
  styleUrls: ['./category-page.component.css']
})
export class CategoryPageComponent implements OnInit {
  categories: Category[];
  role: string;

  constructor(private userService: UserService, private categoryService: ProductAndCategoryService) {
  }

  ngOnInit(): void {
    this.initRoleAndCategory();
  }

  private initRoleAndCategory(){
    this.userService.getCurrentUser().subscribe(
      data => {
        this.role = data.role;
        this.initCategory();
      },
      error => {
        this.initCategory();
      }
    );
  }

  private initCategory(){
    if (this.role == 'ADMIN'){
      this.initCategoryForPersonal();
    }else{
      this.initCategoryForUser();
    }
  }

  private initCategoryForUser() {
    this.categoryService.getAllCategoryForUser().subscribe(
      data => {
        this.categories = data;
      },
      error => {

      }
    );
  }

  private initCategoryForPersonal() {
    this.categoryService.getAllCategoryForPersonal().subscribe(
      data => {
        this.categories = data;
      },
      error => {

      }
    );
  }


  addNewProduct(newForm: NgForm) {
    this.categoryService.addNewCategory(newForm.value).subscribe(
      data=>{
        console.log(data);
        location.reload();
      },
      error => {
        console.error(error.message);
      }
    );
  }
}
