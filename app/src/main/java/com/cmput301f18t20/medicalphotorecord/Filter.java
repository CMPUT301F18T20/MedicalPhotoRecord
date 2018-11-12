/*
 * Class name: Filter
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 11/11/18 6:21 PM
 *
 * Last Modified: 11/11/18 6:12 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.cmput301f18t20.medicalphotorecord;

/**
 * Filter class to determine if Geo Location or Body Location should be included in the query
 *
 * @author mwhackma
 * @version 1.0
 * @see BodyLocation
 * @since 1.0
 */

public class Filter {
    protected Boolean isBodyLocationIncluded, isGeoIncluded;

    /** Default constructor sets both BodyLocationIncluded and GeoIncluded to false
     */
    Filter() {
        isBodyLocationIncluded = Boolean.FALSE;
        isGeoIncluded = Boolean.FALSE;
    }

    /** A Secondary constructor lets you set bodyLocationIncluded and GeoIncluded to
     * whatever you need.
     * @param isBodyLocationIncluded desired setting of body location boolean
     * @param isGeoIncluded desired setting of geo location boolean
     */
    Filter(Boolean isBodyLocationIncluded, Boolean isGeoIncluded) {
        this.isBodyLocationIncluded = isBodyLocationIncluded;
        this.isGeoIncluded = isGeoIncluded;
    }

    /** Ask the filter if body location should be included in the search
     * @return True if body location is included, else False.
     */
    public Boolean BodyLocationIncluded() {
        return isBodyLocationIncluded;
    }

    /** Ask the filter if geo location should be included in the search
     * @return True if geo location is included, else False.
     */
    public Boolean GeoIncluded() {
        return isGeoIncluded;
    }

    /** Set the filter's body location toggle to whatever value is passed in
     * @param bodyLocationIncluded new desired setting of body location boolean
     */
    public void setBodyLocationIncludedStatus(Boolean bodyLocationIncluded) {
        this.isBodyLocationIncluded = bodyLocationIncluded;
    }

    /** Set the filter's geo location toggle to whatever value is passed in
     * @param geoIncluded new desired setting of geo location boolean
     */
    public void setGeoIncludedStatus(Boolean geoIncluded) {
        this.isGeoIncluded = geoIncluded;
    }
}
