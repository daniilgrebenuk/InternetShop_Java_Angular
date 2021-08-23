import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userApiUrl = environment.userApiUrl;
  private role: string;

  constructor(private http: HttpClient) {
  }

  getCurrentUser(): Observable<any> {
    return this.http.get<any>(this.userApiUrl + '/current');
  }




}
