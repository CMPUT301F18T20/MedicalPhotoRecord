package Controllers;

import com.cmput301f18t20.medicalphotorecord.Problem;
import com.cmput301f18t20.medicalphotorecord.Record;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import Exceptions.CommentTooLongException;
import Exceptions.TitleTooLongException;
import Exceptions.UserIDMustBeAtLeastEightCharactersException;

public class AddCommentRecordController {
    public void addRecord(String createdBy, String title, String comment, Date date, String patientId, String problemUUID) throws CommentTooLongException,
            UserIDMustBeAtLeastEightCharactersException, TitleTooLongException, ExecutionException, InterruptedException {

        Record record = new Record(createdBy,title);
        record.setComment(comment);
        record.setDate(date);
        record.setAssociatedProblemUUID(problemUUID);

        new ElasticsearchRecordController.AddRecordTask().execute(record).get();
        Problem problem = new ElasticsearchProblemController.GetProblemByProblemUUIDTask().execute(problemUUID).get();
        if (problem.getCreatedByUserID().equals(patientId)) {
            problem.addRecord(record);
        }
        new ElasticsearchProblemController.SaveModifiedProblem().execute(problem).get();

        // TODO addOfflineSaveController

    }
}
