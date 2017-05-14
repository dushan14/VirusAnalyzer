/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virusanalyzer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


/**
 *
 * @author Dushan
 */
public class ReportGenerator {

    Analyzer analyzer;

    String report = "";
    String reportName = "Report" + System.currentTimeMillis() + ".txt";

    public ReportGenerator(File file) throws IOException {
        analyzer = new Analyzer(file);
    }

    public String getStringReport() throws IOException {

        VirusDetails vd = analyzer.getDetails();
        add("File name\t", analyzer.getName());
        add("MD5\t\t", analyzer.md5);
        System.out.println(vd.tID);
        if (vd.vxVirusName == null) {
            //not a virus
            add("Status\t\t", "Not a threat");
        } else {
            add("Status\t\t", "Threat");
            add("Type\t\t", vd.vxType);
            add("SHA1\t\t", vd.vxSHA1);
            add("SHA256\t\t", vd.vxSHA256);
            add("Awareness\t", Integer.toString(vd.vxAwareness));
        }
        add("Hash\t\t", Integer.toString(analyzer.getHash()));
        return report;

    }

    public void saveReortAndOpen() {
        if (report != "") {
            try {

                //write into file
                PrintWriter writer = new PrintWriter(reportName, "UTF-8");
                writer.println(report);
                writer.close();

                //open the file
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().edit(new File(reportName));
                } else {

                }
            } catch (IOException e) {

            }
        } else {
            System.out.println("Nothing to write...");
        }
    }

    private void add(String key, String info) {
        report += key + ": " + info + "\r\n";
    }

}
