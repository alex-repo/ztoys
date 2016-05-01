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

public interface IImage {

    public int getSize();

    public Integer getLabel();

    public int getPixel(int x, int y);

    public void setPixel(int x, int y, int color);

    public int[] getPixels(int offset, int stride, int x, int y, int width, int height);

    public void setPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height);

    public IImage resize(int width, int height);

    public IImage crop(int x1, int y1, int x2, int y2);

    public int getType();

    public double getWidth();

    public double getHeight();

    public byte[] getBuffer(double lastX, double lastY, int width, int height);

    public double[] asGrayDoubleArray();

}
