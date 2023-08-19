import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { error } from 'console';
import { Router } from '@angular/router';

@Injectable()
export class TokenInterceptorInterceptor implements HttpInterceptor {

  constructor(private router:Router) {}

 intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    const token = localStorage.getItem('token');
    console.log("inside the interceptor")
    if (token) {
      console.log(`Bearer ${token}`);

      const modifiedRequest = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        }
      });
      console.log('Modified Request:', modifiedRequest);
      console.log('Headers:', modifiedRequest.headers.keys()); // Log headers keys
      console.log('Headers:', modifiedRequest.headers);

      return next.handle(modifiedRequest);
    }

    return next.handle(request).pipe(
      catchError((error) => {
        if (error instanceof HttpErrorResponse) {
          console.log(error.url);
          if (error.status === 401 || error.status === 403) {
            if (this.router.url !== '/') {
              localStorage.clear();
              this.router.navigate(['/']);
            }
          }
        }
        return throwError(error);
      })
    );
    
  }
}
