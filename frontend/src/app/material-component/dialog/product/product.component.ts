import { Component, EventEmitter, OnInit, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {
  onAddProduct = new EventEmitter();
  onEditProduct = new EventEmitter();
  productForm: any = FormGroup
  dialogAction: any = "Add";
  action: any = "Add";
  categorys: any = []
  responseMessage: any;



  constructor(
    @Inject(MAT_DIALOG_DATA) public dialogData: any,
    private fromBuilder: FormBuilder,
    private productService: ProductService,
    private categoryService: CategoryService,
    private snackbar: SnackbarService,
    private diologRef: MatDialogRef<ProductComponent>

  ) { }

  ngOnInit(): void {
    this.productForm = this.fromBuilder.group({
      name: ['', Validators.required],
      price: ['', Validators.required],
      categoryId: ['', Validators.required],
      description: ['', Validators.required]
    });

    if (this.dialogData.action === "Edit") {
      this.dialogAction = "Edit";
      this.action = "Update";
      this.productForm.patchValue(this.dialogData.data);

    }
    this.getCategorys()
  }
  getCategorys() {
    this.categoryService.getAll().subscribe(res => {
      this.categorys = res;
    }, err => {
      console.log(err);
      if (err.error?.message) {
        this.responseMessage = err.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackbar.openSnackBar(this.responseMessage, GlobalConstants.error);
    }


    )
  }
  handleSubmit() {
    if (this.dialogData.action === "Edit") {
      console.log("inside the edit dialg")
      console.log(this.dialogData.data)

   this.edit();
    } else {
     
     this.add();
    }
  }
  edit() {
    console.log("inside the edit dialg")

    var formData = this.productForm.value;
    var data = {
      id: this.dialogData.data.id,
      name: formData.name,
      price: formData.price,
      categoryId: formData.categoryId,
      description: formData.description
    }
    console.log(data);
    this.productService.update(data).subscribe((res: any) => {

      this.diologRef.close()
      this.onEditProduct.emit()
      this.responseMessage = "Product Edited successfully"
      this.snackbar.openSnackBar(this.responseMessage, GlobalConstants.productAdded);

    }, (err) => {
      console.log(err);
      if (err.error?.message) {
        this.responseMessage = err.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackbar.openSnackBar(this.responseMessage, GlobalConstants.error);

    }


    )

  }

  add() {
    console.log("inside the add dialg")

    var formData = this.productForm.value;
    var data = {
      name: formData.name,
      price: formData.price,
      categoryId: formData.categoryId,
      description: formData.description
    }
     console.log(data);
    this.productService.add(data).subscribe((res: any) => {

      this.diologRef.close()
      this.onAddProduct.emit()
      this.responseMessage = "Product added successfully"
      this.snackbar.openSnackBar(this.responseMessage, GlobalConstants.productAdded);

    }, (err) => {
      console.log(err);
      if (err.error?.message) {
        this.responseMessage = err.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackbar.openSnackBar(this.responseMessage, GlobalConstants.error);

    }


    )
  }
}
