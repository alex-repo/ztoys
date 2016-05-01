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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

public class EffectProvider {

    private static final ConcurrentHashMap<String, IEffect> effectsMap = new ConcurrentHashMap<>();

    static IEffect register(String className) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        IEffect effect = null;
        if (!effectsMap.contains(className)) {
            synchronized (effectsMap) {
                if (!effectsMap.contains(className)) {
                    Class<?> clazz = Class.forName(className);
                    Constructor constructor = clazz.getConstructor(new Class[]{});
                    effect = (IEffect) constructor.newInstance();
                    effectsMap.put(className, effect);
                }
            }
        }
        return effect;
    }

    static public IEffect get(String className) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (effectsMap.contains(className) || (register(className) instanceof IEffect)) {
            return effectsMap.get(className);
        } else {
            return null;
        }
    }
}
