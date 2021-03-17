package Scheduling;

/**
 * The purpose of this class is to find the distance and direction of two entities based on the values for
 * the two entities longitude and latitude which are then converted to miles to be able to tell the distance
 * and then are analyzed with trig functions to find the direct needed to travel from the fist entity to the
 * second
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 3/1/2021
 */
public class DirectionDistance {

    //internal variable declaration to hold the values for the locations of the two entities
    private final double longitude_1;
    private final double latitude_1;
    private final double longitude_2;
    private final double latitude_2;

    /**
     * This is the constructor for the distance and direction calculations, which takes the coordinates
     * for the two locations, with the starting location entered first and the end location entered second
     *
     * @param longitude_1 the longitude value for the first entity
     * @param latitude_1 the latitude value for the first entity
     * @param longitude_2 the longitude value for the second entity
     * @param latitude_2 the latitude value for the second entity
     */
    public DirectionDistance(double longitude_1, double latitude_1, double longitude_2, double latitude_2) {

        //assigns the passed in parameters to the values of the internal variables and converting to miles
        /*
            Calculations for the ratio of the units to the miles was done by:
            https://www.usgs.gov/faqs/how-much-distance-does-a-degree-minute-and-second-cover-your-maps?qt-news_science_products=0#qt-news_science_products
         */
        this.longitude_1 = longitude_1 * 54.6;
        this.latitude_1 = latitude_1 * 69;
        this.longitude_2 = longitude_2 * 54.6;
        this.latitude_2 = latitude_2 * 69;
    }

    /**
     * Used to return the distance between the two points in miles by converting the units of the longitude and
     * latitude and the euclidean distance found
     *
     * @return the euclidean distance between the two points (in miles)
     */
    public double get_distance(){

        //finds the relative distance values
        double y_dist = latitude_2 - latitude_1;
        double x_dist = longitude_2 - longitude_1;

        //finds the euclidean distance
        double euclidean = Math.sqrt(Math.pow(y_dist, 2) + Math.pow(x_dist, 2));

        //converts the distance value found to be in miles (approximate)
        return round(euclidean, 2);
    }

    /**
     * Used to round the results of the distance calculation to return a better looking result for the graph
     *
     * @param value value to truncate
     * @param places the places to truncate till
     * @return the result of the rounding calculation
     */
    public double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    /**
     * Used to return the direction the object would need to head in to get to the second location
     * The calculations used are based on the inverse tan value of the distance x and y found by the
     * difference between the values which finds the angle value from 0-90 which is then added to the correct
     * value for the start of the angle calculation based on the relative quadrant the second entity is in if
     * the first entity is viewed as the relative origin of the two points. This then leads to the calculation
     * of the distance value to return which is based on the main directions for a map (n, ne, e, se, s, sw, w, nw)
     * and the returned angle values which are viewed to be in hedges around the direction to find the "most"
     * correct answer
     *
     * @return string value for the direction from the first point to the second point
     */
    public String get_direction(){

        //finds the distance values
        double y_dist = Math.abs(latitude_2 - latitude_1);
        double x_dist = Math.abs(longitude_2 - longitude_1);

        //finds the angle between the distances
        double angle = Math.atan(x_dist/y_dist);

        //checking the quadrant for the points, point 1 is the origin, used to get angle out of 360
        if(latitude_1 > latitude_2){

            if(longitude_1 > longitude_2) {

                //in quad 3
                angle += 180;

            } else {

                //in quad 4
                angle += 270;
            }

        } else {

            if(longitude_1 > longitude_2) {

                //in quad 2
                angle += 90;
            }

            //else already math setup for being in quad 1
        }


        //checking for if the angle is between the two values, returning the correct direction value
        if((angle < 22.5) && (angle >= 0)) {

            //return east direction
            return "E";
        }
        else if((angle >= 22.5) && (angle < 67.5)) {

            //north east direction
            return "NE";
        }
        else if((angle >= 67.5) && (angle < 112.5)) {

            //north direction
            return "N";
        }
        else if((angle >= 112.5) && (angle < 157.5)) {

            //north west direction
            return "NW";
        }
        else if((angle >= 157.5) && (angle < 202.5)) {

            //west direction
            return "W";
        }
        else if((angle >= 202.5) && (angle < 247.5)) {

            //south west direction
            return "SW";
        }
        else if((angle >= 247.5) && (angle < 292.5)) {

            //south direction
            return "S";
        }
        else if((angle >= 292.5) && (angle < 337.5)) {

            //south east direction
            return "SE";
        }
        else if ((angle >= 337.5) && (angle < 360)){

            //east direction
            return "E";
        }
        else {

            //null direction
            return "";
        }
    }
}
