import {Component, OnInit} from '@angular/core';
import {User} from "../model/user";
import {AuthService} from "../service/auth.service";
import {TokenStorageService} from "../service/token-storage.service";
import {environment} from "../../environments/environment";
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})


export class LoginComponent implements OnInit {

  errorMessage: string;

  constructor(
    private authService: AuthService,
    private tokenStorageService: TokenStorageService,
    private rout: Router
  ) {
  }

  ngOnInit(): void {
  }

  onSubmit(newForm: NgForm): void {
    this.authService.login(newForm.value).subscribe(
      resp => {
        this.tokenStorageService.setToken(resp.headers.get('Authorization'), newForm.value.check);
        location.reload()
      },
      error => {
        this.errorMessage = error.error;
        console.log(error.error())
      },
      () => {
      }
    )
  }


}
