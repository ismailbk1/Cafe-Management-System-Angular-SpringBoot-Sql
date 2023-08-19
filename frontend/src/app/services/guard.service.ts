import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { SnackbarService } from './snackbar.service';
import jwt_decode from 'jwt-decode';
import { GlobalConstants } from '../shared/global-constants';
@Injectable({
  providedIn: 'root'
})
export class GuardService {

  constructor(
    private auth:AuthService,
    private route:Router,
    private snackBar:SnackbarService
  ) { }

  canActivate(router:ActivatedRouteSnapshot):boolean
  {
let data=router.data.expectedRole;
const token:any=localStorage.getItem('token')
var tokenPayload:any;
try {
  tokenPayload=jwt_decode(token)


} catch (error) {
  localStorage.clear()
  this.route.navigate(['/'])
  
}
let expectedRole=''
for(let i=0;i<2;i++){
  if(data[i]==tokenPayload.role){
    expectedRole=tokenPayload.role;

  }
}
console.log(tokenPayload.role)
if(tokenPayload.role=='user' || tokenPayload.role=='admin'){
  if(this.auth.isAuthonticated() && tokenPayload.role==expectedRole ){
    return true 
  }
  this.snackBar.openSnackBar(GlobalConstants.unauthorized,GlobalConstants.error);
  this.route.navigate(['/cafe/dashboard'])
  return false
}
else {this.route.navigate(['/'])
localStorage.clear()
return false

}
  }
}
