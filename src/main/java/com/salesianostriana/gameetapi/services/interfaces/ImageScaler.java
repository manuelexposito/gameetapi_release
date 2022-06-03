package com.salesianostriana.gameetapi.services.interfaces;

import java.awt.image.BufferedImage;

public interface ImageScaler {
    BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) throws Exception;
}
