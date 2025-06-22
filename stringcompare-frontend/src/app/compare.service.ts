import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CompareService {
  private apiUrl = 'http://localhost:8080/compare';

  constructor(private http: HttpClient) {}

  compare(input: string): Observable<any> {
    return this.http.post(this.apiUrl, { input });
  }
}