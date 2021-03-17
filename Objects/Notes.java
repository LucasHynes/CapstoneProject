package Objects;

/**
 * This class holds the information around notes for the various attributes within the
 * database, with the key being the target of the note is the SINGLE id that is not a
 * -1 value, and that number is the key for the correct attribute to which this note is
 * targeted to
 *
 *                  ****NOTE: This class is not yet active in the UI****
 *
 * @author Lucas Hynes
 * @version 0.2.2
 * @since 3/1/2021
 */
public class Notes {

    //definition of the internal variable values with defaults defined
    private int notes_id;
    private String note;
    private int distributor_id = -1;
    private int store_front_id = -1;
    private int truck_id = -1;
    private int driver_id = -1;

    /**
     * Constructor which takes the category input to know which key is referenced as
     * the category id, then the note itself and the id of the note
     *
     * @param notes_id the id of the note
     * @param note the note itself
     * @param category the category of the note (truck, driver, store front, etc.)
     * @param category_id the primary_id of what the note references, as pointed out by the category variable
     */
    public Notes(int notes_id, String note, String category, int category_id) {

        //assigns the values
        this.notes_id = notes_id;
        this.note = note;

        //check the value for the category parameter
        switch (category) {

            case "distributor":

                //assigns the id to the distributor value
                this.distributor_id = category_id;
                break;

            case "store_front":

                //assigns the id to the store front value
                this.store_front_id = category_id;
                break;

            case "truck":

                //assigns the id to the truck value
                this.truck_id = category_id;
                break;

            case "driver":

                //assigns the id to the driver value
                this.driver_id = category_id;
                break;
        }
    }

    /**
     * @return the id of the note
     */
    public int getNotes_id() {
        return notes_id;
    }

    /**
     * @param notes_id sets to the id of the note
     */
    @SuppressWarnings("unused")
    public void setNotes_id(int notes_id) {
        this.notes_id = notes_id;
    }

    /**
     * @return the string value of the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note sets the value to the value of the note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the distributor id is returned
     */
    public int getDistributor_id() {
        return distributor_id;
    }

    /**
     * @param distributor_id sets to the id of the distributor
     */
    @SuppressWarnings("unused")
    public void setDistributor_id(int distributor_id) {
        this.distributor_id = distributor_id;
    }

    /**
     * @return the id of the store front
     */
    public int getStore_front_id() {
        return store_front_id;
    }

    /**
     * @param store_front_id sets to the id value of the store front
     */
    @SuppressWarnings("unused")
    public void setStore_front_id(int store_front_id) {
        this.store_front_id = store_front_id;
    }

    /**
     * @return the id of the truck value
     */
    @SuppressWarnings("unused")
    public int getTruck_id() {
        return truck_id;
    }

    /**
     * @param truck_id sets the value to the id of the truck
     */
    @SuppressWarnings("unused")
    public void setTruck_id(int truck_id) {
        this.truck_id = truck_id;
    }

    /**
     * @return the id of the driver
     */
    @SuppressWarnings("unused")
    public int getDriver_id() {
        return driver_id;
    }

    /**
     * @param driver_id sets the value to the id of the driver
     */
    @SuppressWarnings("unused")
    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }
}
