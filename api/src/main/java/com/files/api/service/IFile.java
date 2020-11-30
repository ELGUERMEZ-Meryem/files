package com.files.api.service;

import java.io.InputStream;

public interface IFile {
    void uploadFile(InputStream file, String fileName);
}
