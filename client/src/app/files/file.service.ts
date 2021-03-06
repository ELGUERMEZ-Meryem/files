import { Injectable } from '@angular/core';
import {HttpClient, HttpEvent, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable()
export class FileService {

  constructor(private http: HttpClient) { }

  upload(file: File): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();

    formData.append('file', file);

    const req = new HttpRequest('POST', `${environment.apiUrl}/upload-file`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  uploadInDataBase(file: File): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();

    formData.append('file', file);

    const req = new HttpRequest('POST', `${environment.apiUrl}/upload-file-database`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  uploadOneRequest(files: File[]): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();

    for (let file of files) {
      formData.append('files', file);
    }

    const req = new HttpRequest('POST', `${environment.apiUrl}/upload-files`, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.http.request(req);
  }

  getFiles(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/files`);
  }

  getFilesFromDatabase(): Observable<any> {
    return this.http.get(`${environment.apiUrl}/files-from-database`);
  }

  getFileById(id: string): Observable<any> {
    return this.http.post(`${environment.apiUrl}/file-by-id`, id);
  }
}
