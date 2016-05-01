package com.ap.common.image;

import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public abstract class ImageJob {

    private final IEffect effect;

    public abstract AbstractImageWrapper run(AbstractImageWrapper image, Function<Long[], Void> progressCallBack) throws InterruptedException, ExecutionException;

    public ImageJob(String effect) throws Exception {
        this.effect = EffectProvider.get(effect);
    }

    public IEffect getEffect() {
        return effect;
    }

    public String getName() {
        return effect.getClass().getName();
    }

    public ImageData execute(AbstractImageWrapper image, int x, int y, int width, int height, Function<Long[], Void> progressCallBack) {
        ImageData imageData = new ImageData(FxImageUtils.getImageData(image, x, y, width, height), width, FxImageUtils.bytesInPixel(image));
        getEffect().effect(imageData, progressCallBack);
        return imageData;
    }

}
