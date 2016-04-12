package org.glyme.cmcc;

import Jama.Matrix;

/**
 * Created by dell on 2016/4/12.
 */
public class NNDetector {
    public static int rgb2bw(int r, int g, int b) {
        return (r * 0.299 + g * 0.587 + b * 0.144) > 125 ? 255 : 0;
    }
}
