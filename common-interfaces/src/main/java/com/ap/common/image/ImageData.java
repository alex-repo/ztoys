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

public class ImageData {

    private byte[] imageData;
    private int width;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getBytesInPixel() {
        return bytesInPixel;
    }

    public void setBytesInPixel(int bytesInPixel) {
        this.bytesInPixel = bytesInPixel;
    }

    private int bytesInPixel;

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public ImageData(byte[] imageData, int width, int bytesInPixel) {
        this.imageData = imageData;
        this.width = width;
        this.bytesInPixel = bytesInPixel;
    }

    private ImageData() {
    }

}
