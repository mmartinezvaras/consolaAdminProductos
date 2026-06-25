import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiClientService {
  private readonly baseUrl = 'http://localhost:8080/api';

  constructor(private readonly http: HttpClient) {}

  list<T>(endpoint: string, params?: Record<string, string | number | boolean | undefined>): Observable<T[]> {
    return this.http.get<T[]>(`${this.baseUrl}${endpoint}`, { params: this.params(params) });
  }

  path<T>(endpoint: string, params?: Record<string, string | number | boolean | undefined>): Observable<T> {
    return this.http.get<T>(`${this.baseUrl}${endpoint}`, { params: this.params(params) });
  }

  get<T>(endpoint: string, id: string | number): Observable<T> {
    return this.http.get<T>(`${this.baseUrl}${endpoint}/${id}`);
  }

  create<T>(endpoint: string, body: unknown): Observable<T> {
    return this.http.post<T>(`${this.baseUrl}${endpoint}`, body);
  }

  update<T>(endpoint: string, id: string | number, body: unknown): Observable<T> {
    return this.http.put<T>(`${this.baseUrl}${endpoint}/${id}`, body);
  }

  patch<T>(endpoint: string, id: string | number, path: string, body: unknown): Observable<T> {
    return this.http.patch<T>(`${this.baseUrl}${endpoint}/${id}${path}`, body);
  }

  delete(endpoint: string, id: string | number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}${endpoint}/${id}`);
  }

  postAction<T>(endpoint: string, body: unknown): Observable<T> {
    return this.http.post<T>(`${this.baseUrl}${endpoint}`, body);
  }

  private params(params?: Record<string, string | number | boolean | undefined>): HttpParams {
    let httpParams = new HttpParams();
    Object.entries(params ?? {}).forEach(([key, value]) => {
      if (value !== undefined && value !== '') {
        httpParams = httpParams.set(key, String(value));
      }
    });
    return httpParams;
  }
}
