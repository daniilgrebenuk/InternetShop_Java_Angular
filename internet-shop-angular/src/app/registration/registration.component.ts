import {Component, OnInit} from '@angular/core';
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../service/auth.service";
import {TokenStorageService} from "../service/token-storage.service";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  isPasswordConfirmed: boolean;
  messageError: any;
  usernameIsAvailable: boolean;
  emailIsAvailable: boolean;
  hasError: boolean;

  constructor(
    private rout: Router,
    private authService: AuthService,
    private tokenStorage: TokenStorageService
  ) {
  }

  ngOnInit(): void {
    if(this.tokenStorage.getToken()){
      this.rout.navigate(['/']).then();
    }
  }

  onSubmit(newForm: NgForm) {
    this.usernameIsAvailable = true;
    this.emailIsAvailable = true;
    if (newForm.value.password != newForm.value.passwordConfirm) {
      this.isPasswordConfirmed = false;
      this.hasError = true;
      return;
    }


    this.authService.register(newForm.value).subscribe(
      data => {
        this.rout.navigate(['/']).then();
      },
      error => {
        this.messageError = error.error;
        this.usernameIsAvailable = error.error.usernameIsAvailable === 'true';
        this.emailIsAvailable = error.error.emailIsAvailable === 'true';
        this.hasError = true;
      }
    )
  }
}
