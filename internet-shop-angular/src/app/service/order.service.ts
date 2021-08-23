import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {Order} from "../model/order";

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private orderApiUrl = environment.orderApiUrl;

  constructor(private http: HttpClient) { }

  getCurrentOrder(): Observable<Order>{
    return this.http.get<Order>(this.orderApiUrl+'/newOrder');
  }

  addOrder(order: Order): Observable<any>{
    return this.http.post<any>(this.orderApiUrl+'/add', order);
  }

  getRecentOrders(): Observable<Order[]>{
    return this.http.get<Order[]>(this.orderApiUrl+'/recent');
  }
}
