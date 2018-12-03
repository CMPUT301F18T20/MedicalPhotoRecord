/*
 * Class name: SearchActivityTestPatient
 *
 * Version: Version 1.0
 *
 * Developed by members of CMPUT301F18T20 on Date: 02/12/18 7:39 PM
 *
 * Last Modified: 02/12/18 7:39 PM
 *
 * Copyright (c) 2018, CMPUT301F18T20, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package Activities;

import Enums.USER_TYPE;

import static Enums.USER_TYPE.PATIENT;

public class SearchActivityTestPatient extends SearchActivityTest {
    public String getUserIDForIntent() {
        return PatientUserID;
    };
    public USER_TYPE getUserTypeForIntent() {
        return PATIENT;
    };
}
