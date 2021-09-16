package com.herringbone.backpropagationnetwork;

import com.herringbone.backpropagationnetwork.model.DataSet;
import com.herringbone.backpropagationnetwork.model.Layer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainingService {

    private final BackPropagationNetwork backPropagationNetwork;

    @Value("${network.iterations}") private int iterations;

    @Value("${network.learning-rate}") private float learningRate;
    @Value("${network.momentum}") private float momentum;

    public TrainingService(BackPropagationNetwork backPropagationNetwork) {
        this.backPropagationNetwork = backPropagationNetwork;
    }

    public void trainingStep(DataSet trainingDataSet, List<Layer> layers) {
        for (int iteration=0; iteration < this.iterations; iteration++) {
            trainingDataSet.getTraining().forEach(s ->
                    backPropagationNetwork.train(s.input, s.result,
                            this.learningRate, this.momentum, layers));
            if ((iteration + 1) % 100 == 0) {
                log.info("Iteration #" + iteration);
                trainingDataSet.getTraining().forEach(s -> {
                    Float[] t = s.input.toArray(Float[]::new);
                    StringBuilder logMessage = new StringBuilder();
                    s.input.forEach( input -> {
                        logMessage.append(input);
                        logMessage.append(", ");
                    });
                    logMessage.append(" --> ");
                    List<Float> layerOutput = backPropagationNetwork.run(s.input, layers);
                    layerOutput.forEach( output -> {
                        logMessage.append(Math.round(output));
                        logMessage.append(", ");
                    });
                    log.info(logMessage.toString());
                });
            }
        }

        // Get the weights
        log.info("Print the final weights ");
        layers.forEach( s -> Arrays.stream(s.getWeights()).sequential().forEach(t -> log.info("{} -> Weight of {}", s.getDescription(), t)));
    }

    public void trainingTest(DataSet runningDataSet, List<Layer> layers) {
        final AtomicInteger correct = new AtomicInteger();

        runningDataSet.getTesting().forEach(s -> {
            List<Float> results = backPropagationNetwork.run(s.input, layers);
            List<Integer> roundedResults = results.stream().map(Math::round).collect(Collectors.toList());
            List<Integer> expectedResuts = s.getResult().stream().map(Math::round).collect(Collectors.toList());
            boolean isEqual = roundedResults.equals(expectedResuts);
            if (isEqual) {
                correct.incrementAndGet();
            }
        });
        log.info("Total correct {} out of {}", correct.get(), runningDataSet.getTesting().size());
    }
}
