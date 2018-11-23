package Controllers;

import android.content.Context;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.google.gson.Gson;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import Activities.AddProblemActivity;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;
import androidx.test.rule.ActivityTestRule;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class OfflinePatientControllerTest {

    @Rule
    public ActivityTestRule<Activities.AddProblemActivity> AddProblemActivity =
            new ActivityTestRule<>(AddProblemActivity.class);

    public void wipeOfflineDatabase(){

        Context context = AddProblemActivity.getActivity().getBaseContext();
        ArrayList<Patient> emptyPatients = new ArrayList<>();
        new OfflineSaveController().savePatientList(emptyPatients, context);
    }

    public Patient addPatientToDatabase(Context context) throws UserIDMustBeAtLeastEightCharactersException {

        // Create patient
        Patient expectedPatient = new Patient("testOfflineGetPatient","","");
        ArrayList<Patient> patients = new ArrayList<>();
        patients.add(expectedPatient);
        new OfflineSaveController().savePatientList(patients, context);
        return expectedPatient;
    }

    @Test
    public void testOfflineGetPatient() throws UserIDMustBeAtLeastEightCharactersException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Wipe database
        wipeOfflineDatabase();

        // Create patient and add to database
        Patient expectedPatient = addPatientToDatabase(context);

        // Test getPatient()
        Patient gotPatient = new OfflinePatientController().getPatient(context, expectedPatient.getUuid());

        // Compare 2 objects, convert to gson string since date is giving some problem
        String expectedPatientString = new Gson().toJson(expectedPatient);
        String gotPatientString = new Gson().toJson(gotPatient);
        assertEquals("offline get patients are not the same", expectedPatientString, gotPatientString);

        // Wipe database
        wipeOfflineDatabase();
    }

    @Test
    public void testOfflineAddPatient() throws UserIDMustBeAtLeastEightCharactersException {
        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Wipe database
        wipeOfflineDatabase();

        // Create new patient
        Patient expectedPatient = new Patient("testOfflineAddPatient", "", "");

        // Test addPatient()
        new OfflinePatientController().addPatient(context, expectedPatient);

        // Compare 2 objects, convert to gson string since date is giving some problem
        Patient gotPatient = new OfflinePatientController().getPatient(context, expectedPatient.getUuid());
        String expectedPatientString = new Gson().toJson(expectedPatient);
        String gotPatientString = new Gson().toJson(gotPatient);
        assertEquals("offline add patients are not the same", expectedPatientString, gotPatientString);

        // Wipe database
        wipeOfflineDatabase();
    }

    @Test
    public void testOfflineDeletePatient() throws UserIDMustBeAtLeastEightCharactersException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Wipe database
        wipeOfflineDatabase();

        // Create patient and add to database
        Patient expectedPatient = addPatientToDatabase(context);

        // Test deletePatient
        new OfflinePatientController().deletePatient(context, expectedPatient);

        // Compare 2 objects, should be null since it can't be found
        Patient gotPatient = new OfflinePatientController().getPatient(context, expectedPatient.getUuid());
        assertNull("offline delete patients are not the same", gotPatient);

        // Wipe database
        wipeOfflineDatabase();
    }

    @Test
    public void testOfflineModifyPatient() throws UserIDMustBeAtLeastEightCharactersException {

        Context context = AddProblemActivity.getActivity().getBaseContext();

        // Wipe database
        wipeOfflineDatabase();

        // Create original patient and add to database
        Patient originalPatient = addPatientToDatabase(context);

        // Modify patient
        originalPatient.setEmail("testOfflineModifyPatient@email.com");
        originalPatient.setPhoneNumber("1111111111");

        // Test modifyPatient
        new OfflinePatientController().modifyPatient(context, originalPatient);

        // Compare 2 objects, convert to gson string since date is giving some problem
        Patient gotPatient = new OfflinePatientController().getPatient(context, originalPatient.getUuid());
        String expectedPatientString = new Gson().toJson(originalPatient);
        String gotPatientString = new Gson().toJson(gotPatient);
        assertEquals("offline modify patients are not the same", expectedPatientString, gotPatientString);

        // Wipe database
        wipeOfflineDatabase();
    }
}
