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


package com.ap.common.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DoubleAccumTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void add() {
        DoubleAccum doubleAccum = new DoubleAccum(0);
        doubleAccum.add(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, doubleAccum.get(), .00001);
    }

    @Test
    public void sum() {
        fail("Not yet implemented");
    }

    @Test
    public void max() {
        fail("Not yet implemented");
    }

    @Test
    public void min() {
        fail("Not yet implemented");
    }
}
