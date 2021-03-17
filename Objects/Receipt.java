package Objects;

import java.time.LocalDate;

/**
 * This class is used to represent the receipts being entered and observed with
 * in the database and application
 *
 * @author Lucas Hynes
 * @version 1.0.0
 * @since 2/20/2021
 */
public class Receipt {

    //holds the internal stats around the receipts
    private final int receipt_id;
    private final int driver_id;
    private final String company;
    private double amount;
    private final int card_no;
    private LocalDate date;
    private final String city;
    private String state;
    private final int zip_code;
    private final String category;

    /**
     * Constructor for the receipt class
     *
     * @param receipt_id the id of the receipt
     * @param driver_id the id of the driver
     * @param company the name of the company the receipt is for
     * @param amount the amount on the receipt
     * @param card_no the number of the card
     * @param date the date the receipt was made
     * @param city the city the receipt is from
     * @param state the state the receipt is from
     * @param zip_code the zip code the receipt is from
     * @param category the category of the receipt
     */
    public Receipt(int receipt_id, int driver_id, String company, double amount, int card_no, LocalDate date,
                   String city, String state, int zip_code, String category) {

        //assigns the values to the internal values within the object
        this.receipt_id = receipt_id;
        this.driver_id = driver_id;
        this.company = company;
        this.amount = amount;
        this.card_no = card_no;
        this.date = date;
        this.city = city;
        this.state = state;
        this.zip_code = zip_code;
        this.category = category;
    }

    /**
     * @return the id of the receipt
     */
    public int getReceipt_id() {
        return receipt_id;
    }

    /**
     * @return sets the value to the id of the driver
     */
    public int getDriver_id() {
        return driver_id;
    }

    /**
     * @return the company associated with the receipts
     */
    public String getCompany() {
        return company;
    }

    /**
     * @return the amount the receipt was for
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount sets the value the amount the receipt was for
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return the number of the card on the receipt
     */
    public int getCard_no() {
        return card_no;
    }

    /**
     * @return the date the receipt was made
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @param date sets the value to the date of the receipt
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @return the city for the receipt
     */
    public String getCity() {
        return city;
    }

    /**
     * @return the state for the receipt
     */
    public String getState() {
        return state;
    }

    /**
     * @param state sets the value to the state associated with the receipt
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the zip code for the receipt
     */
    public int getZip_code() {
        return zip_code;
    }

    /**
     * @return the category of the receipt
     */
    public String getCategory() {
        return category;
    }

}
