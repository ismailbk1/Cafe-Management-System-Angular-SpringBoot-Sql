import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CategoryService } from 'src/app/services/category.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent implements OnInit {
onAddCategory=new EventEmitter();
onEditCategory=new EventEmitter();
categoryForm:any= FormGroup
dialogAction:any="Add"
action:any="Add"
responseMessage:any


  constructor(@Inject(MAT_DIALOG_DATA) public dialogData:any
  ,
  private fromBuilder:FormBuilder,
  private categoryService:CategoryService,
  public dialogRef:MatDialogRef<CategoryComponent>,
  private snackbar :SnackbarService) { }



  ngOnInit(): void {

    this.categoryForm=this.fromBuilder.group({
      name:['',Validators.required],
    })

    if(this.dialogData.action==='Edit'){
      this.dialogAction="Edit"
      this.action="Update"
      this.categoryForm.patchValue(this.dialogData.data)
    }
  }
  handleSubmit(){

    if(this.dialogAction=== "Edit"){
      this.Edit();
    }
    else{
      this.Add();
    }
  }
  Edit(){
    var formData=this.categoryForm.value;
    console.log(this.dialogData.id)
    var data={
      id:this.dialogData.id,
      name:formData.name
    }
    this.categoryService.update(data).subscribe(res=>{
      this.dialogRef.close()
      this.onEditCategory.emit(res)
      this.snackbar.openSnackBar("Category Update Successfully","success")
    },(error)=>{
      this.dialogRef.close();
      console.log(error)

      
      this.snackbar.openSnackBar(error.error?.message,"error")
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackbar.openSnackBar(this.responseMessage, GlobalConstants.error);
    }
    
    )}

  Add(){
    var formData=this.categoryForm.value;
    var data={
      name:formData.name
    }
    this.categoryService.add(data).subscribe(res=>{
      this.dialogRef.close()
      this.onAddCategory.emit(res)
      this.snackbar.openSnackBar("Category Added Successfully","success")
    },(error)=>{
      this.dialogRef.close();
      console.log(error)

      
      this.snackbar.openSnackBar(error.error?.message,"error")
      if (error.error?.message) {
        this.responseMessage = error.error?.message;
      } else {
        this.responseMessage = GlobalConstants.genericError;
      }
      this.snackbar.openSnackBar(this.responseMessage, GlobalConstants.error);
    }
    
    )
  }

}
