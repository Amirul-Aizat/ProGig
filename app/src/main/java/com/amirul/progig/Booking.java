package com.amirul.progig;

import java.io.Serializable;

public class Booking implements Serializable {

    String customerId;
    String service_name;
    String booking_date;
    String booking_time;
    String message;
    String gigId;
    String gig_name;
    String bookingID;
    String location;
    String status;

    public String getGig_name() {
        return gig_name;
    }

    public void setGig_name(String gig_name) {
        this.gig_name = gig_name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGigId() {
        return gigId;
    }

    public void setGigId(String gigId) {
        this.gigId = gigId;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
