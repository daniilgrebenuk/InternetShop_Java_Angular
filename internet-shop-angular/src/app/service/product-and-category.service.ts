import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {Product} from "../model/product";
import {Category} from "../model/category";

@Injectable({
  providedIn: 'root'
})

export class ProductAndCategoryService {
  private productApiUrl = environment.productApiUrl;
  private categoryApiUrl = environment.categoryApiUrl;

  constructor(private http: HttpClient) {
  }

  getProductById(id: string): Observable<Product> {
    return this.http.get<Product>(this.productApiUrl + '/' + id);
  }

  getProductsByOrderId(id: string): Observable<Product[]> {
    return this.http.get<Product[]>(this.productApiUrl + '/order/' + id);
  }

  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.productApiUrl + '/all');
  }

  getAllProductByNameContains(name: string): Observable<Product[]> {
    return this.http.get<Product[]>(this.productApiUrl + '/search/' + name);
  }

  getAllProductsByCategoryId(id: string): Observable<Product[]> {
    return this.http.get<Product[]>(this.productApiUrl + '/category/' + id);
  }

  getCategoryById(id: string): Observable<Category> {
    return this.http.get<Category>(this.categoryApiUrl + '/' + id);
  }

  getAllCategoryForUser(): Observable<Category[]> {
    return this.http.get<Category[]>(this.categoryApiUrl + '/all');
  }

  getAllCategoryForPersonal(): Observable<Category[]> {
    return this.http.get<Category[]>(this.categoryApiUrl + '/updatable/all');
  }

  addNewProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.productApiUrl + '/updatable/add', product);
  }

  addNewCategory(category: Category): Observable<Category> {
    return this.http.post<Category>(this.categoryApiUrl + '/updatable/add', category);
  }
}
