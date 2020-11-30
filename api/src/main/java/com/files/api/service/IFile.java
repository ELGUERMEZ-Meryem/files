package com.files.api.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IFile {
    void uploadFile(InputStream file, String fileName);

    List<String> getFiles() throws IOException;
}
