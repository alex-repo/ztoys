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

public class Convolution implements IEffect {

    public static byte[] convolutionKernel(byte[] source) {
        byte[] kernal = new byte[]{-1, -1, -1, -1, 8, -1, -1, -1, -1};
        if (source.length != 9 * 4) {
            return null;
        }
        double[] resultData = new double[]{0., 0., 0., 0.};
        for (int i = 0; i < 9; i++) {
            resultData[0] += source[i * 4] / 256. * kernal[i];
            resultData[1] += source[i * 4 + 1] / 256. * kernal[i];
            resultData[2] += source[i * 4 + 3] / 256. * kernal[i];
        }
        byte[] result = new byte[4];
        result[0] = (byte) (resultData[0] * 255);
        result[1] = (byte) (resultData[1] * 255);
        result[2] = (byte) (resultData[2] * 255);
        result[3] = -1;

        return result;
    }

    @Override
    public void effect(ImageData imageData, Function<Long[], Void> progressCallBack) {
        int bytesInPixel = imageData.getBytesInPixel();
        int scanlineStride = imageData.getWidth() * bytesInPixel;
        byte[] data = imageData.getImageData();
        int xr = scanlineStride - (2 * bytesInPixel);
        int yr = (data.length / scanlineStride - 2);
        byte[] tmpdata = new byte[xr * yr];
        for (int iy = 0; iy < yr; iy++) {
            for (int ix = 0; ix < xr; ix += bytesInPixel) {
                byte[] source = new byte[9 * bytesInPixel];
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < bytesInPixel * 3; i++) {
                        source[j * bytesInPixel * 3 + i] = data[iy * scanlineStride + (j * scanlineStride) + ix + i];
                    }
                }
                byte[] result = convolutionKernel(source);
                for (int i = 0; i < bytesInPixel; i++) {
                    tmpdata[iy * xr + ix + i] = result[i];
                }
            }
            FxImageUtils.reportProgress(progressCallBack, iy * scanlineStride + scanlineStride * bytesInPixel, data.length);
        }
        imageData.setImageData(tmpdata);
        imageData.setWidth(xr / bytesInPixel);

    }

}
