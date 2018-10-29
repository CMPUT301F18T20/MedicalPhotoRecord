package com.cmput301f18t20.medicalphotorecord;

public class Filter {
    protected Boolean isBodyLocationIncluded, isGeoIncluded;

    Filter() {
        isBodyLocationIncluded = Boolean.FALSE;
        isGeoIncluded = Boolean.FALSE;
    }

    Filter(Boolean isBodyLocationIncluded, Boolean isGeoIncluded) {
        this.isBodyLocationIncluded = isBodyLocationIncluded;
        this.isGeoIncluded = isGeoIncluded;
    }

    public Boolean BodyLocationIncluded() {
        return isBodyLocationIncluded;
    }

    public Boolean GeoIncluded() {
        return isGeoIncluded;
    }

    public void setBodyLocationIncludedStatus(Boolean bodyLocationIncluded) {
        this.isBodyLocationIncluded = bodyLocationIncluded;
    }

    public void setGeoIncludedStatus(Boolean geoIncluded) {
        this.isGeoIncluded = geoIncluded;
    }
}
