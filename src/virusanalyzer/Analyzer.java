/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusanalyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import virusanalyzer.database.DBClass;

/**
 *
 * @author Dushan
 */
public class Analyzer {

    public static File selectedFile;
    public static String md5;
    public static VirusDetails details;

    public Analyzer(File file) throws IOException {

        selectedFile = file;
        md5=getMD5();
       

    }

    public String getName() {
        return selectedFile.getName();
    }
  public int getHash() {
        return selectedFile.hashCode();
    }
  
    
    private String getMD5() throws FileNotFoundException, IOException {
        String md5;
        try (FileInputStream fis = new FileInputStream(selectedFile)) {
            md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
        }
        return md5;
    }

    
    public VirusDetails getDetails() throws IOException{   
        System.out.println("getting details..");
        details=DBClass.readFromDatabase(md5);
        return details;
    }
    
    
}
