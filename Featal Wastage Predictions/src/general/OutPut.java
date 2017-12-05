/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dr FatBoySlymPG
 */
public class OutPut {
    private String LOC = "data/Output/";
    private StringBuilder file;
    private File outFile;
    private FileWriter writer;
    
    public OutPut(String fileName){        
        try {
            writeFile(fileName, true);
        } catch (IOException ex) {
            Logger.getLogger(OutPut.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeFile(String fileName, boolean append) throws IOException {
        this.file = new StringBuilder(LOC);
        file.append(fileName);
        
        outFile = new File(file.toString());
        if(!outFile.exists()){
            outFile.createNewFile();        
            writer = new FileWriter(outFile, append);
        }else{
            outFile.delete();
            outFile.createNewFile();        
            writer = new FileWriter(outFile, append);
        }
        
    }
    public void writeFile(String data) throws IOException{
        writer.write(data);
        writer.flush();
    }
    
    public void closeOutput() throws IOException{
        writer.close();
    }
    
}
