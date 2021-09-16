package com.herringbone.backpropagationnetwork;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.herringbone.backpropagationnetwork.model.DataSet;
import com.herringbone.backpropagationnetwork.model.Layer;
import com.herringbone.backpropagationnetwork.model.Network;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Slf4j
public class TrainingRunner  implements CommandLineRunner {

    private final TrainingService trainingService;
    private final NetworkRepository networkRepository;
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    @Value("${network.input-size}") int inputSize;
    @Value("${network.hidden-size}") int hiddenSize;
    @Value("${network.output-size}") int outputSize;


    TrainingRunner(TrainingService trainingService, NetworkRepository networkRepository) {
        this.trainingService = trainingService;
        this.networkRepository = networkRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        String fileName = "OrTraining.json";
        DataSet dataSet = getDataSet(fileName);
        List<Layer> layers = List.of(
                new Layer(inputSize, hiddenSize, "Input to Hidden Layer", 0),
                new Layer(hiddenSize, outputSize, "Hidden to Output Layer", 1)
        );
//        trainingService.trainingStep(dataSet, layers);

        List<Layer> layers2 = List.of(
                new Layer(inputSize, hiddenSize, "Input to Hidden Layer", 0),
                new Layer(hiddenSize, outputSize, "Hidden to Output Layer", 1)
        );
        //Reset network and
        fileName = "AndTraining.json";
        dataSet = getDataSet(fileName);
//        trainingService.trainingStep(dataSet, layers2);

        //Rest network and run for IrisData

        fileName = "iris.json";
        dataSet = getDataSet(fileName);
        List<Layer> layers3 = List.of(
                new Layer(dataSet.getNetwork().getInput(), dataSet.getNetwork().getHidden().get(0),
                        "Input to Hidden Layer", 0),
                new Layer(dataSet.getNetwork().getHidden().get(0), dataSet.getNetwork().getOutput(), "Hidden to Output Layer", 1)
        );
        trainingService.trainingStep(dataSet, layers3);
        trainingService.trainingTest(dataSet, layers3);

        Network network = new Network();
        network.setLayer(layers2);
        network.setType("BackPropagation");
        Network network2 = new Network();
        network2.setLayer(layers);
        network2.setType("BackPropagation");
        Network network3 = new Network();
        network3.setLayer(layers3);
        network3.setType("BackPropagation");
        List networks = List.of(network, network2, network3);
        networkRepository.deleteAll()
                .thenMany(
                        Flux.just(network, network2, network3)
                                .flatMap(networkRepository::save)
                ).thenMany(networkRepository.findAll())
                .subscribe(s -> System.out.println("saving " + s.toString()));
    }

    private DataSet getDataSet(String fileName) throws IOException {
        InputStream ip = getFileFromResourceAsStream(fileName);
        String jsonData = convertInputStreamToString(ip);
        DataSet dataSet  = new ObjectMapper().readValue(jsonData, DataSet.class);
        log.info("Root descriptor: " + dataSet.getDescriptor());
        return dataSet;
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    private String convertInputStreamToString(InputStream is) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8);
    }
}
