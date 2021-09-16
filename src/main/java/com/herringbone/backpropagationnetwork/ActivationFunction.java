package com.herringbone.backpropagationnetwork;

public class ActivationFunction {
    public static float sigmoid(float x) {
        return (float) (1 /  (1 + Math.exp(-x)));
    }

//    public static float dSigmoid(float x) {
//        return sigmoid(x) *  (1 - sigmoid(x));
//    }

    // assume the input x is the already calcuated sigmoid of (x)
    public static float dSigmoid(float x) {
        return x *  (1 - x);
    }
}
