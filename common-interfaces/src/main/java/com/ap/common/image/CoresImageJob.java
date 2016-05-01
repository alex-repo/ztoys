package com.ap.common.image;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

public class CoresImageJob extends ImageJob {

    public CoresImageJob(String name) throws Exception {
        super(name);
    }

    public class CallableTask implements Callable<ImageData> {

        private Function<Long[], Void> progressCallBack;
        private int width;
        private int height;
        private int x;
        private int y;
        private AbstractImageWrapper image;

        private CallableTask(AbstractImageWrapper image, int x, int y, int width, int height, Function<Long[], Void> progressCallBack) {
            this.progressCallBack = progressCallBack;
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;
            this.image = image;
        }

        @Override
        public ImageData call() throws Exception {
            return execute(image, x, y, width, height, progressCallBack);
        }
    }

    @Override
    public AbstractImageWrapper run(AbstractImageWrapper image, Function<Long[], Void> progressCallBack) throws InterruptedException, ExecutionException {
        int cores = 4;
        int dimpart = (int) Math.sqrt(cores);
        int width = (int) image.getWidth();
        int pwidth = width / dimpart;
        int wremain = width % dimpart;
        int height = (int) image.getHeight();
        int pheight = height / dimpart;
        int hremain = height % dimpart;

        ExecutorService executorService = Executors.newFixedThreadPool(cores);
        Map<ImagePartPosition, Future<ImageData>> taskResults = new HashMap<>();

        for (int ix = 0; ix < dimpart; ix++) {
            for (int iy = 0; iy < dimpart; iy++) {
                ImagePartPosition partPositon = new ImagePartPosition(ix * pwidth, iy * pheight, pwidth + wremain * ((ix == dimpart - 1) ? 1 : 0), pheight
                        + hremain * ((iy == dimpart - 1) ? 1 : 0));
                taskResults.put(partPositon, executorService.submit(new CallableTask(image, partPositon.x, partPositon.y,
                        partPositon.width, partPositon.height, progressCallBack)));
            }
        }

        AbstractImageWrapper wImage = new AbstractImageWrapper(image.asFX());

        for (Map.Entry<ImagePartPosition, Future<ImageData>> entrySet : taskResults.entrySet()) {
            ImagePartPosition key = entrySet.getKey();
            Future<ImageData> value = entrySet.getValue();
            ImageData result = value.get();
            wImage.setPixels(key.x, key.y, result);
        }
        return wImage;
    }
}
