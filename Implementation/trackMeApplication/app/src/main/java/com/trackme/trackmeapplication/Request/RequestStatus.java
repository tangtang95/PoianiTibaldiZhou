package com.trackme.trackmeapplication.Request;

import android.graphics.Color;

/**
 * Enum for the possible state of a request with the color associate.
 *
 * @author Mattia Tibaldi
 */
public enum RequestStatus {
    PENDING(Color.YELLOW),
    ACCEPT(Color.GREEN),
    REFUSE(Color.RED);

    private int color;

    /**
     * Constructor.
     *
     * @param color the color of the request.
     */
    RequestStatus(int color) {
        this.color = color;
    }

    /**
     * Getter method.
     *
     * @return the color.
     */
    public int getColor() {
        return color;
    }
}
