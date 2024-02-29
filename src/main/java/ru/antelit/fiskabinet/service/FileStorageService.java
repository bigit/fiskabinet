package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileStorageService {

    @Autowired
    private MinioService minioService;

    public String saveFile(File file) {
        return null;

    }

    public String getPath(String filename) {
        return null;
    }
}