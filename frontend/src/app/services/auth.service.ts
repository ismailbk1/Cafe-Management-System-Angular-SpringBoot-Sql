import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private route:Router) { }


  isAuthonticated():boolean{
const token=localStorage.getItem('token');

if(!token){
this.route.navigate(['/']) 
return false
}
return true ;

  }
}
