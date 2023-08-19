import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { error } from 'console';
import { saveAs } from 'file-saver';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { BillService } from 'src/app/services/bill.service';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';

@Component({
  selector: 'app-manage-order',
  templateUrl: './manage-order.component.html',
  styleUrls: ['./manage-order.component.scss']
})
export class ManageOrderComponent implements OnInit {
displayedColumns: string[] = ['name', 'category', 'price', 'quantity', 'total', 'edit'];
dataSource:any=[];
manageOrderForm:any=FormGroup
categorys:any=[]
products:any=[]
totalAmount:number=0;
responseMessage:any;
price:any;
  constructor(
    private categoryService:CategoryService,
    private productService:ProductService,
    private formBuilder:FormBuilder,
    private snackBar:SnackbarService,
    private billService:BillService,
    private ngxService:NgxUiLoaderService

    
  ) { }

  ngOnInit(): void {
    this.ngxService.start();
    this.getCategories();

    this.manageOrderForm = this.formBuilder.group({
      name: ['', Validators.required],
     email: ['', Validators.required,Validators.pattern(GlobalConstants.emailRegex)],
     contactNumber: ['', Validators.required,Validators.pattern(GlobalConstants.contactNumberRegex)],
     paymentMethod: ['', Validators.required],
    product:[null, Validators.required],
    category: [null, Validators.required],
    quantity: [null, Validators.required],
    price: [null, Validators.required],
    total: [0, Validators.required]
    });
  }
getCategories(){
  this.categoryService.getCategoryFilter().subscribe(res=>{
    console.log(res);
    this.categorys=res;
    this.ngxService.stop();
  },error=>{
    this.ngxService.stop();
    console.log(error);
    this.snackBar.openSnackBar(GlobalConstants.error,'error');
  })
}
getProductByCategory(values:any) {
  this.productService.getProductByCategory(values.id).subscribe(res=>{
    this.products=res;
this.manageOrderForm.controls['price'].setValue('')
this.manageOrderForm.controls['quantity'].setValue('')
this.manageOrderForm.controls['total'].setValue(0)


  },error=>{
    console.log(error);
    
    if (error.error?.message) {
      this.responseMessage = error.error?.message;
    } else {
      this.responseMessage = GlobalConstants.genericError;
    }
    this.snackBar.openSnackBar(this.responseMessage, GlobalConstants.error);
  });
    
    
}

getProductDetails(values:any) {
 this.productService.getById(values.id).subscribe(
  (res:any)=>{
    console.log(res);
    this.price=res.price;
    this.manageOrderForm.controls['price'].setValue(res.price);
    this.manageOrderForm.controls['quantity'].setValue('1');
    this.manageOrderForm.controls['total'].setValue(this.price*1);

  },error=>{
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
setQuantity(values:any){
  var temp=this.manageOrderForm.controls['quantity'].value;
  if(temp>0){
    this.manageOrderForm.controls['total'].setValue(this.manageOrderForm.controls['quantity'].value *this.manageOrderForm.controls['price'].value);

  }
  else if(temp!=''){
    this.manageOrderForm.controls['quantity'].setValue(1);
    this.manageOrderForm.controls['total'].setValue(this.manageOrderForm.controls['price'].value*this.manageOrderForm.controls['quantity'].value)
  }

}
validateProductAdd(){
  if(this.manageOrderForm.controls['total'].value===0 || this.manageOrderForm.controls['total'].value===null
 || this.manageOrderForm.controls['quantity'].value<=0){
    return true;
  }
  return false;
}
validateSubmit(){
  if(  this.totalAmount === 0 ||
    this.manageOrderForm.controls.name.value === null ||
    this.manageOrderForm.controls.email.value === null ||
    this.manageOrderForm.controls.contactNumber.value === null ||
    this.manageOrderForm.controls.paymentMethod.value === null ||
    !this.manageOrderForm.controls.contactNumber.valid ||
    !this.manageOrderForm.controls.email.valid) return true;

    return false;
}
add() {
  let formData = this.manageOrderForm.value;
  let productName = this.dataSource.find(
    (e: { id: number; }) => e.id == formData.product.id
  );
  if (productName === undefined) {
    this.totalAmount += formData.total;
    this.dataSource.push({
      id: formData.product.id,
      name: formData.product.name,
      category: formData.category.name,
      quantity: formData.quantity,
      price: formData.price,
      total: formData.total,
    });

    this.dataSource = [...this.dataSource];
    this.snackBar.openSnackBar(GlobalConstants.productAdded, 'success');
  } else {
    this.snackBar.openSnackBar(
      GlobalConstants.productExistError,
      GlobalConstants.error
    );
  }
}
handleDeletAction(value: any, element: any) {
  this.totalAmount -= element.total;
  this.dataSource.splice(value, 1);
  this.dataSource = [...this.dataSource];
}

submitAction() {
  this.ngxService.start();
  let formData = this.manageOrderForm.value;
  let data = {
    name: formData.name,
    email: formData.email,
    contactNumber: formData.contactNumber,
    paymentMethod: formData.paymentMethod,
    totalAmount: this.totalAmount,
    productDetails: JSON.stringify(this.dataSource),
  };

  this.billService.generateReport(data).subscribe(
    (resp: any) => {
      console.log(resp);
      this.downloadFile(resp?.uuid);
      this.manageOrderForm.reset();
      this.dataSource = [];
      this.totalAmount = 0;
      // Open the PDF in a new tab/window
      const pdfUrl = `C:\Users\HP\Desktop\Back-end\AllPdf${resp?.uuid}.pdf`;
      window.open(pdfUrl, '_blank');
    },
    (error) => {
      console.log(error)
      this.ngxService.stop();
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackBar.openSnackBar(this.responseMessage, GlobalConstants.error);
    }
  );
}
downloadFile(fileName: any) {
  let data = {
    uuid: fileName,
  };

  this.billService.getPdf(data).subscribe((resp: any) => {
    saveAs(resp, fileName + '.pdf');
    this.ngxService.stop();
  });
}

}
