package com.herringbone.backpropagationnetwork;

import com.herringbone.backpropagationnetwork.model.Layer;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Getter
public class BackPropagationNetwork {

    private final LayerService layerService;

    public BackPropagationNetwork(LayerService layerService) {
        this.layerService = layerService;
    }

    public List<Float> run(List<Float> input, List<Layer> layers) {
        List<Float> inputActivations = input;
        for (Layer layer : layers) {
            inputActivations = layerService.run(inputActivations, layer);
        }

        return inputActivations;
    }

    public void train(List<Float> input, List<Float> targetOutput, float learningRate,
                      float momentum, List<Layer> layers) {
        List<Float> calculatedOutput = run(input, layers);
        List<Float> error = new ArrayList<>(Collections.nCopies(calculatedOutput.size(), 0F));
        for (int i=0; i < error.size(); i++) {
            error.set(i, targetOutput.get(i) - calculatedOutput.get(i));
        }

        for (int i = layers.size() -1; i >= 0; i--) {
            error = layerService.train(error, learningRate, momentum, layers.get(i));
        }
    }
}
