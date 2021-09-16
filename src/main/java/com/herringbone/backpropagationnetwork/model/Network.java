package com.herringbone.backpropagationnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Network {
    String type;
    String name;
    List<String> featureMap;
    List<Layer> layer;

}
