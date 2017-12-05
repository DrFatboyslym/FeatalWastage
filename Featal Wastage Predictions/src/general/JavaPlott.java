/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import java.util.ArrayList;

/**
 *
 * @author Dr FatBoySlymPG
 */
public class JavaPlott {
    double[][] data1;
    public JavaPlott(String xLabel, String yLabel, ArrayList<? extends Number> arrlist){
        recastData(arrlist);
        
    }

    JavaPlott(ArrayList<? extends Number> arrlist) {
        recastData(arrlist);
    }

    private void recastData(ArrayList<? extends Number> arrlist) {
        data1 = new double[2][arrlist.size()];
        int n;
        for(int i = 0; i < arrlist.size(); i++){
            data1[0][i] = i;
            data1[1][i] = Double.parseDouble(arrlist.get(i).toString());
            
            System.out.println(data1[0][i] + ":::" + data1[1][i]);
        }
        
        plot(data1);
    }
    
    public void plot(double[][] array2d){
        AbstractPlot data2 = new DataSetPlot(array2d);
        
        PlotStyle myPlotStyle = new PlotStyle();
        myPlotStyle.setStyle(Style.LINESPOINTS);
        myPlotStyle.setLineWidth(1);
        
        data2.setPlotStyle(myPlotStyle);
        data2.setTitle("Trial");
        JavaPlot plotter = new JavaPlot();
        plotter.setTitle("Plot Data2");
        
//        plotter.addPlot(data2);
    
        plotter.set("xlabel", "'Waste Rate'");
        plotter.set("ylabel", "'Time'");
        plotter.plot();
    }
}
