package com.herringbone.backpropagationnetwork.model;

import lombok.Getter;

import java.util.List;

@Getter
public class DataSet {
    public String descriptor;
    public List<String> featureMap;
    public List<Classification> classifierMap;
    public List<Training> training; //should be 70% of data
    public List<Testing> testing; // should be 30% of data
    public NetworkParameters network;
}
