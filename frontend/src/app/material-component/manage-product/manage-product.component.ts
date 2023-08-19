import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ProductService } from 'src/app/services/product.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { ProductComponent } from '../dialog/product/product.component';
import { ConfirmationComponent } from '../dialog/confirmation/confirmation.component';

@Component({
  selector: 'app-manage-product',
  templateUrl: './manage-product.component.html',
  styleUrls: ['./manage-product.component.scss']
})
export class ManageProductComponent implements OnInit {
displayedColumns:String[] = ['name', 'categoryName','description', 'price', 'edit'];
dataSource:any ;
length:any ;
  responseMessage:any ;
  searchQuery: string = ''; // Property to hold the search query


  constructor(
    private productService:ProductService,
    private ngxService:NgxUiLoaderService,
    private dialog:MatDialog,
    private snackBar:SnackbarService,
    private router:Router,
  ) { }

  ngOnInit(): void {
    this.ngxService.start();
    this.tableData();
  }
  tableData() {
    this.productService.get().subscribe(
      (data:any)=>{
        this.ngxService.stop();
        this.dataSource = new MatTableDataSource(data)
  },(error:any)=>{
    this.ngxService.stop();
    this.responseMessage = error.error?.message;
    console.log(error);
    if (error.error?.message) {
      this.responseMessage = error.error?.message;
    } else {
      this.responseMessage = GlobalConstants.genericError;
    }
    this.snackBar.openSnackBar(this.responseMessage, GlobalConstants.error);
  }
  )

}
applyFilter() {
  this.dataSource.filter = this.searchQuery.trim().toLocaleLowerCase()
}
handledAddAction(){
  const dialogConfig=new MatDialogConfig();
  dialogConfig.data={
    action:'Add'
  }
  dialogConfig.width="850px"
  const dialogRef=this.dialog.open(ProductComponent,dialogConfig);
  this.router.events.subscribe(()=>{
    dialogRef.close();
  })
  const sub=dialogRef.componentInstance.onAddProduct.subscribe(()=>{

    this.tableData();
  });
}

handeledEditAction(element: any){
  const dialogConfig=new MatDialogConfig();
  console.log(element)
  dialogConfig.data={
    action:'Edit',
    data: element,
    id:element.id
  }
  dialogConfig.width="850px"
  const dialogRef=this.dialog.open(ProductComponent,dialogConfig);
  this.router.events.subscribe(()=>{
    dialogRef.close();
  })
  const sub=dialogRef.componentInstance.onAddProduct.subscribe(()=>{

    this.tableData();
  });
}

handeledDeleteAction(valeus: any){
const dialogConfig=new MatDialogConfig();
dialogConfig.data={
  message:'delete product from the database !?',
  confirmation:true,
}
const dialogRef=this.dialog.open(ConfirmationComponent, dialogConfig)
const sub=dialogRef.componentInstance.onEmitStatusChange.subscribe(()=>{
  this.ngxService.start();
  this.deleteProduct(valeus.id);
  dialogRef.close();
  



});
}
deleteProduct(id:any){
  this.productService.delete(id).subscribe(
    (res:any)=>{
      this.ngxService.stop();
      this.tableData();
      this.snackBar.openSnackBar('Product deleted successfully','success');

},err=>{
  this.ngxService.stop();
  console.log(err);
  if (err.error?.message) {
    this.responseMessage = err.error?.message;
  } else {
    this.responseMessage = GlobalConstants.genericError;
  }
  this.snackBar.openSnackBar(this.responseMessage, GlobalConstants.error);
}
  );}
onChange(status:any,id: any){
  this.ngxService.start()
  const data ={
    status:status.toString(),
    id:id
  }
  this.productService.updateStatus(data).subscribe(
    (res:any)=>{
      this.ngxService.stop();
        this.snackBar.openSnackBar("Product Status updated successfully","success")

    },err=>{
      this.ngxService.stop();
      console.log(err);
      if (err.error?.message) {
        this.responseMessage = err.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackBar.openSnackBar(this.responseMessage, GlobalConstants.error);
    });
}
}
