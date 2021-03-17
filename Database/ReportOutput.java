package Database;

import FormFiles.ErrorWindow;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The purpose of the class is to produce the output for the report function of the program to allow for the creation
 * of .csv files for the user to be able to use in other functions and to study for business analytics
 *
 * @author Lucas Hynes
 * @version 1.0.1
 * @since 2/28/2021
 */
public class ReportOutput {

    //file name to save (user input)
    private final String fileName;

    //variable for the writing
    private FileWriter csvWriter;

    /**
     * Constructs the object to set the writer to the appropriate file
     *
     * @param fileName name of file to write to
     */
    public ReportOutput(String fileName){

        //sets internal string to variable
        this.fileName = fileName;


        //attempts to set the writer with the given filename
        try {
            setCsvWriter(fileName);

        } catch (IOException e) {

            //displays errors encountered with details
            new ErrorWindow(new Stage(), String.valueOf(e.toString()), e.getMessage());
        }

    }

    /**
     * Used to add data to a csv and close the csv after writing. The data is a table that is the result of a user
     * based query so none of the aspects for the table are known before the method is called, so the solution is built
     * to be dynamic and to handle the various input scenarios
     *
     * @param col string list of column names
     * @param data 2d list of data to be within the csv
     * @throws IOException handles invalid data input
     */
    public void setFileData(ArrayList<String> col, ArrayList<ArrayList<String>> data) throws IOException {

        //returns the file writer
        FileWriter fw = getCsvWriter();

        //goes through the columns, checking for the last entry to be able to know when to insert the \n character to
        //end the line of the table
        for(String s: col) {

            fw.append(s);

            if(col.size() > col.indexOf(s) + 1){

                //used if not the last entry
                fw.append(",");

            } else {

                //used for last entry
                fw.append("\n");
            }
        }

        //goes through the first list for all the rows returned of the stored data
        for(ArrayList<String> rowData: data) {

            int count = 0;

            //goes through values within the given row, adding the values and proper following characters based on
            //the variables location in the list
            for(String val: rowData) {

                //adds the value
                fw.append(val);

                if (rowData.size() > count + 1) {

                    //used for all entries except the last
                    fw.append(",");

                } else {

                    //used for the last entry in the row
                    fw.append("\n");
                }

                //increments the count
                count ++;
            }
        }


        //completes the write and closes the file
        fw.flush();
        fw.close();
    }

    /**
     * @return the string value of the filename
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return the csv writer object
     */
    public FileWriter getCsvWriter() {
        return csvWriter;
    }

    /**
     * sets the csv object  for the class
     *
     * @param filename the filename for the csv
     * @throws IOException handles invalid data
     */
    public void setCsvWriter(String filename) throws IOException {
        this.csvWriter = new FileWriter(filename);
    }
}
