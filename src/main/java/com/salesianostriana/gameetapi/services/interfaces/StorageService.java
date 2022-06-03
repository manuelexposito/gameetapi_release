package com.salesianostriana.gameetapi.services.interfaces;

import com.google.cloud.storage.BlobId;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

public interface StorageService {

    String uploadFile(MultipartFile file);


    byte[] downloadFile(String file);


    //Path load(String filename);

    String deleteFile(String filename);


}
