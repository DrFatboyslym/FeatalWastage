/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import NeuralNetwork.PredictNeuralNetwork;
import java.io.IOException;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;

/**
 *
 * @author Dr FatBoySlymPG
 */
public class Main{
    public static void main(String[] args) throws IOException{
        launch();
    }

//    @Override
    public static void launch() throws IOException{
        Data data = new Data();  
        
        for(int i = 0; i < Data.getRawData().size(); i++){
            System.out.println(Data.getRawData().get(i));
        }
        
        PredictNeuralNetwork ann = new PredictNeuralNetwork(Data.getRawData(), 80);
        BasicNetwork annNetwork = ann.createNetwork();
        MLDataSet train = ann.generateTraining();
        ann.trainNetwork(annNetwork,train);
        ann.evaluate();
        ann.predict();
        
    }
}
