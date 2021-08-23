import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../model/user";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authUrl = environment.authApiUrl;

  constructor(private http: HttpClient) { }



  login(user: User): Observable<any>{
    return this.http.post<any>(this.authUrl+'/login', user,{observe: 'response'});
  }

  register(user: User): Observable<any>{
    return this.http.post(this.authUrl+'/registration',user);
  }
}
