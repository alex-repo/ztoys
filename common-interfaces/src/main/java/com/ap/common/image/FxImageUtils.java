/**
 * Copyright 2015 alex
 * <p>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ap.common.image;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;

import java.nio.ByteBuffer;
import java.util.function.Function;

public class FxImageUtils {

    public static int bytesInPixel(PixelFormat pixelFormat) {
        switch (pixelFormat.getType()) {
            case BYTE_BGRA_PRE:
            case BYTE_BGRA:
                return 4;
            default:
                return 4;
        }
    }

    public static byte[] getImageData(AbstractImageWrapper image) {
        return getImageData(image, 0, 0, (int) image.getWidth(), (int) image.getHeight());
    }

    public static byte[] getImageData(AbstractImageWrapper image, int x, int y, int width, int height) {
        PixelReader pixelReader = image.asFX().getPixelReader();
        PixelFormat pixelFormat = pixelReader.getPixelFormat();
        int psize = bytesInPixel(pixelFormat);
        int isize = width * height * psize;
        byte[] imageData = new byte[isize];
        WritablePixelFormat<ByteBuffer> wpixelFormat = PixelFormat.getByteBgraPreInstance();
        pixelReader.getPixels(x, y, width, height, wpixelFormat, imageData, 0, width * psize);
        return imageData;
    }

    public static void setViewImage(ImageView image_view, byte[] imageData) {
        Image image = image_view.getImage();
        image_view.setImage(writeImage(image, imageData, 0, 0, (int) image.getWidth(), (int) image.getHeight()).asFX());
    }

    private static AbstractImageWrapper writeImage(Image image, byte[] imageData, int x, int y, int width, int height) {
        // Create WritableImage
        PixelReader pixelReader = image.getPixelReader();
        AbstractImageWrapper wImage = new AbstractImageWrapper(new WritableImage((int) image.getWidth(), (int) image.getHeight()));
        PixelFormat pixelFormat = pixelReader.getPixelFormat();
        WritablePixelFormat<ByteBuffer> wpixelFormat = PixelFormat.getByteBgraPreInstance();
        PixelWriter pixelWriter = wImage.asFX().getPixelWriter();
        pixelWriter.setPixels(x, y, width, height, wpixelFormat, imageData, 0, width * bytesInPixel(pixelFormat));
        return wImage;
    }

    public static AbstractImageWrapper writeImage(AbstractImageWrapper image, byte[] imageData, int x, int y, int width, int height) {
        return writeImage(image.asFX(), imageData, x, y, width, height);
    }

    public static int bytesInPixel(AbstractImageWrapper image) {
        PixelReader pixelReader = image.asFX().getPixelReader();
        PixelFormat pixelFormat = pixelReader.getPixelFormat();
        return bytesInPixel(pixelFormat);

    }

    public static void reportProgress(Function<Long[], Void> progressCallBack, long doneWork, long allWork) {
        Long[] progress = new Long[]{doneWork, allWork};
        progressCallBack.apply(progress);
    }

}
