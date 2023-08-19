import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { BillService } from 'src/app/services/bill.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { ViewBillProductsComponent } from '../dialog/view-bill-products/view-bill-products.component';
import { ConfirmationComponent } from '../dialog/confirmation/confirmation.component';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-view-bill',
  templateUrl: './view-bill.component.html',
  styleUrls: ['./view-bill.component.scss']
})
export class ViewBillComponent implements OnInit {
displayColumns: string[] =['name', 'email','contactNumber','paymentMethod','total','view']
dataSource:any 
responseMessage:any
  searchQuery: any;

constructor(
  private billService:BillService,
  private ngxService:NgxUiLoaderService,
  private dialog:MatDialog,
  private router:Router,
  private snackbar:SnackbarService
  
) { }

  ngOnInit(): void {
    this.ngxService.start();
    this.tableData();
  }
tableData(){
  this.billService.getBills().subscribe((res:any) =>{
console.log(res);
    this.ngxService.stop();
    this.dataSource=new MatTableDataSource(res);

  },error =>{
    console.log(error)
      this.ngxService.stop();
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackbar.openSnackBar(this.responseMessage, GlobalConstants.error);
  }
    
    );
}
applyFilter() {
  this.dataSource.filter = this.searchQuery.trim().toLocaleLowerCase()
}
handleViewAction(values:any) {
  const dialogConfig = new MatDialogConfig();
  dialogConfig.data={
    data: values

  }
  dialogConfig.width = "100%"
  const dialogRef=this.dialog.open(ViewBillProductsComponent,dialogConfig);
  this.router.events.subscribe(() =>{
  dialogRef.close();
  });
}
handleDeleteAction(values:any){
  const dialogConfig = new MatDialogConfig();
  dialogConfig.data={
    data: values,
message:'Delete selected bill '+values.name+'?',
confirmation:true
  };
  const dialogRef=this.dialog.open(ConfirmationComponent,dialogConfig);
  const sub =dialogRef.componentInstance.onEmitStatusChange.subscribe((res) => {
this.ngxService.stop();
this.deleteBill(values.id);
this.tableData();
this,dialogRef.close();
  });

}
deleteBill(id:number) {
  console.log(id);
this.billService.delete(id).subscribe((res:any) => {
  this.ngxService.stop();
  this.tableData();
  this.snackbar.openSnackBar('Bill deleted successfully', 'success');
},error => {
  console.log("inside the delete bill in  the error section of")
  
  console.log(error)
      this.ngxService.stop();
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericError;
        this.tableData();

      }
  
      this.snackbar.openSnackBar("Bill deleted Succefully ", "success");
      this.tableData();

}); 

}
downloadReportAction(values:any){
this.ngxService.start();
var data={
  name:values.name,
  email:values.email,
  uuid:values.uuid,
  contactNumber:values.contactNumber,
  paymentMethod:values.paymentMethod,
  totalAmount:values.total.toString(),
  productDetails:values.productDetails
}
this.downloadFile(values.uuid,data);
}
  downloadFile(fileName: string, data: any) {
    console.log(data);
    console.log(fileName)
    this,this.billService.getPdf(data).subscribe(res => {
      saveAs(res, fileName + '.pdf');
      this.ngxService.stop();
    }
    , err => {
      console.log(err);
      this.ngxService.stop();
    });
  }
}
