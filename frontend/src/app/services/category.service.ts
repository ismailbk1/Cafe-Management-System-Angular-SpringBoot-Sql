import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
url=environment.apiURL;
jsonHeader = {
  headers: new HttpHeaders().set('Content-Type', 'application/json'),
};

  constructor(private httpClient:HttpClient) { }


  add(data:any) {
    console.log(data)
    console.log(this.httpClient.post(`${this.url}/category/add`,data))
    return this.httpClient.post(`http://localhost:8090/category/add`,data,this.jsonHeader);
  
  }

  update(data:any) {
    console.log(data)
    return this.httpClient.post(`${this.url}/category/update`,data,this.jsonHeader);
  
  }
  delete(data:any) {
    return this.httpClient.post(this.url+'category/delete',data,this.jsonHeader);
  
  }
  getAll() {
   // console.log("inside getAll method");
    //console.log(this.httpClient.get(`${this.url}/category/get`))
    return this.httpClient.get(`${this.url}/category/get`);
  
  }
  getCategoryFilter(){
    return this.httpClient.get(`${this.url}/category/get?filterValue=true`);
  }
}
