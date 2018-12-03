/* resources used https://www.youtube.com/watch?v=ARezg1D9Zd0 */


package Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.cmput301f18t20.medicalphotorecord.Patient;
import com.cmput301f18t20.medicalphotorecord.R;

import Activities.BrowseUserActivity;

/**
 * AddPatientDialog
 * Pop up dialog for provider to assign patient to themselves
 * @version 2.0
 * @see com.cmput301f18t20.medicalphotorecord.Provider
 * @see com.cmput301f18t20.medicalphotorecord.Patient
 */
public class AddPatientDialog extends AppCompatDialogFragment {

    private EditText editTextPatientUserID;
    private AddPatientDialogListener listener;

    @Override
    @NonNull
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_patient_dialog, null);

        builder.setView(view)
                .setTitle("Add Patient")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userID = editTextPatientUserID.getText().toString();
                        listener.verifyUserID(userID);
                    }
                });

         editTextPatientUserID = view.findViewById(R.id.add_patient_user_id);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddPatientDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();

        }
    }

    public interface AddPatientDialogListener{
        void verifyUserID(String userID);
    }

}
