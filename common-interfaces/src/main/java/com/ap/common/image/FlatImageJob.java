package com.ap.common.image;

import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class FlatImageJob extends ImageJob {

    public FlatImageJob(String name) throws Exception {
        super(name);
    }

    @Override
    public AbstractImageWrapper run(AbstractImageWrapper image, Function<Long[], Void> progressCallBack) throws InterruptedException, ExecutionException {
        ImageData imageData = execute(image, 0, 0, (int) image.getWidth(), (int) image.getHeight(), progressCallBack);
        return FxImageUtils.writeImage(image, imageData.getImageData(), 0, 0, imageData.getWidth(), imageData.getImageData().length / (imageData.getWidth() * imageData.getBytesInPixel()));
    }

}
