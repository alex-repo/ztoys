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

public class FOrtoCube {

    float[] min = new float[]{0, 0, 0};
    float[] max = new float[]{1, 1, 1};

    public FOrtoCube(float[] min, float[] max) {
        System.arraycopy(min, 0, this.min, 0, 3);
        System.arraycopy(max, 0, this.max, 0, 3);
    }

    public FOrtoCube() {
    }

    public boolean isBelong(float[] point) {
        return point[0] >= min[0] && point[0] <= max[0] &&
                point[1] >= min[1] && point[1] <= max[1] &&
                point[2] >= min[2] && point[2] <= max[2];
    }

}
