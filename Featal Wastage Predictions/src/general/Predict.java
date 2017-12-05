/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import java.util.ArrayList;

/**
 *
 * @author Dr FatBoySlymPG
 */
//TODO Make abstract and implement plot methods
public interface Predict {
    public abstract void evaluate();
    //TODO: implement public abstract void evaluatePlot();
    public abstract void predict();
    //TODO: implement public abstract void predictPlot();
}
