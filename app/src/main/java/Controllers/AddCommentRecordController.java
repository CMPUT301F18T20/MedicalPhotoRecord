package Controllers;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import Exceptions.CommentTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

/**
 * AddCommentRecordController
 * Creates a new Record that holds the provider's comment
 * and adds it into a selected problem's list of records.
 *
 * @author  Richard De Asis
 * @version 1.0
 * @since   2018-11-27
 *
 */


public class AddCommentRecordController {
    /**
     * addRecord
     * Given an ID, title, comment, date, patient's ID and problem UUID
     * a comment record is made and added to the list of records of a given problem.
     * The problem is fetched through the given problemUUID
     *
     * @param createdBy - ID of CommentRecord creator
     * @param title - title
     * @param comment - comment
     * @param date - auto generated date
     * @param patientId - patient's ID
     * @param problemUUID - problem's UUID
     * @throws CommentTooLongException - comment too long
     * @throws UserIDMustBeAtLeastEightCharactersException - ID must be >=8
     * @throws TitleTooLongException - title too long
     * @throws ExecutionException -
     * @throws InterruptedException -
     */
    public void addRecord(String createdBy, String title, String comment, Date date, String patientId, String problemUUID) throws CommentTooLongException,
            UserIDMustBeAtLeastEightCharactersException, TitleTooLongException, ExecutionException, InterruptedException {

        Record record = new Record(createdBy,title);
        record.setComment(comment);
        record.setDate(date);
        record.setAssociatedProblemUUID(problemUUID);
        record.setAssociatedPatientUserID(patientId);

        new ElasticsearchRecordController.AddRecordTask().execute(record).get();
        //Problem problem = new ElasticsearchProblemController.GetProblemByProblemUUIDTask().execute(problemUUID).get();
        //if (problem.getCreatedByUserID().equals(patientId)) {
        //    problem.addRecord(record);
        // }
        //new ElasticsearchProblemController.SaveModifiedProblem().execute(problem).get();

        // TODO addOfflineSaveController

    }
}
