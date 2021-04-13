package PDFinput;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class input {
     public static void inputPdf(String filename) {

        PDDocument pdf = null;
         try {
            pdf = Loader.loadPDF(new File(filename));
        } catch (IOException e) {
            pdf = null;
            System.out.println(e.getMessage());
        }

        PDPage p = pdf.getPage(1);

        try {
            System.out.println(p.getContents());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
     }

     public static void main(String[] args){
         inputPdf("C:\\Users\\firef\\Downloads");
     }
}
