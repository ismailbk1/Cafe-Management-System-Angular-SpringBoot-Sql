import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
 url=environment.apiURL;
 jsonHeader = {
  headers: new HttpHeaders().set('Content-Type', 'application/json'),
};


  constructor(
    private http: HttpClient
  ) { }

  add(data:any) {
    console.log(data);
return this.http.post(`${this.url}/product/add`, data,this.jsonHeader);
}
get(){
  return this.http.get(`${this.url}/product/get`,this.jsonHeader);

}
update(data:any){

 return this.http.post(`${this.url}/product/update`,data,this.jsonHeader);
}
updateStatus(data:any){
 return  this.http.post(`${this.url}/product/updateStatus`,data,this.jsonHeader);
}
delete(id:number){
 return this.http.post(`${this.url}/product/delete/${id}`,this.jsonHeader);
}
getProductByCategory(id:any){
  return this.http.get(`${this.url}/product/getProductByCategory/${id}`,this.jsonHeader);
}

getById(id:any){

  return this.http.get(`${this.url}/product/getById/${id}`,this.jsonHeader);
}

}