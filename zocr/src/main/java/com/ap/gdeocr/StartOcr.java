package com.ap.gdeocr;

import com.ap.common.model.VectorInjectiveSpace;
import com.ap.nn.core.Network;
import com.ap.ztoys.samples.convolution.ConvolutionalOcr;

public class StartOcr {

    static VectorInjectiveSpace trainSet;
    static Network convolutionalNet;

    public static void main(String[] args) {

        trainSet = ConvolutionalOcr.getSignTrainSet();
        convolutionalNet = ConvolutionalOcr.getNetwork();

        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(OCR.class, args);
            }
        }.start();
    }
}
