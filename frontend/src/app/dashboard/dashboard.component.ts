import { Component, AfterViewInit } from '@angular/core';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from '../services/snackbar.service';
import { GlobalConstants } from '../shared/global-constants';
import { DashboardService } from '../services/dashboard.service';
import { resolve } from 'dns';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements AfterViewInit {

  responseMessage:any;
  data:any;

  ngAfterViewInit(): void {
    // This is where you can put code that needs to run after the view has been initialized.
    // For example, if you need to interact with DOM elements or other view-related tasks.
  }

  constructor(
    private dashboardService:DashboardService,
   private ngxService:NgxUiLoaderService,
    private snackbarService:SnackbarService
  ){
    ngxService.start();
    this.dashboardData()
    

  }
  dashboardData(){
    this.dashboardService.getDetails().subscribe(
      (response:any)=>{
        console.log("isnide the dashboard data ")
        console.log(response)
this.ngxService.stop()
this.data=response;
      },
      (error)=>{
        console.log("isnide the dashboard data in the error phase ")
console.log(error)
this.ngxService.stop();
if(error.error?.message){
  this.responseMessage=error.error?.message
}else{
  this.responseMessage=GlobalConstants.genericError;
}
this.snackbarService.openSnackBar(this.responseMessage,GlobalConstants.error)
      }
    )
  }
 
}
