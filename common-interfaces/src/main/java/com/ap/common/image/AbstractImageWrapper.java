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

import com.ap.common.model.Kernel;
import com.ap.common.util.CommonUtils;
import com.ap.common.util.ImageUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelFormat.Type;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class AbstractImageWrapper implements IImage {

    final WritableImage writableImage;

    private PixelReader pixelReader;
    private PixelWriter pixelWriter;
    private PixelFormat<ByteBuffer> pixelFormat;
    private PixelFormat<ByteBuffer> writablePixelFormat;
    private Integer label;
    private int indexr;
    private int indexg;
    private int indexb;
    private int indexa;

    double COLOR_MAX_INTENSITY = 255;
    int COLOR_HISTOGRAM_SIZE = (int) (COLOR_MAX_INTENSITY + 1);
    double HUE_MAX_INTENSITY = 359;
    int HUE_HISTOGRAM_SIZE = (int) (HUE_MAX_INTENSITY + 1);

    Type type;

    public enum CHANNEL {
        RED, GREEN, BLUE, OPACITY
    }

    public WritableImage getWritableImage() {
        return writableImage;
    }

    public PixelWriter getPixelWriter() {
        return pixelWriter;
    }

    public PixelFormat<ByteBuffer> getWritablePixelFormat() {
        return writablePixelFormat;
    }

    // TODO: 12/01/16 Label as parameter everywhere
    public AbstractImageWrapper(WritableImage writableImage) {
        pixelReader = writableImage.getPixelReader();
        this.writableImage = writableImage;
        initFields(writableImage.toString());
        CommonUtils.getLogger(getClass()).debug(label + " " + type);
    }

    public AbstractImageWrapper(javafx.scene.image.Image image) {
        this.writableImage = getWritebleImage(image);
        initFields(writableImage.toString());
        CommonUtils.getLogger(getClass()).debug(label + " " + type);
    }

    public AbstractImageWrapper(WritablePixelFormat<ByteBuffer> wpixelFormat, byte[] buffer, int width, int height) {
        pixelFormat = wpixelFormat;
        this.writableImage = new WritableImage(width, height);
        pixelReader = writableImage.getPixelReader();
        pixelWriter = this.writableImage.getPixelWriter();
        pixelWriter.setPixels(0, 0, width, height, wpixelFormat, buffer, 0, width * 4);
        initFields(writableImage.toString());
        CommonUtils.getLogger(getClass()).debug(label + " " + type);
    }

    public AbstractImageWrapper(String url) {
        javafx.scene.image.Image image = new Image(url);
        this.writableImage = getWritebleImage(image);
        initFields(url);
        CommonUtils.getLogger(getClass()).debug(label + " " + type);
    }

    public AbstractImageWrapper(File file) throws Exception {
        try (InputStream inputStream = new FileInputStream(file)) {
            javafx.scene.image.Image image = new Image(inputStream);
            this.writableImage = getWritebleImage(image);
            initFields(file.getName());
        }
        CommonUtils.getLogger(getClass()).debug(label + " " + type);
    }

    private WritableImage getWritebleImage(Image image) {
        pixelReader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        return new javafx.scene.image.WritableImage(pixelReader, width, height);
    }

    private void initFields(String label) {
        int start = label.indexOf("#");
        int end = label.lastIndexOf(".");
        if (start > -1) {
            this.label = new Integer(label.substring(start + 1, end));
        } else {
            this.label = -1;
        }

        pixelWriter = this.writableImage.getPixelWriter();
        pixelFormat = pixelReader.getPixelFormat();
        type = pixelFormat.getType();

        switch (type) {
            case BYTE_BGRA:
                writablePixelFormat = PixelFormat.getByteBgraInstance();
                indexr = 2;
                indexg = 1;
                indexb = 0;
                indexa = 3;
                break;
            case BYTE_BGRA_PRE:
                writablePixelFormat = PixelFormat.getByteBgraPreInstance();
                indexr = 2;
                indexg = 1;
                indexb = 0;
                indexa = 3;
                break;
            case BYTE_INDEXED:
                throw new UnsupportedOperationException();
            case BYTE_RGB:
                writablePixelFormat = PixelFormat.getByteRgbInstance();
                indexr = 0;
                indexg = 1;
                indexb = 2;
                indexa = -1;
                break;
            case INT_ARGB:
                throw new UnsupportedOperationException();
            case INT_ARGB_PRE:
                throw new UnsupportedOperationException();
            default:
                break;
        }

    }

    private int c256(double color) {
        return (int) Math.round(color * COLOR_MAX_INTENSITY);
    }

    private int cropY(double lastY, int side) {
        return ((int) lastY + side > getHeight()) ? (int) getHeight() - side : (int) lastY;
    }

    private int cropX(double lastX, int side) {
        return ((int) lastX + side > getWidth()) ? (int) getWidth() - side : (int) lastX;
    }

    public double[] asGrayDoubleArray() {
        CommonUtils.getLogger(getClass()).trace(getLabel());
        assert pixelFormat.isPremultiplied();

        PixelReader reader = writableImage.getPixelReader();
        double[] data = new double[getSize()];
        for (int y = 0; y < writableImage.getHeight(); y++) {
            for (int x = 0; x < writableImage.getWidth(); x++) {
                Color color = reader.getColor(x, y).grayscale();
                double red = color.getRed();
                data[x + (int) writableImage.getWidth() * y] = red;
                System.out.printf("%4x", Math.round(red * 256));//
            }
            System.out.println();
        }
        return data;
    }

    byte[] getBuffer() {
        byte[] buffer = new byte[((int) getWidth() * (int) getHeight() * 4)];
        pixelReader.getPixels(0, 0, (int) getWidth(), (int) getHeight(), (WritablePixelFormat<ByteBuffer>) writablePixelFormat, buffer, 0,
                (int) getWidth() * 4);
        return buffer;
    }

    @Override
    public int getSize() {
        return (int) (writableImage.getWidth() * writableImage.getHeight());
    }

    @Override
    public Integer getLabel() {
        return label;
    }

    @Override
    public double getWidth() {
        return writableImage.getWidth();
    }

    @Override
    public double getHeight() {
        return writableImage.getHeight();
    }

    @Override
    public byte[] getBuffer(double lastX, double lastY, int width, int height) {

        byte[] buffer = new byte[width * height * 4];
        int X = cropX(lastX, width);
        int Y = cropY(lastY, height);
        pixelReader.getPixels((X > 0 ? X : 0), (Y > 0 ? Y : 0), width, height, (WritablePixelFormat<ByteBuffer>) writablePixelFormat, buffer, 0, width * 4);
        return buffer;

    }

    public javafx.scene.image.WritableImage asFX() {
        return writableImage;
    }

    public void setPixels(int x, int y, ImageData result) {
        int scanlineStride = result.getWidth() * FxImageUtils.bytesInPixel(pixelFormat);
        pixelWriter.setPixels(x, y, result.getWidth(), result.getImageData().length / scanlineStride, writablePixelFormat, result.getImageData(), 0,
                scanlineStride);

    }

    public PixelReader getPixelReader() {
        return pixelReader;
    }

    public PixelFormat<ByteBuffer> getPixelFormat() {
        return writablePixelFormat;
    }

    public byte[] getArea(double centerX, double centerY, int half) {
        int side = half * 2 + 1;
        byte[] buffer = new byte[side * side * 4];
        double X = (centerX + side < getWidth()) ? centerX : getWidth() - side - 1;
        double Y = (centerY + side < getHeight()) ? centerY : getHeight() - side - 1;
        pixelReader.getPixels((X > 0 ? (int) X : 0), (Y > 0 ? (int) Y : 0), side, side, (WritablePixelFormat<ByteBuffer>) writablePixelFormat, buffer, 0,
                side * 4);
        return buffer;
    }

    public double[] getDataConvolved(Kernel kernel) {
        assert pixelFormat.isPremultiplied();
        double[] data = asGrayDoubleArray();
        double[] dbuffer = new double[data.length];
        int width = kernel.getWidth();
        int height = kernel.getHeight();
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                double[] kBuffer = ImageUtils.getSubData(data, x, y, (int) getWidth(), width, height);
                dbuffer[(int) (x + y * getWidth())] = kernel.convolve(kBuffer);
            }
        }
        return dbuffer;
    }

    public double[] getHistogramEqualization(double[] LUT, boolean inverse) {
        int pixelCount = (int) (getWidth() * getHeight());
        double[] buffer = new double[pixelCount];
        double sum = 0;
        double[] lut;
        if (LUT == null) {
            lut = getHistogramLUT();
        } else {
            lut = LUT;
        }
        for (int i = 0; i < pixelCount; ++i) {
            if (inverse) {
                buffer[i] = lut[(int) (COLOR_MAX_INTENSITY - c256(gray(i)))];
            } else {
                buffer[i] = lut[c256(gray(i))];
            }
        }
        return buffer;
    }

    public double[] getHistogramLUT() {
        int pixelCount = (int) getWidth() * (int) getHeight();
        double sum = 0;
        double[] lut = new double[COLOR_HISTOGRAM_SIZE];
        int[] histogram = new int[COLOR_HISTOGRAM_SIZE];
        for (int i = 0; i < pixelCount; i++) {
            int index = c256(gray(i));
            histogram[index]++;
        }
        for (int i = 0; i < COLOR_HISTOGRAM_SIZE; ++i) {
            sum += histogram[i];
            lut[i] = sum / pixelCount;
        }
        return lut;
    }

    private double gray(int i) {
        int y = (int) (i / getWidth());
        int x = (int) (i % getWidth());
        Color color = pixelReader.getColor(x, y).grayscale();
        return color.getRed();
    }

    public double[] getChannel(double lastX, double lastY, int width, int height, CHANNEL name) {
        byte[] buffer = getBuffer(lastX, lastY, width, height);
        int pixelCount = width * height;
        double[] channel = new double[width * height];
        int offset = 0;
        switch (name) {
            case RED:
                offset = indexr;
                break;
            case GREEN:
                offset = indexg;
                break;
            case BLUE:
                offset = indexb;
                break;
            case OPACITY:
                offset = indexa;
                break;
            default:
                break;
        }

        for (int i = 0; i < pixelCount; i++) {
            channel[i] = (buffer[i * 4 + offset] & 0xff) / COLOR_MAX_INTENSITY;
        }
        return channel;
    }

    public double[] getHistogramHUELUT() {
        byte[] buffer = getBuffer();
        int pixelCount = (int) getWidth() * (int) getHeight();
        double sum = 0;
        double[] lut = new double[HUE_HISTOGRAM_SIZE];
        int[] histogram = new int[HUE_HISTOGRAM_SIZE];

        for (int i = 0; i < pixelCount; i++) {

            int ired = 0xff & buffer[i * 4 + indexr];
            int igreen = 0xff & buffer[i * 4 + indexg];
            int iblue = 0xff & buffer[i * 4 + indexb];
            int ia = 0xff & buffer[i * 4 + indexa];

            Color color = Color.rgb(ired, igreen, iblue, ia / COLOR_MAX_INTENSITY);
            double hue = color.getHue();
            assert hue <= 360.;
            int index = (int) Math.round(hue);
            histogram[index >= HUE_MAX_INTENSITY ? 0 : index]++;
        }

        for (int i = 0; i < HUE_HISTOGRAM_SIZE; ++i) {
            sum += histogram[i];
            lut[i] = sum / pixelCount;
        }
        return lut;
    }

    byte[] getBufferPosterize(double lastX, double lastY, int side, float level, FOrtoCube cube) {
        byte[] buffer = getBuffer(lastX, lastY, side, side);
        float[] hsv = new float[3];
        for (int i = 0; i < side * side; i++) {
            int ired = 0xff & buffer[i * 4 + indexr];
            int igreen = 0xff & buffer[i * 4 + indexg];
            int iblue = 0xff & buffer[i * 4 + indexb];
            java.awt.Color.RGBtoHSB(ired, igreen, iblue, hsv);
            if (i == 0) {
                System.out.print("*,* " + ired + " | " + igreen + " | " + iblue + " | " + hsv[0] + " | " + hsv[1] + " | " + hsv[2]);
            }

            float r = (float) (ired / 255.);
            float g = (float) (igreen / 255.);
            float b = (float) (iblue / 255.);

            int ir = (int) ((Math.round(r * level) / level) * COLOR_MAX_INTENSITY);
            int ig = (int) ((Math.round(g * level) / level) * COLOR_MAX_INTENSITY);
            int ib = (int) ((Math.round(b * level) / level) * COLOR_MAX_INTENSITY);

            java.awt.Color.RGBtoHSB(ir, ig, ib, hsv);
            if (i == 0) {
                System.out.println(" -> *,* " + ir + " | " + ig + " | " + ib + " | " + hsv[0] + " | " + hsv[1] + " | " + hsv[2]);
            }

            if (cube.isBelong(hsv)) {
                buffer[i * 4 + indexr] = (byte) (0xff & ir);
                buffer[i * 4 + indexg] = (byte) (0xff & ig);
                buffer[i * 4 + indexb] = (byte) (0xff & ib);
            } else {
                buffer[i * 4 + indexr] = 0;
                buffer[i * 4 + indexg] = 0;
                buffer[i * 4 + indexb] = 0;
                if (indexa >= 0) {
                    buffer[i * 4 + indexa] = 0;
                }
            }
        }
        return buffer;
    }

    byte[] getBufferConvolved(double lastX, double lastY, int side, Kernel kernel) {
        assert pixelFormat.isPremultiplied();
        byte[] buffer = getBuffer(lastX, lastY, side, side);
        int X = cropX(lastX, side);
        int Y = cropY(lastY, side);
        assert kernel.getWidth() == kernel.getHeight();
        int kernelSide = kernel.getHeight();

        for (int x = X; x < X + side; x++) {
            for (int y = Y; y < Y + side; y++) {
                byte[] kBuffer = getBuffer(x, y, kernelSide, kernelSide);
                double[] kCBuffer = new double[kernelSide * kernelSide];
                for (int i = 0; i < 4; i++) {
                    if (kernel.getFmsum() == 0 && i == indexa) {
                        continue;
                    }
                    for (int xi = 0; xi < kernelSide; xi++) {
                        for (int yi = 0; yi < kernelSide; yi++) {
                            kCBuffer[xi + yi * kernelSide] = (kBuffer[xi * 4 + yi * kernelSide * 4 + i] & 0xff) / COLOR_MAX_INTENSITY;
                        }
                    }
                    buffer[(x - X) * 4 + (y - Y) * side * 4 + i] = (byte) ((int) (kernel.convolve(kCBuffer) * COLOR_MAX_INTENSITY) & 0xff);
                }
            }
        }
        return buffer;
    }

    @Override
    public int getPixel(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPixel(int x, int y, int color) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getPixels(int offset, int stride, int x, int y, int width, int height) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IImage resize(int width, int height) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IImage crop(int x1, int y1, int x2, int y2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
