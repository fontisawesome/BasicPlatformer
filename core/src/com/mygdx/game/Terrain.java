package com.mygdx.game;

import java.util.ArrayList;

public class Terrain {
    int res = 32;
    int smoothness = 4;
    double[] heights = new double[800 / smoothness / res + 1];
    double[] uheights = new double[800 / res + 1];
    String[][] ores = new String[800 / res][800 / res];
    int sx = 0;
    int sy = 0;
    int sxp = 0;
    int syp = 0;
    int syp1 = 0;
    int v = 200;
    int o = 0;
    int prevsquare = 0;
    int lineheight = 0;

    public static double interpolate(double height1, double height2, double ratio) {
        double ft = ratio * Math.PI;
        double f = (1 - Math.cos(ft)) * 0.5;
        return height1 * (1 - f) + height2 * f;
    }

    public void init() {
        for (int i = 0; i < heights.length; i++) {
            heights[i] = Math.random();
        }
        for (int i = 0; i < heights.length - 1; i++) {
            uheights[i * smoothness] = ((int) (heights[i] * v) / res) * res + o;
            for (int j = (800 / res) - 1; j > ((int) (heights[i] * v) / res) + o / res; j--) {
                ores[j][i * smoothness] = "D";
            }
            for (int j = 1; j < smoothness; j++) {
                uheights[i * smoothness + j] = ((int) (interpolate(heights[i], heights[i + 1], (1.0 / smoothness) * j) * v) / res) * res + o;
                for (int jj = (800 / res) - 1; jj > ((int) (interpolate(heights[i], heights[i + 1], (1.0 / smoothness) * j) * v) / res) + o / res; jj--) {
                    ores[jj][i * smoothness + j] = "D";
                }
            }
        }
    }
    public Terrain(){
        init();
    }
}
