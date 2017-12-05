/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNetwork;

import general.OutPut;
import general.Predict;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.temporal.TemporalDataDescription;
import org.encog.ml.data.temporal.TemporalMLDataSet;
import org.encog.ml.data.temporal.TemporalPoint;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/**
 *
 * @author Dr FatBoySlymPG
 */
public class PredictNeuralNetwork implements Predict {
    private static final int WINDOW_SIZE = 7;
    private static final int HIDDEN_LAYER_1 = 7;
//    private static final int HIDDEN_LAYER_2 = 4;
    private static final int FIRST_YEAR = 2008;
    private static final int TRAIN_START = WINDOW_SIZE;
    private static final double MAX_ERROR = 0.0003;
    private static  int trainEnd;
    private int trainingPercent;
    private  int evaluateStart;
    private int evaluateEnd;
    private int dataYear;
    private ArrayList<Double> featalHistory;
    private ArrayList<YearMonth> ymList;
    private ArrayList<Double> evaluateOpen, evaluateClose;
    ArrayList<Double> predictWaste;
    private BasicNetwork network;
    private DateTimeFormatter dtf;
    
    /**
     * 
     * @param featalHistory 
     */
    public PredictNeuralNetwork(ArrayList<Double> featalHistory) {
        this(featalHistory, 70);
    }
    /**
     * 
     * @param featalHistory
     * @param trainingPercent 
     */
    public PredictNeuralNetwork(ArrayList<Double> featalHistory, int trainingPercent) {
        this.featalHistory = featalHistory;
        this.trainingPercent = trainingPercent;   
        predictWaste = new ArrayList(featalHistory);
        dtf = DateTimeFormatter.ofPattern("MMM-yy", Locale.ENGLISH);
        dataYear = FIRST_YEAR;
        
    }
    
    //Create Network
    public BasicNetwork createNetwork(){
        BasicNetwork network = new BasicNetwork();
        network.addLayer(new BasicLayer(WINDOW_SIZE));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, HIDDEN_LAYER_1));
//        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, HIDDEN_LAYER_2));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
        
        network.getStructure().finalizeStructure();
        network.reset(5);
        
        return network;
    }
    
    // generate training sets
    public MLDataSet generateTraining(){
        return generateTraining(trainingPercent);
    }
    private MLDataSet generateTraining(int trainingPercent){
        System.out.println(trainingPercent);
        trainEnd = (int)(((trainingPercent) * featalHistory.size())/100);
        System.out.println(trainEnd);
        evaluateStart = trainEnd + 1;
        
        TemporalMLDataSet result = new TemporalMLDataSet(WINDOW_SIZE, 1);
        
        TemporalDataDescription desc = new TemporalDataDescription(
                TemporalDataDescription.Type.RAW, true, true);
        result.addDescription(desc);
        
        for(int month=TRAIN_START; month < trainEnd; month++){
            if(((month+1)%12)==1){
                dataYear++;
            }
            TemporalPoint point = new TemporalPoint(1);
            point.setSequence(month);
            point.setData(0, featalHistory.get(month));            
            result.getPoints().add(point);
        }
        result.generate();
        return result;
    }
    
    // train neural network
    public void trainNetwork(BasicNetwork network, MLDataSet training) throws IOException{
        this.network = network;
        final Train train = new ResilientPropagation(network, training);
        int epoch = 1;
        OutPut output;

        output = new OutPut("trainingEpoch_Error.csv");
        String out;
        System.out.println("I'm here" + training.size());
        String outHeader = "Epoch" + "," + "Error" + "\n";
        output.writeFile(outHeader);
        do{
//            System.out.println("I'm here" + training.size());
            train.iteration();
            System.out.println("Epoch #" + epoch + " Error:" + train.getError());
//            out = epoch + " " + String.format("%#.2E", train.getError()) + "\n";
            out = epoch + "," + train.getError() + "\n";
            output.writeFile(out);
            epoch++;
        } while(train.getError() >= MAX_ERROR);
        output.closeOutput();
    }
    
    @Override
    public void evaluate(){
        final String DELIMETER = ",";
        OutPut outFile = new OutPut("evaluateNN.csv");
        DecimalFormat df = new DecimalFormat("0.###");
        evaluateOpen = new ArrayList();
        evaluateClose = new ArrayList();
        evaluateEnd = featalHistory.size();
        final String  HEADER = "Month" + DELIMETER + "Actual-Rate" + DELIMETER + "Predicted"
                + "\n";
        String outData;
        System.out.println(HEADER);
        try {
            outFile.writeFile(HEADER);
        } catch (IOException ex) {
            Logger.getLogger(PredictNeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int year=evaluateStart; year<evaluateEnd; year++){
            if(((year+1)%12)==1){
                dataYear++;
            }
            // calculate based on actual data
            MLData input = new BasicMLData(WINDOW_SIZE);
            for(int i=0; i < input.size(); i++){
//                System.out.println("..."  + ((year - WINDOW_SIZE) + i));
                input.setData(i, this.featalHistory.get((year - WINDOW_SIZE) + i));
            }
            MLData output = network.compute(input);
            double prediction = output.getData(0);
            
            System.out.println(((year%12)+1) + " " + dataYear +" "+ this.featalHistory.get(year) +
                    " " + Double.parseDouble(df.format(prediction)));
            
            outData = ((year%12)+1) + DELIMETER + this.featalHistory.get(year) +
                    DELIMETER + prediction + "\n";
            try {
                outFile.writeFile(outData);
            }catch (IOException ex) {
                Logger.getLogger(PredictNeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
            }
            evaluateOpen.add(prediction);            
        }
        try {
            outFile.closeOutput();
        } catch (IOException ex) {
            Logger.getLogger(PredictNeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
   public void predict() {
       OutPut outFile = new OutPut("predictNN.csv");
       final String DELIMETER = ",";
       final int PREDICT_YEARS = 2;
       int monthPredicted = (12 * PREDICT_YEARS) + evaluateEnd;
//        int count = 108;
        String outData;
        MLData input = new BasicMLData(WINDOW_SIZE);
        System.out.println("Prediction starts!!!");
        for(int month = evaluateEnd-WINDOW_SIZE; month < monthPredicted; month++){
            if(((month+1)%12)==1){
                dataYear++;
            }
            for(int i=0; i < input.size(); i++){
                input.setData(i, this.predictWaste.get(month + i));
            }
            MLData output = network.compute(input);
            predictWaste.add(output.getData(0));
//            System.out.println(count + "\t" + predictWaste.get(month));
            outData = "" + Month.of((month%12)+1) + "-" + dataYear + DELIMETER
                    + predictWaste.get(month) + "\n";
            System.out.print(outData);
            
           try {
               outFile.writeFile(outData);
           } catch (IOException ex) {
               Logger.getLogger(PredictNeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
        try {
            outFile.closeOutput();
        } catch (IOException ex) {
            Logger.getLogger(PredictNeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
}
