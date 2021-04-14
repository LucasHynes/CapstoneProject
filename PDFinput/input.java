package PDFinput;

import java.io.File;
import java.io.IOException;

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

        /*
         * Loop through the page data returned by page, adding
         * the orders to the database through new Order() calls 
        */    
    }

     public static void main(String[] args){
         inputPdf("");
     }
}
