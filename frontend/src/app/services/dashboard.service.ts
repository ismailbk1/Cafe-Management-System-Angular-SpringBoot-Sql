import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
url=environment.apiURL
  constructor(private htpp:HttpClient) { }
  getDetails() {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  
    console.log('Headers:', headers.keys()); // Log headers keys
    console.log('Headers:', headers); // Log headers
  
    const options = { headers: headers };
    return this.htpp.get("http://localhost:8090/dashboard/details", options);
  }
}
