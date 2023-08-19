import { Routes } from '@angular/router';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { ManageCategoryComponent } from './manage-category/manage-category.component';
import { GuardService } from '../services/guard.service';
import { ManageProductComponent } from './manage-product/manage-product.component';
import { ManageOrderComponent } from './manage-order/manage-order.component';
import { ViewBillComponent } from './view-bill/view-bill.component';
import { ManageUserComponent } from './manage-user/manage-user.component';


export const MaterialRoutes: Routes = [
    {
        path:'category',
        component:ManageCategoryComponent,
        canActivate: [GuardService],
        data:{
            expectedRole:['admin']
        }
        


    },
    {
        path:'product',
        canActivate: [GuardService],
        component:ManageProductComponent,
        data:{
            expectedRole:['admin']
        }
    
    },
    {
        path:'order',
component:ManageOrderComponent,
canActivate: [GuardService],
        data:{
            expectedRole:['admin','user']
        }
    },
    {
        path:'bill',
component:ViewBillComponent,
canActivate: [GuardService],
        data:{
            expectedRole:['admin','user']
        }
    },
    {
        path:'user',
component:ManageUserComponent,
canActivate: [GuardService],
        data:{
            expectedRole:['admin']
        }
    }
];
