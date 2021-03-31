package Database;

import FormFiles.ErrorWindow;
import Objects.Driver;
import Objects.*;
import Scheduling.ScheduleChartObject;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * This class is used to access the central database of the application to add, set, and get the various data points
 * within the database, to be able to access the data through out the rest of the classes. This is used through the
 * public data retrieval methods included.
 *
 * @author Lucas Hynes
 * @version 1.3.0
 * @since 2/19/2021
 */
public class DatabaseAccess {
    //holds the information that allows the class to access the database
    private static final String userName = "admin";
    private static final String password = "fleetadmiral";
    //to use in case of database access failure: allowPublicKeyRetrieval=true&
    private static final String url = "jdbc:mysql://localhost:3306/fleetdata?useSSL=false";

    /**
     * Gets the open orders in the database that need to be added to the trucks
     *
     * @return a list of the open and active orders
     * @throws SQLException handles any invalid entries
     * @param current_location the current location of the pick up
     */
    public ArrayList<Order> get_open_orders(Object current_location) throws SQLException {

        //id for the distributor picking up
        int id;

        //checks for the proper class for the object passed through
        if(current_location instanceof Distributor){

            //sets the value to the locations id
            id = ((Distributor) current_location).getDistributor_id();

        } else {

            //displays errors encountered with details
            new ErrorWindow(new Stage(), "Store Front attempt to pick up", "The user is " +
                    "not allowed to pickup an order from a store front, the store must be a Distributor. If" +
                    "the store is also a distributor, add the same information into the system for the option" +
                    "to select the store as an option");

            //escapes the function for safety
            return null;
        }

        //attempts the connection to the server, calling a
        // SQL statement to retrieve the orders placed,
        // and not yet picked up
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM orders WHERE status = " +
                     "\"Order Placed\" AND Distributor_ID = " + id + ";");
             ResultSet rs = ps.executeQuery()) {

            ArrayList<Order> open_orders = new ArrayList<>();

            //loops through the queries resulting set and adds all the open order values

            while (rs.next()) {
                //adds all orders received from the query into classes held in a list
                open_orders.add(new Order(rs.getInt("Order_ID"),
                        rs.getString("PK_From_Company"), rs.getString("Key_2"),
                        get_distributor_based_id(rs.getInt("Distributor_ID")),
                        get_store_front_based_id(rs.getInt("StoreFront_ID")),
                        get_rate_based_id(rs.getInt("Rate_ID")), rs.getDouble("Cubes"),
                        rs.getDouble("Weight"), rs.getDouble("Pieces"),
                        rs.getDouble("Merch_Price"), rs.getString("Notes")));

                //adds the receive date to the note.
                open_orders.get(open_orders.size() - 1).setReceiveDate(rs.getDate(
                        "Create_Date").toLocalDate());
            }
            //returns the list of the open orders
            return open_orders;
        }
    }

    /**
     * Used to receive a String list of the trucks in the database
     * @return the list of truck names
     * @throws SQLException protects against invalid data within database
     */
    public ArrayList<String> get_truck_string() throws SQLException {
        //attempts the connection to the server, calling a SQL statement to retrieve the list of trucks
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM truck_fleet");
             ResultSet rs = ps.executeQuery()) {

            //holds the list of truck names (starts empty)
            ArrayList<String> truck_names = new ArrayList<>();

            //loops through results
            while (rs.next()) {
                //adds the truck names to the list
                truck_names.add(rs.getString("Truck_Name"));
            }

            //returns the list of truck names
            return truck_names;
        }
    }

    /**
     * Used to return the classes of all entered distributors in the database
     *
     * @return the list of distributors and associated data
     * @throws SQLException handles invalid data points
     */
    public ArrayList<Distributor> get_distributors() throws SQLException {
        //attempts the connection to the server, calling a SQL statement to retrieve the list of trucks
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM distributor");
             ResultSet rs = ps.executeQuery()) {

            //holds list of distributors
            ArrayList<Distributor> distributor_list = new ArrayList<>();

            //loops through query results
            while (rs.next()) {
                //adds the data returned into a list of distributor objects
                distributor_list.add(new Distributor(rs.getInt("Distributor_ID"),
                        rs.getString("Company_Name"), rs.getString("Address"),
                        rs.getString("City"), rs.getString("State"),
                        rs.getDouble("Long"), rs.getDouble("Lat")));
            }

            //returns the list of class objects type Distributor
            return distributor_list;
        }
    }

    /**
     * Used to return the Store Fronts held within the database
     *
     * @return list of store fronts and associated data
     * @throws SQLException handles invalid data points
     */
    public ArrayList<StoreFront> get_store_fronts() throws SQLException {
        //attempts to connect to server and query given string
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM store_front");
             ResultSet rs = ps.executeQuery()) {

            //holds the list of store fronts
            ArrayList<StoreFront> storefront_list = new ArrayList<>();

            //loops through the returned rows
            while (rs.next()) {
                //adds the data to the class structure
                storefront_list.add(new StoreFront(rs.getInt("StoreFront_ID"),
                        rs.getString("Company_Name"), rs.getString("Address"),
                        rs.getString("City"), rs.getString("State"),
                        rs.getDouble("Long"), rs.getDouble("Lat")));
            }

            //returns the list of store fronts
            return storefront_list;
        }
    }

    /**
     * Used to return the trucks held within the database
     *
     * @return the list of trucks and associated data
     * @throws SQLException handles invalid data points
     */
    public ArrayList<Truck> get_trucks() throws SQLException {

        //attempts to connect and query the database with the given statement
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM truck_fleet");
             ResultSet rs = ps.executeQuery()) {

            //holds the list of Trucks
            ArrayList<Truck> truck_list = new ArrayList<>();

            //loops through the returned rows
            while (rs.next()) {

                //adds the associated data to the class structure
                truck_list.add(new Truck(rs.getInt("Truck_ID"), rs.getString("Truck_Name"),
                        rs.getDouble("Avg_Time_per_Dist")));

                truck_list.get(truck_list.size() - 1).setDriver(get_driver_based_id(rs.getInt("Driver_ID")));
            }

            //returns the list of truck objects
            return truck_list;
        }
    }

    /**
     * Used to return the drivers held within the database
     *
     * @return the list of drivers associated
     * @throws SQLException handles invalid data points
     */
    public ArrayList<Driver> get_drivers() throws SQLException {

        //attempts to connect to the database and query the given statement
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM drivers");
             ResultSet rs = ps.executeQuery()) {

            //holds the list of drivers
            ArrayList<Driver> driver_list = new ArrayList<>();

            //loops through the returned rows
            while (rs.next()) {
                //adds the driver to the class object
                driver_list.add(new Driver(rs.getInt("Driver_ID"),
                        rs.getString("Name"), rs.getDouble("Avg_Time_per_Dist")));
            }

            //returns the list of drivers
            return driver_list;
        }
    }

    /**
     * Used to return the rates information held within the database
     *
     * @return the rates for the different companies
     * @throws SQLException handles the invalid data points
     */
    public ArrayList<RateSheet> get_rates() throws SQLException {

        //attempts to connect and query the database with the given statement
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM rate_sheet");
             ResultSet rs = ps.executeQuery()) {

            //holds the list of rates
            ArrayList<RateSheet> rate_list = new ArrayList<>();

            //loops through the returned rows
            while (rs.next()) {

                //adds the rate information into the class object for the rates
                rate_list.add(new RateSheet(rs.getInt("Rate_ID"), rs.getInt("Distributor_ID"),
                        rs.getInt("StoreFront_ID"), rs.getDouble("Rate"),
                        rs.getDouble("Minimum"), rs.getString("Notes")));
            }

            //returns the list of rates
            return rate_list;
        }
    }

    /**
     * Used to return the notes stored within the database
     *
     * @return the list of notes stored as a class object
     * @throws SQLException handles invalid data
     */
    public ArrayList<Notes> get_notes() throws SQLException {

        //attempts to connect to the database and query the given statement
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM notes");
             ResultSet rs = ps.executeQuery()) {

            //holds the list of notes
            ArrayList<Notes> note_list = new ArrayList<>();

            //loops through the returned rows
            while (rs.next()) {

                //holds the category value of the target
                String category = "";
                //holds the id of the target
                int target_id = -1;

                //checks for the id and sets the category based on which value is not the -1 value
                if (rs.getInt("Distributor_ID") != -1) {

                    //assigns the category and id to distributor
                    category = "distributor";
                    target_id = rs.getInt("Distributor");

                } else if (rs.getInt("StoreFront_ID") != -1) {

                    //assigns the category and id to store front
                    category = "store_front";
                    target_id = rs.getInt("StoreFront_ID");

                } else if (rs.getInt("Truck_ID") != -1) {

                    //assigns the category and id to trucks
                    category = "truck";
                    target_id = rs.getInt("Truck_ID");

                } else if (rs.getInt("Driver_ID") != -1) {

                    //assigns the category and id to drivers
                    category = "driver";
                    target_id = rs.getInt("Driver_ID");
                }

                //adds t0he result to a new class object with the associated note and the note's id
                note_list.add(new Notes(rs.getInt("Notes_ID"), rs.getString("Notes"),
                        category, target_id));
            }

            //returns the list of notes associated within the database
            return note_list;
        }
    }

    /**
     * Returns all orders regardless of wether or not they have been delivered
     *
     * @return the orders
     * @throws SQLException handles the invalid data
     */
    public ArrayList<Order> get_all_orders() throws SQLException {
        //attempts the connection to the server, calling a SQL statement to retrieve the orders placed,
        // and not yet picked up
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM orders");
             ResultSet rs = ps.executeQuery()) {

            //holds the list of orders
            ArrayList<Order> open_orders = new ArrayList<>();

            //loops through the queries resulting set and adds all the open order values
            while (rs.next()) {
                open_orders.add(new Order(rs.getInt("Order_ID"),
                        rs.getString("PK_From_Company"), rs.getString("Key_2"),
                        get_distributor_based_id(rs.getInt("Distributor_ID")),
                        get_store_front_based_id(rs.getInt("Store Front_ID")),
                        get_rate_based_id(rs.getInt("Rate_ID")), rs.getDouble("Cubes"),
                        rs.getDouble("Weight"), rs.getDouble("Pieces"),
                        rs.getDouble("Merch_Price"), rs.getString("Notes")));
            }

            //returns the full list of orders
            return open_orders;
        }
    }

    /**
     * Returns the receipts and their associated data points to the user
     *
     * @return the list of receipts
     * @throws SQLException handles invalid data points
     */
    public ArrayList<Receipt> get_receipts() throws SQLException {
        //attempts the connection to the server, calling a SQL statement to retrieve the orders placed,
        // and not yet picked up
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM receipts");
             ResultSet rs = ps.executeQuery()) {

            ArrayList<Receipt> receipts = new ArrayList<>();

            //loops through the queries resulting set and adds all the open order values
            while (rs.next()) {
                //adds the resulting data to the class object
                receipts.add(new Receipt(rs.getInt("Receipt_ID"),
                        rs.getInt("Driver_ID"), rs.getString("Company"),
                        rs.getDouble("Amount"), rs.getInt("Card_No"),
                        LocalDate.parse(rs.getString("Date")), rs.getString("City"),
                        rs.getString("State"), rs.getInt("Zip"),
                        rs.getString("Category")));
            }

            //returns the list of receipts
            return receipts;
        }
    }

    /**
     * Ued to get all delivery times within the database
     *
     * @return the list of delivery times
     * @throws SQLException handles invalid data points
     */
    @SuppressWarnings("unused")
    public ArrayList<DeliveryTime> get_delivery_times() throws SQLException {
        //attempts the connection to the server, calling a SQL statement to retrieve the orders placed,
        // and not yet picked up
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM delivery_times");
             ResultSet rs = ps.executeQuery()) {

            //holds the list of delivery times
            ArrayList<DeliveryTime> deliveryTimes = new ArrayList<>();

            //loops through the queries resulting set and adds all the open order values
            while (rs.next()) {
                //adds the resulting data from the row into a new object
                deliveryTimes.add(new DeliveryTime(rs.getInt("Delivery_ID"),
                        get_store_front_based_id(rs.getInt("StoreFront_ID")),
                        get_distributor_based_id(rs.getInt("Distributor_ID")),
                        get_truck_based_id(rs.getInt("Truck_ID")),
                        get_order_based_id(rs.getInt("Order_ID")),
                        LocalDate.parse(rs.getString("Time_Received")),
                        LocalDate.parse(rs.getString("Time_Pickup")),
                        LocalDate.parse(rs.getString("Time_Drop_off")),
                        rs.getDouble("Distance")));
            }

            //returns the delivery times
            return deliveryTimes;
        }
    }

    /**
     * Used to get the distributor that has a matching ID value
     *
     * @param id the id number of the Distributor desired
     * @return the distributor object that has a matching id
     * @throws SQLException handles invalid data points
     */
    public Distributor get_distributor_based_id(int id) throws SQLException {

        //calls the method to get the complete list of distributors
        ArrayList<Distributor> distributors = get_distributors();

        //loops through the list
        for (Distributor d : distributors) {

            //looks for the matching id value
            if (d.getDistributor_id() == id) {

                //if found returns the matched object
                return d;
            }
        }

        //if no match was found, return null
        return null;
    }

    /**
     * Used to get the store front based on the matching id
     *
     * @param id the id of the desired store front
     * @return the store front object
     * @throws SQLException handles invalid data points
     */
    public StoreFront get_store_front_based_id(int id) throws SQLException {

        //retrieves the list of store front objects
        ArrayList<StoreFront> storeFronts = get_store_fronts();

        //loops through the store fronts
        for (StoreFront s : storeFronts) {

            //searches for the matching id value
            if (s.getStore_front_id() == id) {

                //returns the matched storefront value
                return s;
            }
        }

        //if no match was found, returns null
        return null;
    }

    /**
     * Used to get the truck object with the matching id
     *
     * @param id the id of the desired truck
     * @return the truck object
     * @throws SQLException handles invalid data points
     */
    public Truck get_truck_based_id(int id) throws SQLException {

        //retrieves the list of trucks through a method call
        ArrayList<Truck> trucks = get_trucks();

        //loops through the trucks
        for (Truck t : trucks) {

            //searching for the matching id value
            if (t.getTruck_id() == id) {

                //returns the matched truck object
                return t;
            }
        }

        //if no match was found, returned null
        return null;
    }

    /**
     * Used to get the driver object with the matching id
     *
     * @param id the id of the desired driver
     * @return the driver object
     * @throws SQLException handles invalid data points
     */
    public Driver get_driver_based_id(int id) throws SQLException {

        //retrieves the list of drivers through a method call
        ArrayList<Driver> drivers = get_drivers();

        //loops through the resulting list
        for (Driver d : drivers) {

            //searching for the matching id
            if (d.getDriver_id() == id) {

                //returns the matched driver
                return d;
            }
        }

        //if no match was found, return null
        return null;
    }

    /**
     * Used to get the rate object with the matching id
     *
     * @param id id of the desired rate
     * @return the rate object
     * @throws SQLException handles invalid data points
     */
    private RateSheet get_rate_based_id(int id) throws SQLException {

        //retrieves the rates objects through a method call
        ArrayList<RateSheet> rates = get_rates();

        //loops through thr resulting rates
        for (RateSheet r : rates) {

            //searching for the rate with the matching id
            if (r.getRate_id() == id) {

                //returns the rate found to be a match
                return r;
            }
        }

        //if no match was found, return null
        return null;
    }

    /**
     * Used to get the note object with the matching id
     *
     * @param id id of the desired note
     * @return the note object
     * @throws SQLException handles invalid data points
     */
    @SuppressWarnings("unused")
    private Notes get_note_based_id(int id) throws SQLException {

        //retrieves the list of notes through a method call
        ArrayList<Notes> notes = get_notes();

        //loops through the notes returned
        for (Notes n : notes) {

            //searching for the note with the matching id
            if (n.getNotes_id() == id) {

                //returns the matched note
                return n;
            }
        }

        //if no match was found, return null
        return null;
    }

    /**
     * Used to get the order object with the matching id
     * @param id the id of the desired order
     * @return the order object
     * @throws SQLException handles invalid data points
     */
    public Order get_order_based_id(int id) throws SQLException {

        //NOTE: the methodology used within the method is separate from the other methods, as there is an assumption
        //that the amount of orders stored within the database exceed the size to have the other given methodology be
        //not efficient enough to be able to respond to the query quickly enough.

        //attempts the connection to the server, calling a SQL statement to retrieve the orders placed,
        // and not yet picked up
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM orders WHERE Order_ID = " + id + ";");
             ResultSet rs = ps.executeQuery()) {

            //adds the row values to the new object
            Order order = new Order();

            //goes through results
            while(rs.next()) {

                //assigns the value to the order variable
                order = new Order(rs.getInt("Order_ID"),
                    rs.getString("PK_From_Company"), rs.getString("Key_2"),
                    get_distributor_based_id(rs.getInt("Distributor_ID")),
                    get_store_front_based_id(rs.getInt("StoreFront_ID")),
                    get_rate_based_id(rs.getInt("Rate_ID")), rs.getDouble("Cubes"),
                    rs.getDouble("Weight"), rs.getDouble("Pieces"),
                    rs.getDouble("Merch_Price"), rs.getString("Notes"));

                //sets the date for the creation of the order
                order.setReceiveDate(rs.getDate("Create_Date").toLocalDate());

            }
            //returns the most recent matching id of the order being searched for
            return order;
        }
    }

    /**
     * Used to get the receipt object with the matching id
     *
     * @param id the id of the desired receipt
     * @return the receipt object
     * @throws SQLException handles the invalid data points
     */
    @SuppressWarnings("unused")
    public Receipt get_receipt_based_id(int id) throws SQLException {

        //retrieves all receipt objects through method call
        ArrayList<Receipt> receipts = get_receipts();

        //loops through the resulting list of objects
        for (Receipt r : receipts) {

            //searching for the receipt with the matching id
            if (r.getReceipt_id() == id) {

                //returns the matched receipt object
                return r;
            }
        }

        //if no match was found, return null
        return null;
    }

    /**
     * Used to get the delivery time object with the matching id
     *
     * @param id the id of the delivery time requested
     * @return the delivery time object
     * @throws SQLException handles invalid data input
     */
    @SuppressWarnings("unused")
    public DeliveryTime get_delivery_time_based_id(int id) throws SQLException {
        //NOTE: because there is ~2 delivery time per order, the same method is applied as with the
        //get_order_based_id for same reasons of effective resource management

        //attempts the connection to the server, calling a SQL statement to retrieve the orders placed,
        // and not yet picked up
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM delivery_times WHERE Delivery ID = \""
                     + id + "\";");
             ResultSet rs = ps.executeQuery()) {


            //declares class variable to hold results
            DeliveryTime deliveryTimes = null;

            //loops through the queries resulting set and adds all the open order values
            while (rs.next()) {
                //adds the row values found into the new class object
                deliveryTimes = (new DeliveryTime(rs.getInt("Delivery_ID"),
                        get_store_front_based_id(rs.getInt("StoreFront_ID")),
                        get_distributor_based_id(rs.getInt("Distributor_ID")),
                        get_truck_based_id(rs.getInt("Truck_ID")),
                        get_order_based_id(rs.getInt("Order_ID")),
                        LocalDate.parse(rs.getString("Time_Received")),
                        LocalDate.parse(rs.getString("Time_Pickup")),
                        LocalDate.parse(rs.getString("Time_Drop_off")),
                        rs.getDouble("Distance")));
            }

            //returns the found object to the user
            return deliveryTimes;
        }
    }

    /**
     * Used to add an order to the database
     *
     * @param order the order object requested to be added
     * @throws SQLException handles invalid data entries/syntax errors
     */
    public void add_order(Order order) throws SQLException {

        //attempts to connect to database and queries the results of the private function
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement(null_account(order))) {
            //returns the status of the execution function
            ps.execute();
        }
    }

    /**
     * Used to add a receipt tp the database
     *
     * @param receipt the receipt object that is being requested to be added
     * @throws SQLException handles invalid user input
     */
    public void add_receipt(Receipt receipt) throws SQLException {

        //attempts to connect to the database and insert the object's data into the database
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO fleetdata.receipts (" +
                     "Driver_ID, Company, Amount, Card_No, City, State, Zip, Category, Date) VALUES (\"" +
                     receipt.getDriver_id() + "\", \"" + receipt.getCompany() + "\", \"" + receipt.getAmount() +
                     "\", \"" + receipt.getCard_no() + "\", \"" + receipt.getCity() +
                     "\", \"" + receipt.getState() + "\", \"" + receipt.getZip_code() + "\", \"" +
                     receipt.getCategory() + "\", \"" + receipt.getDate() + "\");")) {
            //the result of the query *made void as it is not needed to be checked
            ps.execute();
        }
    }

    /**
     * retrieves the companies used in the receipts within the database
     *
     * @return the array list of String values for the receipt's entered companies
     * @throws SQLException handles invalid data
     */
    public ArrayList<String> get_receipt_companies() throws SQLException {
        //attempts the connection to the server, calling a SQL statement to find the distinct companies
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT Company FROM receipts");
             ResultSet rs = ps.executeQuery()) {

            //list to hold the array list of companies
            ArrayList<String> companies = new ArrayList<>();

            //loops through the returned values
            while (rs.next()) {

                //adds the string to the list
                companies.add(rs.getString("Company"));
            }

            //returns the list of companies
            return companies;
        }
    }

    /**
     * retrieves the categories used in the receipts within the database
     *
     * @return the array list of categories from the receipts
     * @throws SQLException handles invalid data
     */
    public ArrayList<String> get_receipt_categories() throws SQLException {

        //attempts the connection to the server, calling a SQL statement to retrieve the receipts' categories
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT Category FROM receipts");
             ResultSet rs = ps.executeQuery()) {

            //list to hold the different categories
            ArrayList<String> categories = new ArrayList<>();

            //loops through the returned rows
            while (rs.next()) {

                //adds the value to the list
                categories.add(rs.getString("Category"));
            }

            //returns the list of categories
            return categories;
        }
    }

    /**
     * Returns the id of the driver with the given name as input
     *
     * @param driverName the name of the driver to look for the id
     * @return the id that matches the name given
     */
    public int get_driver_id(String driverName) {

        //attempts to connect to the database and query the drivers with the matching name value
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM drivers WHERE" +
                     " Name = \"" + driverName + "\";");
             ResultSet rs = ps.executeQuery()) {

            //holds the id
            int result = -1;

            //loops through the row values returned
            while (rs.next()) {

                //retrieves the id from the result
                result = rs.getInt("Driver_ID");
            }

            //returns the resulting id
            return result;

        } catch (SQLException throwables) {
            //handles invalid sql statement
            throwables.printStackTrace();
        }

        //default return value
        return -1;
    }

    /**
     * Returns the names of the distributors from the database
     *
     * @return the list of names found
     * @throws SQLException handles invalid data
     */
    public ArrayList<String> get_distributors_names() throws SQLException {

        //attempts the connection to the server, calling a SQL statement to retrieve the distributor company names
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT Company_Name FROM distributor");
             ResultSet rs = ps.executeQuery()) {

            //holds the list of distributors
            ArrayList<String> categories = new ArrayList<>();

            //loops through the resulting rows
            while (rs.next()) {

                //adds the values found to the list
                categories.add(rs.getString("Company_Name"));
            }

            //return list of company names
            return categories;
        }
    }

    /**
     * Retrieves the store fronts' names from the database
     *
     * @return the list of names of the store fronts
     * @throws SQLException handles invalid data
     */
    public ArrayList<String> get_store_front_names() throws SQLException {

        //attempts to connect to the database and query the given statement
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT Company_Name FROM store_front");
             ResultSet rs = ps.executeQuery()) {

            //list of company names
            ArrayList<String> categories = new ArrayList<>();

            //loops through returned rows
            while (rs.next()) {

                //adds the returned values to the list
                categories.add(rs.getString("Company_Name"));
            }

            //returns the list
            return categories;
        }
    }

    /**
     * finds the specific rate object based on the distributor and storefront id
     *
     * @param dist_id the distributor's id
     * @param store_id the store front's id
     * @return returns the rate object
     * @throws SQLException handles invalid data
     */
    public RateSheet findRate(int dist_id, int store_id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM rate_sheet WHERE (Distributor_ID = \"" +
                     dist_id + "\") AND (StoreFront_ID = \"" + store_id + "\");");
             ResultSet rs = ps.executeQuery()) {

            //sets the rate sheet to find to null
            RateSheet categories = null;

            //loops through the rows returned
            while (rs.next()) {

                //gets the information from the row and adds an object to the list with the associated data
                categories = (new RateSheet(rs.getInt("Rate_ID"),
                        rs.getInt("Distributor_ID"),
                        rs.getInt("StoreFront_ID"),
                        rs.getDouble("Rate"),
                        rs.getDouble("Minimum"),
                        rs.getString("Notes")));
            }

            //returns the object rate sheet
            return categories;
        }
    }

    /**
     * Used to account for any null values inputted from the user around which would resolve in
     * null pointer errors. Function uses the null checks to change the query string to either
     * include or not the id of the rate, distributor, and store front
     *
     * @param order is the order to check for the nulls
     * @return the appropriate string to be able to insert the order into the database
     */
    private String null_account(Order order) {

        //holds the list of different strings to add to the string returned
        ArrayList<String> non_null_columns = new ArrayList<>();

        //checks if the rate object is null or not
        if (order.getRate() != null) {

            //if the object is not null, adds the values to the list to be able to be added to the query
            non_null_columns.add(0, "Rate_ID, ");
            non_null_columns.add(1, "\"" + order.getRate().getRate_id() + "\", ");
        } else {

            //if the object is null, insert empty values
            non_null_columns.add(0, "");
            non_null_columns.add(1, "");
        }

        //checks if the distributor object is null or not
        if (order.getDistributor() != null) {

            //if the object is not null, adds the values to the list to be able to be added to the query
            non_null_columns.add(2, "Distributor_ID, ");
            non_null_columns.add(3, "\"" + order.getDistributor().getDistributor_id() + "\", ");
        } else {

            //if the object is null, insert empty values
            non_null_columns.add(0, "");
            non_null_columns.add(1, "");
        }

        //checks if the store front object is null or not
        if (order.getStore_front() != null) {

            //if the object is not null, adds the values to the list to be able to be added to the query
            non_null_columns.add(4, "StoreFront_ID, ");
            non_null_columns.add(5, "\"" + order.getStore_front().getStore_front_id() + "\", ");
        } else {

            //if the object is null, insert empty values
            non_null_columns.add(0, "");
            non_null_columns.add(1, "");
        }

        //returns the string with the array values formatted within the text to either add or put a blank value
        // depending on the stored values
        return ("INSERT INTO fleetdata.orders (PK_From_Company, "
                + non_null_columns.get(2) + non_null_columns.get(0) + "Status, Cubes, Weight, Pieces, "
                + non_null_columns.get(4) + "Merch_Price, Create_Date) VALUE(\"" + order.getPrimary_distributor_key()
                + "\", " + non_null_columns.get(3) + non_null_columns.get(1) + "\"" + order.getStatus() + "\", \""
                + order.getCubes() + "\", \"" + order.getWeight() + "\", \"" + order.getPieces() + "\", "
                + non_null_columns.get(5) + "\"" + order.getMerch_price() + "\", \"" + order.getReceiveDate() + "\");");
    }

    /**
     * Returns the objects within the database that are linked to the truck based on the truck object passed
     * through the method
     *
     * @param truck the truck object that is searching for the associated load
     * @return the list of objects associated with the truck
     * @throws SQLException handles invalid input
     */
    public ArrayList<ScheduleChartObject> get_truck_load(Truck truck) throws SQLException {

        //attempts to connect to the database and query to find the loads that have the matching truck id
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM loads WHERE Truck_ID =  "
                     + truck.getTruck_id() + ";");
             ResultSet rs = ps.executeQuery()) {

            //list to hold the objects that are found
            ArrayList<ScheduleChartObject> categories = new ArrayList<>();

            //loops through the resulting rows returned
            while (rs.next()) {

                //checks for the values to skip
                if (!rs.getString("Current_Location_ID").equals("not found")) {

                    //holds the name of the current and target locations names
                    String name_1;
                    String name_2;

                    //checks if the current location is a distributor or a store front
                    if (rs.getString("Current_Location_ID").contains("D")) {

                        //holds the found value, taking out the d character at the end of the number
                        int temp = Integer.parseInt((rs.getString(
                                "Current_Location_ID")).replace("D", ""));

                        //sets the name variable to the value returned from the object as it's matched with the id found
                        name_1 = get_distributor_based_id(temp).getName();

                    } else {

                        //holds the found value of id, removing the s character
                        int temp = Integer.parseInt((rs.getString(
                                "Current_Location_ID")).replace("S", ""));

                        //sets the name variable to the name associated with the given store front id
                        name_1 = get_store_front_based_id(temp).getName();
                    }


                    //checking if the destination location contains the d character signifying the id stored is related
                    //to a distributor, not a store front
                    if (rs.getString("Destination_Location_ID").contains("D")) {

                        //saves the id value found, removing the d character
                        int temp = Integer.parseInt((rs.getString(
                                "Destination_Location_ID")).replace("D", ""));

                        //finds the matching distributor, based on the found id val
                        name_2 = get_distributor_based_id(temp).getName();

                    } else {

                        //saves the id value found, removing the s character
                        int temp = Integer.parseInt((rs.getString(
                                "Destination_Location_ID")).replace("S", ""));

                        //finds the store front object's name associated with the id, through method call
                        name_2 = get_store_front_based_id(temp).getName();
                    }

                    DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    //adds the string values found and the other information stored in the row into the object
                    categories.add(new ScheduleChartObject(name_1, name_2, rs.getString("Info_1"),
                            rs.getString("Info_2"), rs.getString("Activity"),
                            LocalDateTime.parse(rs.getString("DateTime"), f).toLocalDate(),
                            rs.getInt("Minutes_Spent")));

                    //sets the truck and order objects for the found object
                    categories.get(categories.size() - 1).setTruck(truck);
                    categories.get(categories.size() - 1).setOrder(
                            get_order_based_id(rs.getInt("Order_ID")));
                }
            }

            //returns the list of found values for the load of the truck
            return categories;
        }
    }

    /**
     * Function to return the truck object based on the matching name of the truck
     *
     * @param n the string value of the truck to match
     * @return the truck object with the associated name
     * @throws SQLException handles invalid input
     */
    public Truck get_truck_based_name(String n) throws SQLException {

        //holds the list of trucks found
        ArrayList<Truck> t = get_trucks();

        //loops through the list of trucks
        for (Truck a : t) {

            //checks if the name is a match for the truck
            if (a.getTruck_name().equals(n)) {

                //if true return the truck object
                return a;
            }
        }

        //if no match found in list, return null
        return null;
    }

    /**
     * Returns a combined list of the distributors and storefronts within the database
     *
     * @return the object combination of the distributors and store fronts in the database
     * @throws SQLException handles invalid data
     */
    public ArrayList<Object> get_destinations() throws SQLException {

        //holds the list to be added to
        ArrayList<Object> destinations = new ArrayList<>();

        //adds the full list of both distributors and store fronts
        destinations.addAll(get_store_fronts());
        destinations.addAll(get_distributors());

        //returns the final combination
        return destinations;
    }

    /**
     * Goes through the list of destinations and pulls their names into a list
     *
     * @return array list of string values for the names of the destinations
     * @throws SQLException handles invalid data
     */
    public ArrayList<String> get_destination_names() throws SQLException {

        //holds the names found
        ArrayList<String> s = new ArrayList<>();

        //loops through all destinations
        for (Object o: get_destinations()) {

            //checks the class instance of the object and calls the according casted declaration
            if(o instanceof StoreFront) { s.add(((StoreFront) o).getName()); }
            else { s.add(((Distributor) o).getName()); }
        }

        //returns the final list of all locations
        return s;
    }

    /**
     * Retrieves the proper destination object that matches the name passed through the method
     *
     * @param n the name of the distributor/store front to look for
     * @return the object with the matching name
     * @throws SQLException handles invalid data
     */
    public Object get_destination_object(String n) throws SQLException {

        //the list holding the destinations
        ArrayList<Object> o = get_destinations();

        //loops through the list of destinations
        for(Object obj:o){

            //checking for the store front or distributor class type
            if(obj instanceof StoreFront){

                //checks if the object has the name matching the search
                if(((StoreFront) obj).getName().equals(n)){

                    //returns the found obj
                    return obj;
                }

            } else {

                //checks if the object found has the name matching the input variable
                if(((Distributor) obj).getName().equals(n)) {

                    //returns the found object
                    return obj;
                }
            }
        }

        //if no match is found, return null
        return null;
    }

    /**
     * Used to set the distributor passed through the function to the database
     *
     * @param d the distributor object to add to the database
     * @throws SQLException handles invalid data input
     */
    public void set_distributor(Distributor d) throws SQLException {

        //attempts to connect to the database and pass the query to insert a new distributor
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO `distributor` (" +
                     "`Company_Name`, `Address`, `City`, `State`, `Long`, `Lat`) VALUES (\"" +
                     d.getName() + "\", \"" + d.getAddress() + "\", \"" + d.getCity() +
                     "\", \"" + d.getState() + "\", " + d.getLongitude() +
                     ", " + d.getLatitude() + ");")) {

            //result of the query returned (not needed)
            ps.execute();
        }
    }

    /**
     * Used to set the store front passed through the function to the database
     *
     * @param d the store front to add
     * @throws SQLException handles invalid data
     */
    public void set_storeFront(StoreFront d) throws SQLException {

        //attempts to connect to the database and insert the values into the database as a new store front
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO `store_front` (" +
                     "`Company_Name`, `Address`, `City`, `State`, `Long`, `Lat`) VALUES (\"" +
                     d.getName() + "\", \"" + d.getAddress() + "\", \"" + d.getCity() +
                     "\", \"" + d.getState() + "\", \"" + d.getLongitude() +
                     "\", \"" + d.getLatitude() + "\");")) {

            //returns result (not needed)
            ps.execute();
        }
    }

    /**
     * Used to set the values passed through the method to be a new rate sheet entry
     *
     * @param d the distributor linked to the rate
     * @param s the store front linked to the rate
     * @param rate the percentage of the merchandise the shipping costs
     * @param min the mandatory minimum payment
     * @throws SQLException handles invalid data input
     */
    public void set_rateSheet(Distributor d, StoreFront s, double rate, double min) throws SQLException {

        //checks to make sure both objects are not null before proceeding
        if((d!=null) && (s!=null)) {

            //attempts to connect to the database and insert the values into the proper table
            try (Connection conn = DriverManager.getConnection(url, userName, password);
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO fleetdata.rate_sheet (" +
                         "Distributor_ID, StoreFront_ID, Rate, Minimum) VALUES (\"" +
                         d.getDistributor_id() + "\", \"" + s.getStore_front_id() + "\", \"" + rate +
                         "\", \"" + min + "\");")) {

                //holds results of the query
                ps.execute();
            }
        } else {

            //displays errors encountered with details
            new ErrorWindow(new Stage(), "Attempt to add rate for invalid objects",
                    "The rate of the two objects cannot be set with a new company value. " +
                            "Please add the value to the database before creating a rate for it!");

        }
    }

    /**
     * Used to set the values passed through to the database for a new driver
     *
     * @param driverName only param needed to create a driver is a name, rest of chart filled later
     * @throws SQLException handles invalid data input
     */
    public void set_driver(String driverName) throws SQLException{

        //attempts to connect to the database and insert the new driver into the table
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO fleetdata.drivers (" +
                     "Name) VALUES (\"" + driverName + "\");")) {

            //holds results of query
            ps.execute();
        }
    }

    /**
     * Used to match the name input for the driver to match the id value of the driver associated with the trucks
     * active. The join insures only correct candidates are being looped through, while also attempting to limit
     * the accidental addition of drivers with matching names if there was a historical entry matching the name
     *
     * @param value the string value of the driver name
     * @return the id number for the driver
     * @throws SQLException handles invalid data exceptions
     */
    private int driver_id_based_name(String value) throws SQLException {
        //attempts the connection to the server, calling a SQL statement to retrieve the orders placed,
        // and not yet picked up
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("SELECT drivers.Driver_ID, drivers.Name" +
                     " FROM drivers WHERE drivers.Name = \"" + value + "\";");
             ResultSet rs = ps.executeQuery()) {

            int id_found = -1;

            //loops through the queries resulting set and adds all the open order values
            while (rs.next()) {
                //adds the resulting data to the class object
                id_found = rs.getInt("Driver_ID");
            }

            return id_found;
        }
    }

    /**
     * Used to set the values passed through to the database for a new truck
     *
     * @param text only needed value for the truck is the truck's "name" or reference
     * @param value the value of the id of the driver
     * @throws SQLException handles invalid data input
     */
    public void set_truck(String text, String value) throws SQLException{

        //attempts to connect to the database and query an insert into the truck table
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO fleetdata.truck_fleet (" +
                     "Truck_Name, Driver_ID) VALUES (\"" + text + "\", \"" + driver_id_based_name(value) +"\");")) {

            //holds results of query
            ps.execute();
        }
    }

    /**
     * Returns the Distributor object with the matching name as the param in function
     *
     * @param id the name of the distributor
     * @return the Distributor object with matching name
     * @throws SQLException handles invalid data exceptions
     */
    public Distributor get_distributor_based_name(String id) throws SQLException {

        //holds the list of distributors throughout the database
        ArrayList<Distributor> distributors = get_distributors();

        //loops through the found distributors
        for (Distributor d : distributors) {

            //checks for the name to be equal to the value passed through
            if (d.getName().equals(id)) {

                //returns the matching distributor object
                return d;
            }
        }

        //returns null if no match was found
        return null;
    }

    /**
     * Returns the StoreFront object with the matching name as the param in the function
     *
     * @param id the name of the store front to search for
     * @return the matching store front object
     * @throws SQLException handles invalid data exceptions
     */
    public StoreFront get_store_front_based_name(String id) throws SQLException {

        //returns the store fronts to a list
        ArrayList<StoreFront> storeFronts = get_store_fronts();

        //loops through the list of store fronts
        for (StoreFront s : storeFronts) {

            //checking for matching name
            if (s.getName().equals(id)) {

                //return matching object
                return s;
            }
        }

        //return null if no match found
        return new StoreFront();
    }

    /**
     * Used for the reports to be developed and the results returned for more flexible results possible
     *
     * @param query the string value of the query to pass
     * @return the resulting table of the query
     * @throws SQLException handles invalid data exceptions
     */
    public ArrayList<ArrayList<String>> reportQuery(String query) throws SQLException {

        //2d array to hold all the different row information
        ArrayList<ArrayList<String>> list_results = new ArrayList<>();

        //attempts to connect to the database and execute the given query
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {


            //gets the meta data of the info returned
            ResultSetMetaData rsmd = rs.getMetaData();

            //loops through the returned rows of the query
            while(rs.next()){

                //holds all row values
                ArrayList<String> rowValues = new ArrayList<>();

                //goes through the columns returned and adds the values to the list of values
                for(int i = 1; i <= rsmd.getColumnCount(); i++) { rowValues.add(rs.getString(i)); }

                //adds the list made to the 2d list
                list_results.add(rowValues);
            }
        }

        //returns the row values to the user from the query
        return list_results;
    }

    /**
     * Goes through the list of store fronts and distributors to find the correct id for the name value and to return
     * the proper id with the addendum of the character at the end to signify the class of the id
     *
     * @param name the string value to match the object's names
     * @return the String value of the object's id and a character to signify class
     * @throws SQLException handles invalid data input
     */
    private String get_id_based_name(String name) throws SQLException {

        //loops through the list of distributors and looks for name matches, returning the associated id to the name
        for(Distributor d: get_distributors()) { if(d.getName().equals(name)) { return d.getDistributor_id() + "D"; } }

        //loops through the list of store fronts and looks for name matches, returning the associated id to the name
        for(StoreFront s: get_store_fronts()) { if (s.getName().equals(name)) { return s.getStore_front_id() + "S"; } }

        //if no match is found, return default not found string
        return "not found";
    }

    /**
     * Used to input the different load classes into the database
     *
     * @param o the object to add to the load table
     * @throws SQLException handles invalid data input
     */
    public void set_load(ScheduleChartObject o) throws SQLException {

        //attempts to connect to the database and insert the object into the load table with the
        //data associated with it
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO fleetdata.loads (" + "Activity," +
                     " Current_Location_ID, Destination_Location_ID, Info_1, Info_2, DateTime, " + "Minutes_Spent, " +
                     "Truck_ID, Order_ID) VALUES (\"" + o.getCategory() + "\", \"" +
                     get_id_based_name(o.getCurrent_loc()) + "\", \"" + get_id_based_name(o.getTarget_loc()) +
                     "\", \"" + o.getData_group_1() + "\", \"" + o.getData_group_2() + "\", \"" + o.getTime() +
                     "\", \"" + o.getMinutes() + "\", \"" + o.getTruck().getTruck_id() + "\", \"" +
                     o.getOrder().getOrder_id() + "\");")) {

            //holds results of the insert
            ps.execute();
        }
    }

    /**
     * Used to add a new delivery time to the table in the database
     *
     * @param dt the delivery time to add to the database
     * @throws SQLException handles invalid data input
     */
    public void set_delivery_time(DeliveryTime dt) throws SQLException {

        //attempts to connect to the database, then execute a query to insert the values into the database
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO fleetdata.delivery_times (StoreFront_ID," +
                     " Distributor_ID, Truck_ID, Drive_ID, Order_ID, Time_Received, Time_Pickup, Time_Drop_off, " +
                     "Distance) VALUES (\"" + dt.getStore_front().getStore_front_id() + "\", \"" +
                     dt.getDistributor().getDistributor_id() + "\", \"" + dt.getTruck().getTruck_id() +
                     "\", \"" + dt.getTruck().getDriver().getDriver_id() +
                     "\", \"" + dt.getOrder().getOrder_id() +
                     "\", \"" + dt.getTime_received() + "\", \"" + dt.getTime_pickup() + "\", \"" +
                     dt.getTime_drop_off() + "\", \"" + dt.getDistance() + "\");")) {

            //holds the result of the query
            ps.execute();
        }
    }

    /**
     * Adds a load to the historical load table to be used in reference to the linear regression methods
     *
     * @param o the object to add to the table
     * @throws SQLException handles invalid data
     */
    public void set_historical_load(ScheduleChartObject o) throws SQLException {

        //attempts to connect to the database and insert the query into the historical loads table
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO fleetdata.historical_load (Category," +
                     " Current_Location_ID, Destination_Location_ID, Info_1, Info_2, `Date`, Minutes_Spent, " +
                     "Truck_ID, Order_ID) VALUES (\"" + o.getCategory() + "\", \"" +
                     get_id_based_name(o.getCurrent_loc()) + "\", \"" + get_id_based_name(o.getTarget_loc()) +
                     "\", \"" + o.getData_group_1() + "\", \"" + o.getData_group_2() + "\", \"" + o.getTime() +
                     "\", \"" + o.getMinutes() + "\", \"" + o.getTruck().getTruck_id() + "\", \"" +
                     o.getOrder().getOrder_id() + "\");")) {

            //holds the results of the query
            ps.execute();
        }
    }

    /**
     * Used to return the historical values of a specified category
     *
     * @param category string value to match the category of the data
     * @return a list of objects with the same category
     * @throws SQLException handles invalid data input
     */
    public Object[] get_historical_stat(String category) throws SQLException{

        //two lists to hold the different variables of x and y
        ArrayList<Double> list_x = new ArrayList<>();
        ArrayList<Double> list_y = new ArrayList<>();

        //if the load matches pickup or drop off
        if (category.equals("Pick-up") || category.equals("Drop Off")) {

            //attempts to connect to the database and pull the rows with matching category values with
            //valid time entries
            try (Connection conn = DriverManager.getConnection(url, userName, password);
                 PreparedStatement ps = conn.prepareStatement("SELECT * FROM fleetdata.historical_load WHERE" +
                         " (Category = \"Pick-up\" OR Category = \"Drop Off\") AND Minutes_Spent != 0;");
                 ResultSet rs = ps.executeQuery()) {

                //loops through the resulting rows
                while (rs.next()) {

                    //assigns a string array to split the information and get the cube amount
                    String[] s = rs.getString("Info_1").split("c");

                    //adds the cube amount to the found value
                    list_x.add(Double.parseDouble(s[0]));

                    //adds the minutes spent value to the y-list
                    list_y.add(Double.parseDouble(rs.getString("Minutes_Spent")));
                }
            }
        } else if (category.equals("Travel")) {

            //attempts to connect to the database and to retrieve the rows with the matching category value
            try (Connection conn = DriverManager.getConnection(url, userName, password);
                 PreparedStatement ps = conn.prepareStatement("SELECT * FROM fleetdata.historical_load WHERE" +
                         " (Category = \"Travel\") AND Minutes_Spent != 0;");
                 ResultSet rs = ps.executeQuery()) {

                //loops through the resulting rows
                while (rs.next()) {

                    //splits the string to find the mile amount of the object
                    String[] s = rs.getString("Info_2").split("mi");

                    //adds the found mile amount to the list
                    list_x.add(Double.parseDouble(s[0]));

                    //add the time it takes to get that distance and save it to the y-list
                    list_y.add(Double.parseDouble(rs.getString("Minutes_Spent")));
                }
            }
        }

        //return array holding the x and y lists
        return new Object[]{list_x, list_y};
    }

    /**
     * Used to wipe the current load schedule to move onto a different date
     *
     * @throws SQLException handles internal sql errors
     */
    public void deleteData(String tableName) throws SQLException {

        //attempts to connect to the database and insert the query into the historical loads table
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM fleetdata." + tableName + ";")) {

            //holds the results of the query
            ps.execute();
        }
    }


    /**
     * Used to update the order status of the packages to reflect what is happened to the order
     * since the last database update
     *
     * @param status string value to set the order status to
     * @param order_id the id of the order to update the status of
     * @throws SQLException handles invalid data exceptions
     */
    public void updateStatus(String status, int order_id) throws SQLException {

        //attempts to connect to the database and insert the query into the orders table to update the status
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("UPDATE fleetdata.orders SET Status = \" " +
                     status + "\" WHERE Order_ID = " + order_id + ";")) {

            //holds the results of the query
            ps.execute();
        }
    }

    public void updateStoreFront(StoreFront s) throws SQLException {
        //attempts to connect to the database and insert the query into the orders table to update the status
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("UPDATE fleetdata.store_front SET Company_Name = \" " +
                     s.getName() + "\", Address = \"" + s.getAddress() + "\", City = \"" + s.getCity() + 
                     "\", State = \"" + s.getState() + "\", Long = \"" + s.getLongitude() + "\", Lat = \"" +
                     s.getLatitude() + "\" WHERE StoreFront_ID = " + s.getStore_front_id() + ";")) {

            //holds the results of the query
            ps.execute();
        }
    }

    public void updateDistributor(Distributor s) throws SQLException {
        //attempts to connect to the database and insert the query into the orders table to update the status
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("UPDATE fleetdata.store_front SET Company_Name = \" " +
                     s.getName() + "\", Address = \"" + s.getAddress() + "\", City = \"" + s.getCity() +
                     "\", State = \"" + s.getState() + "\", Long = \"" + s.getLongitude() + "\", Lat = \"" +
                     s.getLatitude() + "\" WHERE Distributor_ID = " + s.getDistributor_id() + ";")) {

            //holds the results of the query
            ps.execute();
        }
    }

    public void update_driver(Driver s) throws SQLException {
        //attempts to connect to the database and insert the query into the orders table to update the status
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("UPDATE fleetdata.drivers SET 'Name' = \" " +
                     s.getDriver_name() + "\" WHERE Driver_ID = " + s.getDriver_id() + ";")) {

            //holds the results of the query
            ps.execute();
        }
    }

    public void update_order(Order o1) throws SQLException {
        //attempts to connect to the database and insert the query into the orders table to update the status
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("UPDATE fleetdata.orders SET PK_From_Company = \" " +
                     o1.getPrimary_distributor_key() + "\", Key_2 = \"" + o1.getSecondary_key() +
                     "\", Distributor_ID = \"" + o1.getDistributor().getDistributor_id() +"\", StoreFront_ID = \"" +
                     o1.getStore_front().getStore_front_id() + "\", Status = \"" + o1.getStatus() + "\", Cubes = \"" +
                     o1.getCubes() + "\", Weight = \"" + o1.getWeight() + "\", Pieces = \"" + o1.getPieces() +
                     "\", Rate_ID = \"" +  o1.getRate().getRate_id() + "\", Notes = \"" + o1.getNotes() + "\", " +
                     "Merch_Price = \"" + o1.getMerch_price() + "\", Create_Date = \"" + o1.getReceiveDate() + "\" " +
                     "WHERE Order_ID = " + o1.getOrder_id() + ";")) {

            //holds the results of the query
            ps.execute();
        }
    }

    public void update_rateSheet(RateSheet rs) throws SQLException {
        //attempts to connect to the database and insert the query into the orders table to update the status
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("UPDATE fleetdata.rate_sheet SET Distributor_ID = \" " +
                     rs.getDistributor_id() + "\", StoreFront_ID = \"" + rs.getStore_front_id() +
                     "\", Rate = \"" + rs.getRate() +"\", Minimum = \"" + rs.getMinimum() + "\", Notes = \"" +
                     rs.getNotes() + "\" WHERE Rate_ID = " + rs.getRate_id() + ";")) {

            //holds the results of the query
            ps.execute();
        }
    }

    public void update_receipt(Receipt r) throws SQLException {
        //attempts to connect to the database and insert the query into the orders table to update the status
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("UPDATE fleetdata.receipts SET Driver_ID = \" " +
                     r.getDriver_id() + "\", Company = \"" + r.getCompany() + "\", Amount = \"" + r.getAmount() +
                     "\", Card_No = \"" + r.getCard_no() + "\", 'Date' = \"" + r.getDate() + "\", City = \"" +
                     r.getCity() + "\", Sate = \"" + r.getState() + "\", Zip = \"" + r.getZip_code() +
                     "\", Category = \"" + r.getCategory() + "\" WHERE Receipt_ID = " + r.getReceipt_id() + ";")) {

            //holds the results of the query
            ps.execute();
        }
    }

    public void update_truck(Truck t) throws SQLException{
        //attempts to connect to the database and insert the query into the orders table to update the status
        try (Connection conn = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = conn.prepareStatement("UPDATE fleetdata.truck_fleet SET Truck_Name = \" " +
                     t.getTruck_name() + "\", Driver_ID = \"" + t.getDriver().getDriver_id() + "\" WHERE Truck_ID = "
                     + t.getTruck_id() + ";")) {

            //holds the results of the query
            ps.execute();
        }
    }
}