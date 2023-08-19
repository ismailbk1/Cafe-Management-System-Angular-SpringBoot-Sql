import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../services/user.service';
import { Route, Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from '../services/snackbar.service';
import { MatDialogRef } from '@angular/material/dialog';
import { GlobalConstants } from '../shared/global-constants';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  public showPassword: boolean = false;
loginForm:any=FormGroup
responseMessage:any

  constructor(
private userservice:UserService,
private ngxservice:NgxUiLoaderService,
private snackbar:SnackbarService,
private dialogRef:MatDialogRef<LoginComponent>,
private router:Router,
private formBuilder:FormBuilder


  ) { }

  ngOnInit(): void {
    this.loginForm=this.formBuilder.group(
      {
        email:[null,[Validators.required,Validators.pattern(GlobalConstants.emailRegex)]],
        password:[null,Validators.required]
      }
    )
  }

  handleSubmit(){
    this.ngxservice.start()
    let formData=this.loginForm.value;
    var data={
      email:formData.email,
      password:formData.password
    }
    console.log(data);
    this.userservice.login(data).subscribe(
      (response:any)=>{
this.ngxservice.stop();
this.dialogRef.close()
localStorage.setItem('token',response.token);
this.router.navigate(['/cafe/dashboard'])
      },(error)=>{
        if (error.error?.message) {
          this.responseMessage = error.error?.message;
        } else {
          this.responseMessage = GlobalConstants.genericError;
        }
        this.snackbar.openSnackBar(this.responseMessage, GlobalConstants.error);
        this.ngxservice.stop();

      }
    )
  }






  public togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }
}
