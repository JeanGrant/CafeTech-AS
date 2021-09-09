package com.example.cafetech;

import android.app.Activity;
import android.graphics.Bitmap;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.image.ops.Rot90Op;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageClassifier {
    private static final float PROBABILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 255.0f;
    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    private TensorImage inputImageBuffer;
    private final TensorBuffer probabilityImageBuffer;
    private final TensorProcessor probabilityProcessor;
    public List<String> labels;
    private final Interpreter tensorClassifier;
    private final int imageResizeX;
    private final int imageResizeY;

    public ImageClassifier(Activity activity) throws IOException {
        //loading the model
        MappedByteBuffer classifierModel = FileUtil.loadMappedFile(activity, "mobilenet_v1_1.0_224_quant.tflite");
        labels = FileUtil.loadLabels(activity, "labels.txt");

        tensorClassifier = new Interpreter(classifierModel, null);

        //input
        int imageTensorIndex = 0;
        int[] inputImageShape = tensorClassifier.getInputTensor(imageTensorIndex).shape();
        DataType inputDataType = tensorClassifier.getInputTensor(imageTensorIndex).dataType();

        //output
        int probabilityTensorIndex = 0;
        int[] outputImageShape = tensorClassifier.getOutputTensor(probabilityTensorIndex).shape();
        DataType outputDataType = tensorClassifier.getOutputTensor(probabilityTensorIndex).dataType();

        imageResizeX = inputImageShape[1];
        imageResizeY = inputImageShape[2];

        inputImageBuffer = new TensorImage(inputDataType);
        probabilityImageBuffer = TensorBuffer.createFixedSize(outputImageShape, outputDataType);

        probabilityProcessor = new TensorProcessor.Builder().add(new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD)).build();
    }

    public List<Recognition> recognizeImage(final Bitmap bitmap, final int sensorOrientation){
        List<Recognition> recognitions = new ArrayList<>();
        inputImageBuffer = loadImage(bitmap, sensorOrientation);
        tensorClassifier.run(inputImageBuffer.getBuffer(), probabilityImageBuffer.getBuffer().rewind());
        Map<String, Float> labelledProbability = new TensorLabel(labels, probabilityProcessor.process(probabilityImageBuffer)).getMapWithFloatValue();
        for (Map.Entry<String, Float> entry : labelledProbability.entrySet()){
            recognitions.add(new Recognition(entry.getKey(), entry.getValue()));
        }
        return recognitions;
    }

    //pre-processing the image (currently altered.. see vid if problems occur)
    private TensorImage loadImage(Bitmap bitmap, int sensorOrientation) {
        inputImageBuffer.load(bitmap);
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        ImageProcessor imageProcessor = new ImageProcessor.Builder()
                .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                .add(new ResizeOp(imageResizeX, imageResizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                .add(new Rot90Op(sensorOrientation))
                .add(new NormalizeOp(IMAGE_MEAN, IMAGE_STD))
                .build();
        return imageProcessor.process(inputImageBuffer);
    }

    static class Recognition implements Comparable {
        private String name;
        private final float confidence;

        public Recognition(String name, float confidence) {
            this.name = name;
            this.confidence = confidence;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getConfidence() {
            return confidence;
        }

        @Override
        public int compareTo(Object o) {
            return Float.compare(((Recognition)o).confidence, this.confidence);
        }
    }
}
