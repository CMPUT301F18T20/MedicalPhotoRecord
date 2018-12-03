package Controllers;

import android.util.Log;

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
     *  Given an ID, title, comment, date, patient's ID and problem UUID
     *  a comment record is made and the record is returned
     *
     * @param providerID - Provider ID
     * @param title - title of comment
     * @param date - date created
     * @param comment - comment written by provider
     * @param problemUUID - selected problem UUID
     * @return - a comment record created by the provider
     * @throws UserIDMustBeAtLeastEightCharactersException - ID must be >=8
     * @throws TitleTooLongException - title too long
     * @throws CommentTooLongException - comment too long
     */

    public Record createRecord(String providerID, String title, Date date, String comment, String problemUUID) throws UserIDMustBeAtLeastEightCharactersException, TitleTooLongException, CommentTooLongException {

        // Creating a new problem to be added, add that problem to patient, save patient
        Record record = new Record(providerID, title);
        record.setDate(date);
        record.setComment(comment);
        record.setAssociatedProblemUUID(problemUUID);
        return record;
    }

    /**
     * addRecord
     * Given a record add it to the list of records of a given problem.
     * The problem is fetched through the given problemUUID
     * @param record - record to be added
     * @throws ExecutionException -
     * @throws InterruptedException -
     */
    public void addRecord(Record record) throws ExecutionException, InterruptedException {
        new ElasticsearchRecordController.AddRecordTask().execute(record).get();
        Problem problem = new ElasticsearchProblemController.GetProblemByProblemUUIDTask().execute(record.getAssociatedProblemUUID()).get();
        problem.addRecord(record);
        new ElasticsearchProblemController.SaveModifiedProblem().execute(problem).get();


        // TODO addOfflineSaveController

    }
}
