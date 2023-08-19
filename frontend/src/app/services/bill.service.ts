import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BillService {
  url=environment.apiURL;
  jsonHeader = {
   headers: new HttpHeaders().set('Content-Type', 'application/json'),
 };

 
  constructor(    private http: HttpClient
    ) { }

    generateReport(data: any){
      return this.http.post(`${this.url}/bill/generatReport`,data,this.jsonHeader);
    }
    getPdf(data: any): Observable<Blob>{
      return this.http.post(this.url+'/bill/getPdf',data,{responseType: 'blob'});
    }
    getBills(){
      return this.http.get(this.url+'/bill/getBills',this.jsonHeader);
    }
    delete(id:any){

      return this.http.post(this.url+'/bill/delete/'+id,this.jsonHeader);

    }

}
