import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";


const TOKEN_KEY = 'token';


@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {


  constructor() { }

  signOut(): void{
    localStorage.removeItem(TOKEN_KEY);
    sessionStorage.removeItem(TOKEN_KEY);
  }

  getToken(): string {
    let token = localStorage.getItem(TOKEN_KEY);
    if (token != null){
      return token;
    }
    return sessionStorage.getItem(TOKEN_KEY);
  }

  setToken(token: string, saveInLocalMemory: boolean){
    if(saveInLocalMemory) {
      localStorage.setItem(TOKEN_KEY, token);
    }else{
      sessionStorage.setItem(TOKEN_KEY, token);
    }
  }


}
