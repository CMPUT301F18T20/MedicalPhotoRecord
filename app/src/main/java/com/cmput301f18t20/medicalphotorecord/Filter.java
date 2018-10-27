package com.cmput301f18t20.medicalphotorecord;

public class Filter {
    protected Boolean isBodyLocatinIncluded, isGeoIncluded;

    Filter(Boolean isBodyLocatinIncluded, Boolean isGeoIncluded) {
        isBodyLocatinIncluded = isBodyLocatinIncluded;
        isGeoIncluded = isGeoIncluded;
    }

    public Boolean BodyLocationIncluded() {
        return isBodyLocatinIncluded;
    }

    public Boolean GeoIncluded() {
        return isGeoIncluded;
    }

    public void setBodyLocatinIncludedStatus(Boolean bodyLocatinIncluded) {
        isBodyLocatinIncluded = bodyLocatinIncluded;
    }

    public void setGeoIncludedStatus(Boolean geoIncluded) {
        isGeoIncluded = geoIncluded;
    }
}
