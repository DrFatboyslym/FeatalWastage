/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.encog.util.arrayutil.NormalizeArray;

/**
 *
 * @author Dr FatBoySlymPG
 */
public class Data {
    private static String filepath;
    private static ArrayList<FeatalHistory> featalList;
    private static ArrayList<Double> wasteRateHistory;
    private static ArrayList<Double> wasteRateHistoryNorm;
    private static BufferedReader fileReader;
    private static double featalWasteRate;
    
    private static NormalizeArray norm;
    private static double[] wasteRateHistoryNormArray;
    
    private static final String DELIMETER = ",";
    private static final int MONTH_INDEX = 0;
    private static final int TOTAL_FEMALE_INDEX = 2;
    private static final int FEATAL_WASTE_INDEX = 3;
    private static final int WASTE_RATE_INDEX = 4;

    public Data(String filepath, double lo, double hi){
        Data.filepath = filepath;
        readFile(lo, hi);
    }
    
    public Data (double lo, double hi){
        this("data/PreparedDataAll.csv", lo, hi);
    }
    
    public Data(){
        this(0.1, 0.9);
    }
    
    private static void readFile(double lo, double hi){
        wasteRateHistory = new ArrayList();
        featalList = new ArrayList<>();
        String mth;
        int totFemale;
        int totWaste;
        double wasteRate;
        //Read in colunm 3 of featal file :-- Done
        try{
           fileReader = new BufferedReader(new FileReader(Data.filepath));
           String line = "";
           
            //Read the CSV file header to skip it
            fileReader.readLine();
            fileReader.readLine();
            
            while((line = fileReader.readLine()) != null){
                String[] tokens = line.split(Data.DELIMETER);
                mth = tokens[MONTH_INDEX];
                totFemale = Integer.parseInt(tokens[TOTAL_FEMALE_INDEX]);
                totWaste = Integer.parseInt(tokens[FEATAL_WASTE_INDEX]);
                wasteRate = Double.parseDouble(tokens[WASTE_RATE_INDEX]);
                FeatalHistory featalHistory = 
                        new FeatalHistory(mth, totFemale, totWaste, wasteRate);
                
//               System.out.println(featalHistory.getFeatalWasteRate()); 
               featalList.add(featalHistory);
               wasteRateHistory.add(featalHistory.getFeatalWasteRate()/100);
//               System.out.println(featalHistory.getFeatalWasteRate()/100);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
//        normalizeData(lo, hi);
    }
    
    public static ArrayList<Double> getRawData(){
        return wasteRateHistory;
    }
    
    public static ArrayList<?extends Number> getNormalizedData(){
        return wasteRateHistoryNorm;
    }
    
    public static void plotGraph(){
        
    }
}

class FeatalHistory{
    private int totalFemale, totalFeatalWaste; 
    private double featalWasteRate;
    private String month;
    public FeatalHistory(String month, int totalFemale, int totalFeatalWaste, 
            double featalWasteRate){
        DecimalFormat df = new DecimalFormat("0.##");
        this.month = month.substring(0, 2);
        this.totalFemale = totalFemale;
        this.totalFeatalWaste = totalFeatalWaste;
        this.featalWasteRate = Double.parseDouble(df.format(featalWasteRate));           
    }
        

    public FeatalHistory(String month, int totalFemale, int totalFeatalWaste){
        this(month, totalFemale, totalFeatalWaste, (totalFeatalWaste / totalFemale));
    }
        
    public int getTotalFemale(){
        return this.totalFemale;
    }
        
    public int getTotalFeatalWaste(){
        return this.totalFeatalWaste;
    }
    public double getFeatalWasteRate(){
        return this.featalWasteRate;
    }
}
