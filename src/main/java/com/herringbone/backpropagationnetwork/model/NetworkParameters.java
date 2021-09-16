package com.herringbone.backpropagationnetwork.model;

import lombok.Getter;

import java.util.List;

@Getter
public class NetworkParameters {
    public Long iterations;
    public Integer input;
    public List<Integer> hidden;
    public Integer output;
}
