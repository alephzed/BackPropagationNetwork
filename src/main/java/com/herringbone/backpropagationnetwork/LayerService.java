package com.herringbone.backpropagationnetwork;

import com.herringbone.backpropagationnetwork.model.Layer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class LayerService {

    public List<Float> run(List<Float> inputArray, Layer layer) {
        System.arraycopy(inputArray.toArray(), 0, layer.getInput(), 0, inputArray.size());
        layer.getInput()[layer.getInput().length-1] = 1f;
        int offset = 0;

        for (int i=0; i< layer.getOutput().length; i++) {
            for (int j=0; j < layer.getInput().length; j++) {
                layer.getOutput()[i] += layer.getWeights()[offset + j] * layer.getInput()[j];
            }
            layer.getOutput()[i] = ActivationFunction.sigmoid(layer.getOutput()[i]);
            offset += layer.getInput().length;
        }
        return Arrays.asList(layer.getOutput());
    }

    public List<Float> train(List<Float> error, float learningRate, float momentum, Layer layer) {
        int offset = 0;
        List<Float> nextError = new ArrayList<>(Collections.nCopies(layer.getInput().length, 0F));

        for (int i=0; i < layer.getOutput().length; i++) {
            float delta = error.get(i) * ActivationFunction.dSigmoid(layer.getOutput()[i]);
            for (int j=0; j<layer.getInput().length;j++) {
                int weightIndex = offset + j;

                nextError.set(j, nextError.get(j) + layer.getWeights()[weightIndex] * delta);
                float dw = layer.getInput()[j] * delta * learningRate;
                layer.getWeights()[weightIndex] += layer.getDWeights()[weightIndex] * momentum + dw;
                layer.getDWeights()[weightIndex] = dw;
            }
            offset += layer.getInput().length;
        }
        return nextError;
    }
}
