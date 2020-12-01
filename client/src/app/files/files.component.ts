import { Component, OnInit } from '@angular/core';
import {FileService} from "./file.service";
import {Observable} from "rxjs";
import {HttpEventType, HttpResponse} from "@angular/common/http";
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-files',
  templateUrl: './files.component.html',
  styleUrls: ['./files.component.css']
})
export class FilesComponent implements OnInit {

  selectedFiles: File[];
  currentFile: File;
  progress = 0;
  progressInfos = [];
  message = '';
  fileInfos: Observable<any>;
  fileInfosInDatabase: Observable<any>;

  constructor(private fileService: FileService) { }

  ngOnInit(): void {
    this.fileInfos = this.fileService.getFiles();
    this.fileInfosInDatabase = this.fileService.getFilesFromDatabase();
  }

  selectFiles(event) {
    this.progressInfos = [];
    this.selectedFiles = event.target.files;
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  uploadFiles() {
    this.message = '';

    for (let i = 0; i < this.selectedFiles.length; i++) {
      this.upload(i, this.selectedFiles[i]);
    }

  }

  upload(index, file) {
    this.progressInfos[index] = { value: 0, fileName: file.name };

    this.fileService.upload(file).subscribe(
      event => {
        if (event.type === HttpEventType.UploadProgress) {
          this.progressInfos[index].value = Math.round(100 * event.loaded / event.total);
        } else if (event instanceof HttpResponse) {
          this.fileInfos = this.fileService.getFiles();
        }
      },
      err => {
        this.progressInfos[index].value = 0;
        this.message = 'Could not upload the file:' + file.name;
      });
  }

  uploadFile() {
    this.progress = 0;

    this.currentFile = this.selectedFiles[0];
    this.fileService.upload(this.currentFile).subscribe(
      event => {
        if (event.type === HttpEventType.UploadProgress) {
          this.progress = Math.round(100 * event.loaded / event.total);
        } else if (event instanceof HttpResponse) {
          this.message = event.body.message;
          this.fileInfos = this.fileService.getFiles();
        }
      },
      err => {
        this.progress = 0;
        this.message = 'Could not upload the file!';
        this.currentFile = undefined;
      });

    this.currentFile = undefined;
  }

  uploadFileInDataBase() {
    this.progress = 0;
    this.currentFile = this.selectedFiles[0];
    this.fileService.uploadInDataBase(this.currentFile).subscribe(
      event => {
        if (event.type === HttpEventType.UploadProgress) {
          this.progress = Math.round(100 * event.loaded / event.total);
        } else if (event instanceof HttpResponse) {
          this.message = event.body.message;
          this.fileInfosInDatabase = this.fileService.getFilesFromDatabase();
        }
      },
      err => {
        this.progress = 0;
        this.message = 'Could not upload the file!';
        this.currentFile = undefined;
      });

    this.currentFile = undefined;
  }

  uploadFilesOneRequest() {
    this.progress = 0;

    this.currentFile = this.selectedFiles[0];
    this.fileService.uploadOneRequest(this.selectedFiles).subscribe(
      event => {
        if (event.type === HttpEventType.UploadProgress) {
          this.progress = Math.round(100 * event.loaded / event.total);
        } else if (event instanceof HttpResponse) {
          this.message = event.body.message;
          this.fileInfos = this.fileService.getFiles();
        }
      },
      err => {
        this.progress = 0;
        this.message = 'Could not upload the file!';
        this.currentFile = undefined;
      });

    this.currentFile = undefined;
  }

  uploadFileById(id: string) {
    this.fileService.getFileById(id).subscribe(
      data => {
        saveAs(data.data, data.name);

      },
      err => {
        this.message = 'Could not get the file!';
      });

  }
}
