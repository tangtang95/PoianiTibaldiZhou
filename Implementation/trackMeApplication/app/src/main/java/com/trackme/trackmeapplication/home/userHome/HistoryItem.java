package com.trackme.trackmeapplication.home.userHome;

/**
 * History item object, represent an element in the list of data showed to the user.
 *
 * @author Mattia Tibaldi
 */
public class HistoryItem {

    private String date;
    private String position;
    private String pulse;
    private String bloodPressure;

    /**
     * Constructor.
     *
     * @param date the date when the data are taken.
     * @param position position of the user
     * @param pulse pulse value
     * @param bloodPressure blood pressure value
     */
    HistoryItem(String date, String position, String pulse, String bloodPressure) {
        this.date = date;
        this.position = position;
        this.pulse = pulse;
        this.bloodPressure = bloodPressure;
    }

    /**
     * Getter method.
     *
     * @return a string with pulse and blood pressure values
     */
    String getCompactInfo() {
        return "Pulse:" + pulse + " Pressure:" + bloodPressure;
    }

    /*Getter method*/

    public String getDate() {
        return date;
    }

    public String getPosition() {
        return position;
    }

    public String getPulse() {
        return pulse;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }
}
