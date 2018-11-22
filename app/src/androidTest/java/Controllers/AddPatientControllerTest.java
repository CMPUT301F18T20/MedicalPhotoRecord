package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.Provider;
import com.google.gson.Gson;
import org.junit.Rule;
import org.junit.Test;

import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import static junit.framework.TestCase.assertEquals;

public class AddPatientControllerTest  {

    @Rule
    public ActivityTestRule<Activities.BrowseUserActivity> BrowseUserActivity =
            new ActivityTestRule<>(Activities.BrowseUserActivity.class);

    @Test
    @UiThreadTest
    public void testAddPatient() throws UserIDMustBeAtLeastEightCharactersException {
        Context context = BrowseUserActivity.getActivity().getBaseContext();

        // Create new patient and provider
        Provider provider = new Provider("providerName");
        Patient expectedPatient = new Patient("patientName","","");

        // Save to database
        new UserController().addPatient(context, expectedPatient);
        new UserController().addProvider(context, provider);
        new AddPatientController().addPatient(context, provider.getUserID(), expectedPatient.getUserID());

        // Get from database and Compare
        Provider gotProvider = new ModifyProviderController().getProvider(context, provider.getUserID());
        Patient gotPatient = gotProvider.getPatient(gotProvider.getPatients().size()-1);

        // Compare 2 objects
        String expectedPatientString = new Gson().toJson(expectedPatient);
        String gotPatientString = new Gson().toJson(gotPatient);
        assertEquals("added patient are not the same", expectedPatientString, gotPatientString);


    }
}
