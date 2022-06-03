package com.salesianostriana.gameetapi.services.impl;

import com.salesianostriana.gameetapi.services.interfaces.ImageScaler;
import org.springframework.stereotype.Service;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;

@Service
public class ImageScalerService implements ImageScaler {

    @Override
    public BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) {

        return Scalr.resize(originalImage, targetWidth);
    }
}
