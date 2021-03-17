package Scheduling;

import java.util.ArrayList;

/**
 * Used to estimate output value from input variable through linear regression of the historical 
 * and related points. This is done through the algorithms to find a and b to plug into the 
 * equation y = a + bx  to find the correct y val with the correct x input
 * 
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 3/5/2021
 */
public class LinearRegression {
    
    //internal variables for the object, output is always the minutes needed for the input x value
    private ArrayList<Double> given_x_val;
    private ArrayList<Double> minutes_needed;

    /**
     * Used to return the final linear regression estimation and find both a and b values to be able to 
     * calculate the output of the algorithm
     * 
     * @return list of a and then b values
     */
    private double[] find_line() {
        
        //returns the number of values to count
        double n = getGiven_x_val().size();
        
        //finds the sum of all x variables
        double sum_x = sum_var(getGiven_x_val());

        //finds the sum of all y variables
        double sum_y = sum_var(getMinutes_needed());

        //finds the sum of all x*y variables
        double sum_xy = sum_xy(getGiven_x_val(), getMinutes_needed());

        //finds the sum of all x*x variables
        double sum_xx = sum_xy(getGiven_x_val(), getGiven_x_val());

        
        //equations to find the values of a and b based on mean squares equation
        double a = ((sum_y * sum_xx) - (sum_x * sum_xy)) / ((n * sum_xx) - (sum_x * sum_x));
        double b = ((n * sum_xy) - (sum_x * sum_y)) / ((n * sum_xx) - (sum_xx * sum_xx));

        //returns the list of values a and b found
        return new double[]{a, b};
    }

    /**
     * Finds the y value for the given input value of the x value
     * 
     * @param x the metric to find the correlated time needed for
     * @return the result of the equation y = a + bx to return y
     */
    private double find_y(double x){
        
        //gets the two results from internal method
        double[] d = find_line();
        
        //returns the result of the equation, returning the value found as the new y value
        return (d[0] + (x * d[1]));
    }

    /**
     * Public access method for user to input test data and then the given value to estimate based on test value
     * 
     * @param x_input list of historical x values for the metric
     * @param y_input list of historical y values for the time needed in relation to the metric
     * @param given_x the given metric to calculate
     * @return the double result of the linear regression
     */
    public double run(ArrayList<Double> x_input, ArrayList<Double> y_input, double given_x) {
        
        //sets the internal value for the x parameter
        setGiven_x_val(x_input);
        
        //sets the internal variable for the y parameter
        setMinutes_needed(y_input);

        //return the found value
        return find_y(given_x);
    }

    /**
     * finds the sum og the input variable list
     * 
     * @param d list to find the sum of
     * @return the sum of the values within the list
     */
    private double sum_var(ArrayList<Double> d){
        
        //holds final value
        double total = 0;
        
        //loops through the given list
        for(double ds: d){
            
            //adds up the values to the variable
            total += ds;
        }
        
        //returns the final summation total
        return total;
    }

    /**
     * finds the value for the summation of two list values multiplied together
     * 
     * @param d first list to use
     * @param e the second list to use
     * @return the sum of the products of the values in the list
     */
    private double sum_xy(ArrayList<Double> d, ArrayList<Double> e) {
        
        //number to return of final sum
        double total = 0;
        
        //loops through the list, assumption that both lists are of the same size
        for(int i = 0; i < d.size(); i++){
            
            //adds to the sum of the values of the list multiplied together
            total += d.get(i) * e.get(i);
        }
        
        //returns the final sum
        return total;
    }

    /**
     * @return the given list of x values
     */
    private ArrayList<Double> getGiven_x_val() {
        return given_x_val;
    }

    /**
     * @param given_x_val sets the value to the list of given x values
     */
    private void setGiven_x_val(ArrayList<Double> given_x_val) {
        this.given_x_val = given_x_val;
    }

    /**
     * @return the list of minutes previously needed for the matching x value tasks
     */
    private ArrayList<Double> getMinutes_needed() {
        return minutes_needed;
    }

    /**
     * @param minutes_needed sets the value to the list of minutes needed for the values of the x list
     */
    private void setMinutes_needed(ArrayList<Double> minutes_needed) {
        this.minutes_needed = minutes_needed;
    }
}
