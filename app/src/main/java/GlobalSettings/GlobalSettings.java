package GlobalSettings;

import Enums.INDEX_TYPE;

import static Enums.INDEX_TYPE.MAIN;
import static Enums.INDEX_TYPE.TEST;

public class GlobalSettings {
    public static final String
            PROVIDERFILE = "provider_list.sav",
            PATIENTFILE = "patient_list.sav",
            PROBLEMFILE = "problem_list_2.sav",
            RECORDFILE = "record_list.sav",
            PHOTOFILE = "photo_list.sav",
            USERIDEXTRA = "UserID",
            EMAILEXTRA = "Email",
            PHONEEXTRA = "Phone",
            PROBLEMIDEXTRA = "ProblemID",
            MAININDEX = "cmput301f18t20",
            TESTINDEX = "cmput301f18t20test";

    public static INDEX_TYPE INDEXTYPE = MAIN;

    public static String getIndex() {
        if (INDEXTYPE == TEST) {
            return TESTINDEX;
        }

        return MAININDEX;
    }
}
