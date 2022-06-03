package com.salesianostriana.gameetapi.services.impl;

import com.google.api.gax.paging.Page;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.salesianostriana.gameetapi.errors.exceptions.storage.WrongFormatException;
import com.salesianostriana.gameetapi.services.interfaces.ImageScaler;
import com.salesianostriana.gameetapi.services.interfaces.StorageService;
//import io.grpc.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.salesianostriana.gameetapi.errors.exceptions.storage.StorageException;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileSystemCloudStorage implements StorageService {

    private final ImageScaler scaler;

    private Storage storage = StorageOptions.getDefaultInstance().getService();

    @Value("${gcp.bucket.name}")
    private String bucketName;


    @Override
    public String uploadFile(MultipartFile file) {

        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        try{
            byte[] img = file.getBytes();
            String newName = file.hashCode() + "_" + filename;


            BlobId id = BlobId.of(bucketName, newName);
            BlobInfo info = storage.create(BlobInfo.newBuilder(bucketName, newName)
                    .setContentType(file.getContentType())
                    .build(), img);

            return info.getMediaLink();
        } catch (IOException ex){
            ex.printStackTrace();
        }

        return null;

    }



    @Override
    public byte[] downloadFile(String file) {
        return new byte[0];
    }


    /*
    @Override
    public Path load(String filename) {
        Blob obj = storage.get(bucketName, filename);

        return Paths.get(obj.getMediaLink());
    }
*/

    @Override
    public String deleteFile(String url) {
        //Busca el nombre del archivo en la ruta y lo elimina
        String filename = url.split("/")[9].split("\\?")[0];
        System.out.println(filename + "eliminado de GCP Storage");
        return storage.delete(bucketName, filename) ? "Se ha borrado el archivo" : "No se pudo borrar el archivo";



    }


}
