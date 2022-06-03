/*

package com.salesianostriana.gameetapi.controllers.storage;

import com.salesianostriana.gameetapi.services.interfaces.StorageService;
import com.salesianostriana.gameetapi.utils.mediatype.MediaTypeUrlResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import org.springframework.core.io.Resource;

@RestController
@RequestMapping("/file")
public class StorageController {

    @Autowired
    private StorageService service;


    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<byte[]> getFile(@PathVariable String filename) {
        String mimeType = StringUtils.getFilenameExtension(filename);
        byte[] resource = service.downloadFile(filename);


        return ResponseEntity.status(HttpStatus.OK)
                .header("content-type", mimeType)
                .body(resource);


    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file){

        return ResponseEntity.status(HttpStatus.CREATED).body(service.uploadFile(file));
    }


}
*/

