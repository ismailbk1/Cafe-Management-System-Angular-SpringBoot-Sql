import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { CategoryService } from 'src/app/services/category.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { CategoryComponent } from '../dialog/category/category.component';

@Component({
  selector: 'app-manage-category',
  templateUrl: './manage-category.component.html',
  styleUrls: ['./manage-category.component.scss']
})
export class ManageCategoryComponent implements OnInit {
  dataSource: any;
  displayedColumns: String[] = ["name", "edit"];
  responseMessage: any;
  searchQuery: string = ''; // Property to hold the search query

  constructor(
    private categoryService: CategoryService,
    private router: Router,
    private ngxService: NgxUiLoaderService,
    private dialog: MatDialog,
    private snckbar: SnackbarService
  ) { }

  ngOnInit(): void {
    this.ngxService.start();
    this.tableData();
    this.ngxService.stop();
  }

  tableData() {
    this.categoryService.getAll().subscribe((data: any) => {
      this.ngxService.stop();
      this.dataSource = new MatTableDataSource(data);
    }
      , (error) => {
        console.log(error);
        if (error.error?.message) {
          this.responseMessage = error.error?.message;
        } else {
          this.responseMessage = GlobalConstants.genericError;
        }
        this.snckbar.openSnackBar(this.responseMessage, GlobalConstants.error);

      }


    )

  }
    applyFilter() {
      this.dataSource.filter = this.searchQuery.trim().toLocaleLowerCase()
    }
  handleAddAction() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      action: 'Add',
    };
    dialogConfig.width = '550px';
    const dialogRef = this.dialog.open(CategoryComponent, dialogConfig);
    this.router.events.subscribe(() => { //This subscribes to the router's events. Whenever there's a navigation event (such as changing routes),
      // the dialogRef (the reference to the opened dialog) is closed.
      dialogRef.close();
    });

    const sub = dialogRef.componentInstance.onAddCategory.subscribe(
      (resp: any) => {
        this.tableData();
      }
    );
  }
  handleEditAction(values: any) {
    console.log(values);
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      action: 'Edit',
      data:values,
      id:values.id
    };
    dialogConfig.width = '550px';
    const dialogRef = this.dialog.open(CategoryComponent, dialogConfig);
    this.router.events.subscribe(() => { //This subscribes to the router's events. Whenever there's a navigation event (such as changing routes),
      // the dialogRef (the reference to the opened dialog) is closed.
      dialogRef.close();
    });

    const sub = dialogRef.componentInstance.onEditCategory.subscribe(
      (resp: any) => {
        this.tableData();
      }
    );
  }
}
