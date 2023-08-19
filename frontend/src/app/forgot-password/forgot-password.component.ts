import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { FormBuilder, Validators } from '@angular/forms';
import { SnackbarService } from '../services/snackbar.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { MatDialogRef } from '@angular/material/dialog';
import { GlobalConstants } from '../shared/global-constants';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {
  forgotPasswordForm: any
  responseMessage: any;
  constructor(
    private userService: UserService,
    private formbuilder: FormBuilder,
    private snackbarService: SnackbarService,
    private ngxService: NgxUiLoaderService,
    public dialogRef: MatDialogRef<ForgotPasswordComponent>

  ) { }

  ngOnInit(): void {
    this.forgotPasswordForm = this.formbuilder.group({
      email: [null, [Validators.required, Validators.pattern(GlobalConstants.emailRegex)]]
    })
  }
  handleSubmit() {
    this.ngxService.start();
    var formData = this.forgotPasswordForm.value
    var data = {
      email: formData.email
    }

    this.userService.forgotPassword(data).subscribe(
      (response: any) => {
        this.ngxService.stop();
        this.responseMessage = "Pleasr Check Your Email"
        this.dialogRef.close();
        this.snackbarService.openSnackBar(this.responseMessage, "")


      }, (error) => {
        this.ngxService.stop()
        if (error.error?.message) {
          this.responseMessage = error.error?.message

        } else {
          this.responseMessage = GlobalConstants.genericError
        }
        this.snackbarService.openSnackBar(this.responseMessage, GlobalConstants.genericError)
      })
  }
}
