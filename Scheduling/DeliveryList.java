package Scheduling;

import Objects.*;
import Scheduling.ScheduleOptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the object holding the list of activities and orders for the truck while the application
 * is running. The options include adding any of the activities to the list and designing output
 * for both calculation and formatting for the table view of each truck's delivery list
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 2/25/2021
 */
public class DeliveryList {

    //holds the list of activities for the truck
    private ArrayList<Object> deliveryList;

    //holds the truck associated with the delivery list
    private Truck truck;

    //list to hold the class that is used for output to the java fx table view
    private ArrayList<ScheduleChartObject> chart_entries;

    /**
     * This is the constructor method that takes a list of objects for scheduling, and the truck associated,
     * and creates the delivery list class for the list while also taking the data and prepping the format
     * for the table view
     *
     * @param deliveryList list of activities to complete for the truck
     * @param truck the truck doing the deliveries
     */
    public DeliveryList(ArrayList<Object> deliveryList, Truck truck) {

        //assigns the parameters to the internal variables
        this.deliveryList = deliveryList;
        this.truck = truck;

        //makes a new list for the chart entries
        this.chart_entries = new ArrayList<>();

        //goes through the list of objects within the delivery list to get the format for the table
        //view based list correct for the initialization of the class
        for (Object o : deliveryList) {

            //checks the instance of the object
            if (o instanceof Pickup) {

                //adds a new chart entry with the associated information and laid out to present as the
                //chart ready object (check schedule chart object for more info
                chart_entries.add(new ScheduleChartObject(((Pickup) o).getOrder().getDistributor().getName(),
                        ((Pickup) o).getOrder().getStore_front().getName(),
                        ((Pickup) o).getOrder().getCubes() + "c, " + ((Pickup) o).getOrder().getPieces()
                        + "p", ((Pickup) o).getOrder().getWeight() + " lbs", "Pick-up", 
                        ((Pickup) o).getDate(), ((Pickup) o).getMinutes_needed()));

                //sets the order for the new chart entry to the assigned order
                chart_entries.get(chart_entries.size()-1).setOrder(((Pickup) o).getOrder());
            }

            else if (o  instanceof DropOff) {

                //adds a new chart entry with the associated information and laid out to present as the
                //chart ready object (check schedule chart object for more info
                chart_entries.add(new ScheduleChartObject(((DropOff) o).getOrder().getStore_front().getName(),
                        ((DropOff) o).getOrder().getDistributor().getName(),
                        ((DropOff) o).getOrder().getCubes() + "c, " + ((DropOff) o).getOrder().getPieces() 
                        + "p", ((DropOff) o).getOrder().getWeight() + " lbs", "Drop Off", 
                        ((DropOff) o).getDate(), ((DropOff) o).getMinutes_needed()));

                //sets the order for the new chart entry to the assigned order
                chart_entries.get(chart_entries.size()-1).setOrder(((DropOff) o).getOrder());

            }
            else if (o instanceof Travel) {

                //to hold the names of the target and current locations
                String name_1;
                String name_2;

                //checking the instance of the classes to properly extract the name from the object
                if(((Travel) o).getStart_loc() instanceof StoreFront) {
                    name_1 = ((StoreFront) ((Travel) o).getStart_loc()).getName();
                }
                else { name_1 = ((Distributor) ((Travel) o).getStart_loc()).getName(); }

                //checking the instance of the classes to properly extract the name from the object
                if(((Travel) o).getStart_loc() instanceof StoreFront) {
                    name_2 = ((StoreFront)((Travel) o).getEnd_loc()).getName();
                }
                else { name_2 = ((Distributor) ((Travel) o).getEnd_loc()).getName(); }

                //adds the new value to the chart entries with the correct format for a travel object
                chart_entries.add(new ScheduleChartObject(name_1, name_2, ((Travel) o).getDirection(), 
                        ((Travel) o).getDistance() + "mi.", "Travel", ((Travel) o).getTime(),
                        ((Travel) o).getMinutes_needed()));

            }

            else if (o  instanceof Start) {

                //adds the start object to the chart object
                chart_entries.add(new ScheduleChartObject("", "", "", "",
                        "Start", ((Start) o).getStart_time(), ((Start) o).getMinutes_to_first_step()));
            }

            else if (o instanceof End) {

                //adds the end object to the chart object
                chart_entries.add(new ScheduleChartObject("", "", "", "",
                        "End", ((End) o).getEnd_time(), ((End) o).getRest_time_hours()));
            }
        }
    }

    /**
     * Allows for a pickup object to be added to the delivery list
     *
     * @param order the order being added to pick up
     * @param date the date of the pick up
     * @param minutes_needed the time needed for the pick up
     */
    public void add_pickup (Order order, LocalDate date, int minutes_needed){

        //retrieves the list from the object
        ArrayList<Object> deliveryList = getDeliveryList();

        //makes new pick up value with the parameter values
        Pickup new_pickup = new Pickup(order, date, minutes_needed);

        //adds the pickup object to the list of activities
        deliveryList.add(new_pickup);

        //sets the new delivery list to the current delivery list
        setDeliveryList(deliveryList);

        //returns the list used for the chart displays
        ArrayList<ScheduleChartObject> chart_fill = get_chart_fill_1();

        //adds the pickup values to the chart object
        chart_fill.add(new ScheduleChartObject(order.getDistributor().getName(),
                order.getStore_front().getName(), order.getCubes() + "c, " 
                + order.getPieces() + "p", order.getWeight() + "lbs", 
                "Pick-up", date, new_pickup.getMinutes_needed()));

        //adds the truck and order to the chart after being created
        chart_fill.get(chart_fill.size()-1).setOrder(order);
        chart_fill.get(chart_fill.size()-1).setTruck(truck);

        //sets the value to the new chart fill
        setChart_entries(chart_fill);
    }

    /**
     * This method is used to add a delivery to the delivery list as to drop off any associated orders
     *
     * @param order the order being dropped off
     * @param date the date of the drop off
     * @param minutes_needed the minutes needed for the drop off
     */
    public void add_delivery(Order order, LocalDate date, int minutes_needed){

        //retrieves the list from the object
        ArrayList<Object> deliveryList = getDeliveryList();

        //creates new instance for the drop off based on the parameters entered
        DropOff new_drop_off = new DropOff(order, date, minutes_needed);

        //adds the object to the list of activities
        deliveryList.add(new_drop_off);

        //sets the delivery list to the new value
        setDeliveryList(deliveryList);

        //holds the list of chart objects
        ArrayList<ScheduleChartObject> chart_fill = get_chart_fill_1();

        //adds the drop off event to the chart object with the correct format and information
        chart_fill.add(new ScheduleChartObject(order.getDistributor().getName(),
                order.getStore_front().getName(), order.getCubes() + "c, "
                + order.getPieces() + "p", order.getWeight() + "lbs",
                "Drop Off", date, new_drop_off.getMinutes_needed()));

        //sets the order associated with the chart object
        chart_fill.get(chart_fill.size()-1).setOrder(order);

        //sets the chart object to the new chart fill
        setChart_entries(chart_fill);
    }

    /**
     * This method is used to add a travel event to the delivery list
     *
     * @param start_loc the starting location for the truck
     * @param end_loc the ending location for the truck
     * @param direction the direction being traveled
     * @param distance the distance being traveled
     * @param time the date of the travel
     * @param minutes_needed the minutes needed for the travel
     */
    public void add_travel(Object start_loc, Object end_loc, String direction, double distance,
                           LocalDate time, int minutes_needed) {

        //holds the names of the destinations
        String name_1 = "", name_2 = "";

        //gets the active list being used from the user
        ArrayList<Object> deliveryList = getDeliveryList();

        //creates new travel object with parameters
        Travel new_travel = new Travel(start_loc, end_loc, direction, distance, time, minutes_needed);

        //adds the travel to the list
        deliveryList.add(new_travel);

        //sets the new list to the saved delivery list
        setDeliveryList(deliveryList);

        //defines the current list for the objects within the chart
        ArrayList<ScheduleChartObject> chart_fill = get_chart_fill_1();

        //checks to see the class type for the starting location is
        if (start_loc instanceof StoreFront) {

            //retrieves and saves the store front's name
            name_1 = ((StoreFront) start_loc).getName();
        }
        else if (start_loc instanceof Distributor) {

            //retrieves and saves the distributor's name
            name_1 = ((Distributor) start_loc).getName();
        }

        //checks to see the class type for the starting location is
        if (end_loc instanceof StoreFront) {

            //retrieves and saves the store front's name
            name_2 = ((StoreFront) end_loc).getName();
        }
        else if(end_loc instanceof Distributor) {

            //retrieves and saves the distributor's name
            name_2 = ((Distributor) end_loc).getName();
        }

        //adds the values found within the method and applies them to the chart entries
        chart_fill.add(new ScheduleChartObject(name_1, name_2, new_travel.getDirection(),
                new_travel.getDistance() + "mi.", "Travel", new_travel.getTime(), 
                new_travel.getMinutes_needed()));

        //sets the new values to the chart entries
        setChart_entries(chart_fill);
    }

    /**
     * Adds the start event to the delivery list
     *
     * @param time the time to start (date)
     * @param minutes_needed the minutes needed for the start
     */
    public void add_start(LocalDate time, int minutes_needed) {

        //retrieves the active delivery list
        ArrayList<Object> deliveryList = getDeliveryList();

        //adds the start to the delivery list
        deliveryList.add(new Start(time, 0));

        //sets the list to the new values
        setDeliveryList(deliveryList);

        //retrieves the active list of charts
        ArrayList<ScheduleChartObject> chart_fill = get_chart_fill_1();

        //adds the start values to the chart
        chart_fill.add(new ScheduleChartObject("", "", "", "", "Start",
                time, minutes_needed));

        //sets the chart to the new list
        setChart_entries(chart_fill);
    }

    /**
     * Adds the end event to the delivery list
     *
     * @param time the date of the end event
     * @param minutes_needed the minutes needed for the even (usually just 0)
     */
    public void add_end(LocalDate time, int minutes_needed) {

        //retrieves the active delivery list
        ArrayList<Object> deliveryList = getDeliveryList();

        //adds the new end object to the list
        deliveryList.add(new End(time, minutes_needed));

        //sets the updated list to the active list
        setDeliveryList(deliveryList);

        //retrieves the chart objects
        ArrayList<ScheduleChartObject> chart_fill = get_chart_fill_1();

        //adds the new end chart object to the list
        chart_fill.add(new ScheduleChartObject("", "", "", "", "End", 
                time, minutes_needed));

        //updates the values to reflect the new chart
        setChart_entries(chart_fill);
    }

    /**
     * @return the active delivery list
     */
    public ArrayList<Object> getDeliveryList() {
        return deliveryList;
    }

    /**
     * @param deliveryList value of param is assigned to the active list of the class
     */
    public void setDeliveryList(ArrayList<Object> deliveryList) {
        this.deliveryList = deliveryList;
    }

    /**
     * @return the truck associated with the list
     */
    public Truck getTruck() {
        return truck;
    }

    /**
     * @param truck sets the value to the truck associated with the delivery list
     */
    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    /**
     * @return the array list of values from the chart form of output
     */
    public ArrayList<ScheduleChartObject> get_chart_fill_1() {
        return chart_entries;
    }

    /**
     * Used for the output of the chart object when used with the javafx table view
     * as the output type of array list is incompatible
     *
     * @return the observable list for the same values as the get chart fill 1
     */
    public ObservableList<ScheduleChartObject> get_chart_fill_2() {

        //sets the list to the value returned
        List<ScheduleChartObject> list = get_chart_fill_1();

        //returns the fx collection of the list compiled
        return FXCollections.observableList(list);
    }

    /**
     * @param chart_entries the value passed in is used for the return of the chart entries for the class
     */
    public void setChart_entries(ArrayList<ScheduleChartObject> chart_entries) {
        this.chart_entries = chart_entries;
    }

    /**
     * Returns the delivery time objects, going through the list of entries to make the delivery time
     * objects at then end of the scheduling week so that the values can be analyzed in the report form
     *
     * @return the list of delivery time objects for the order
     */
    public ArrayList<DeliveryTime> getDeliverTimes() {

        //returns the active delivery list
        ArrayList<Object> al = getDeliveryList();

        //the delivery time list to hold the final values for the method
        ArrayList<DeliveryTime> dt = new ArrayList<>();

        //loops through all of the objects within the delivery list
        for(Object o: al){

            //checks for the type of action being used to tell wether to add a value, or add on to an
            //already entered value
            if(o instanceof Pickup){

                //defines the object to get the distance values between the store front and distributor
                //for the order
                DirectionDistance dd = new DirectionDistance(((Pickup) o).getOrder().getStore_front().getLongitude(),
                        ((Pickup) o).getOrder().getStore_front().getLatitude(),
                        ((Pickup) o).getOrder().getDistributor().getLongitude(),
                        ((Pickup) o).getOrder().getDistributor().getLatitude());

                //adds the delivery time value with the proper formatting and found values to the list
                dt.add(new DeliveryTime(-1, ((Pickup) o).getOrder().getStore_front(),
                        ((Pickup) o).getOrder().getDistributor(), new Truck(), ((Pickup) o).getOrder(),
                        ((Pickup) o).getOrder().getReceiveDate(), ((Pickup) o).getDate(), LocalDate.parse("0000-01-01"),
                        dd.get_distance()));

            } else if (o instanceof DropOff) {

                //goes through the current delivery time list to find the matching associated order
                //to know when the package was dropped off and to find the correct index to change
                for(DeliveryTime d: dt){

                    //checks for the matching id value
                    if(d.getOrder().getOrder_id() == ((DropOff) o).getOrder().getOrder_id()) {

                        //fills in the drop off date
                        d.setTime_drop_off(((DropOff) o).getDate());
                    }
                }
            }

            //sets the truck value to the delivery time checking for an empty list
            if(dt.size() != 0) {
                dt.get(dt.size() - 1).setTruck(getTruck());
            }
        }

        //returns the final class value list for the delivery times to be used in the database
        return dt;
    }

    public boolean editPickup(ScheduleChartObject sco, int index){

        ArrayList<Object> dl = getDeliveryList();

        Pickup p = new Pickup(sco.getOrder(), sco.getTime(), sco.getMinutes());

        dl.set(index, p);

        setDeliveryList(dl);

        ArrayList<ScheduleChartObject> al = get_chart_fill_1();
        al.set(index, sco);
        setChart_entries(al);

        return false;
    }   

}