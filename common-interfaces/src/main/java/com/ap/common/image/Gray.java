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

import java.util.function.Function;

public class Gray implements IEffect {

    /**
    * BGRA Y = 0.2126 * R + 0.7152 * G + 0.0722 * B - Wikipedia (en.wikipedia.org/wiki/Grayscale)
    */
    @Override
    public void effect(ImageData imageData, Function<Long[], Void> progressCallBack) {
        byte[] data = imageData.getImageData();
        for (int i = 0; i < data.length / imageData.getBytesInPixel(); i++) {
            grayscalepx(data, i * imageData.getBytesInPixel());
        }
    }

    private static void grayscalepx(byte[] imageData, int offset) {
        double gray = 0.21 * (imageData[offset + 2] / 256.) + 0.71 * (imageData[offset + 1] / 256.) + 0.07 * (imageData[offset] / 256.);
        imageData[offset++] = (byte) (gray * 255);
        imageData[offset++] = (byte) (gray * 255);
        imageData[offset] = (byte) (gray * 255);
    }

    private static void grayscalepx(byte[] imageData, int offset, int k) {
        double gray = 0.21 * (imageData[offset + 2] / 256.) + 0.71 * (imageData[offset + 1] / 256.) + 0.07 * (imageData[offset] / 256.);
        imageData[offset++] = (byte) (gray * 255);
        imageData[offset++] = (byte) (gray * 255);
        imageData[offset] = (byte) (gray * 255);
    }

}
