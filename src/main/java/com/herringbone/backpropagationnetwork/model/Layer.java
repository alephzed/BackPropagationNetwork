package com.herringbone.backpropagationnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.util.Arrays;
import java.util.Random;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Layer implements Comparable<Layer>{

    private  Float[] output;
    private  Float[] input;
    private  Float[] weights;
    private  Float[] dWeights;
    @Transient
    private  Random random;
    private  String description;
    private  Integer levelId;

    public Layer(int inputSize, int outputSize, String description, int levelId) {
        this.output = new Float[outputSize];
        this.input = new Float[inputSize+1]; // +1 additional Neuron for the bias node
        this.weights = new Float[(inputSize+1) * outputSize]; // +1 additional Neuron for the bias node
        this.dWeights = new Float[weights.length];
        this.random = new Random();
        this.description = description;
        this.levelId = levelId;

        initWeights();
        initInputs();
        initOutputs();
        initDWeights();
    }

    private void initWeights() {
        for (int i=0; i < weights.length; i++) {
            weights[i] = (random.nextFloat()-0.5f) * 4f; // generate edge weights in the range [-2,2]
        }
    }

    private void initInputs() {
        Arrays.fill(input, 0f);
    }

    private void initOutputs() {
        Arrays.fill(output, 0f);
    }

    private void initDWeights() {
        Arrays.fill(dWeights, 0f);
    }


    @Override
    public int compareTo(Layer layer) {
        return layer.getLevelId() > this.getLevelId() ? 1 : 0;
    }
}
