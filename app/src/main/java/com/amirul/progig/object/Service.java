package com.amirul.progig.object;

import java.io.Serializable;

public class Service implements Serializable {

    private String service_ID;
    private String service_name;
    private String description;
    private String serviceRate;
    private String gigID;
    private String gig_name;
    private String service_area;
    private String category;
    private float review;

    public float getReview() {
        return review;
    }

    public void setReview(float review) {
        this.review = review;
    }

    public String getGig_name() {
        return gig_name;
    }

    public void setGig_name(String gig_name) {
        this.gig_name = gig_name;
    }

    public String getService_area() {
        return service_area;
    }

    public void setService_area(String service_area) {
        this.service_area = service_area;
    }

    public String getService_ID() {
        return service_ID;
    }

    public void setService_ID(String service_ID) {
        this.service_ID = service_ID;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(String serviceRate) {
        this.serviceRate = serviceRate;
    }

    public String getGigID() {
        return gigID;
    }

    public void setGigID(String gigID) {
        this.gigID = gigID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

