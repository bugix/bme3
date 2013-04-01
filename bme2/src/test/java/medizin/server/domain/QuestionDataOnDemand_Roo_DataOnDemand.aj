// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import medizin.server.domain.CommentDataOnDemand;
import medizin.server.domain.Person;
import medizin.server.domain.PersonDataOnDemand;
import medizin.server.domain.Question;
import medizin.server.domain.QuestionDataOnDemand;
import medizin.server.domain.QuestionEvent;
import medizin.server.domain.QuestionEventDataOnDemand;
import medizin.server.domain.QuestionType;
import medizin.server.domain.QuestionTypeDataOnDemand;
import medizin.shared.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect QuestionDataOnDemand_Roo_DataOnDemand {
    
    declare @type: QuestionDataOnDemand: @Component;
    
    private Random QuestionDataOnDemand.rnd = new SecureRandom();
    
    private List<Question> QuestionDataOnDemand.data;
    
    @Autowired
    PersonDataOnDemand QuestionDataOnDemand.personDataOnDemand;
    
    @Autowired
    CommentDataOnDemand QuestionDataOnDemand.commentDataOnDemand;
    
    @Autowired
    QuestionEventDataOnDemand QuestionDataOnDemand.questionEventDataOnDemand;
    
    @Autowired
    QuestionTypeDataOnDemand QuestionDataOnDemand.questionTypeDataOnDemand;
    
    public Question QuestionDataOnDemand.getNewTransientQuestion(int index) {
        Question obj = new Question();
        setAutor(obj, index);
        setDateAdded(obj, index);
        setDateChanged(obj, index);
        setIsAcceptedAdmin(obj, index);
        setIsAcceptedAuthor(obj, index);
        setIsAcceptedRewiever(obj, index);
        setIsReadOnly(obj, index);
        setPicturePath(obj, index);
        setPreviousVersion(obj, index);
        setQuestEvent(obj, index);
        setQuestionShortName(obj, index);
        setQuestionSubVersion(obj, index);
        setQuestionText(obj, index);
        setQuestionType(obj, index);
        setQuestionVersion(obj, index);
        setStatus(obj, index);
        setSubmitToReviewComitee(obj, index);
        return obj;
    }
    
    public void QuestionDataOnDemand.setAutor(Question obj, int index) {
        Person autor = personDataOnDemand.getRandomPerson();
        obj.setAutor(autor);
    }
    
    public void QuestionDataOnDemand.setDateAdded(Question obj, int index) {
        Date dateAdded = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateAdded(dateAdded);
    }
    
    public void QuestionDataOnDemand.setDateChanged(Question obj, int index) {
        Date dateChanged = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateChanged(dateChanged);
    }
    
    public void QuestionDataOnDemand.setIsAcceptedAdmin(Question obj, int index) {
        Boolean isAcceptedAdmin = Boolean.TRUE;
        obj.setIsAcceptedAdmin(isAcceptedAdmin);
    }
    
    public void QuestionDataOnDemand.setIsAcceptedAuthor(Question obj, int index) {
        Boolean isAcceptedAuthor = Boolean.TRUE;
        obj.setIsAcceptedAuthor(isAcceptedAuthor);
    }
    
    public void QuestionDataOnDemand.setIsAcceptedRewiever(Question obj, int index) {
        Boolean isAcceptedRewiever = Boolean.TRUE;
        obj.setIsAcceptedRewiever(isAcceptedRewiever);
    }
    
    public void QuestionDataOnDemand.setIsReadOnly(Question obj, int index) {
        Boolean isReadOnly = Boolean.TRUE;
        obj.setIsReadOnly(isReadOnly);
    }
    
    public void QuestionDataOnDemand.setPicturePath(Question obj, int index) {
        String picturePath = "picturePath_" + index;
        if (picturePath.length() > 255) {
            picturePath = picturePath.substring(0, 255);
        }
        obj.setPicturePath(picturePath);
    }
    
    public void QuestionDataOnDemand.setPreviousVersion(Question obj, int index) {
        Question previousVersion = obj;
        obj.setPreviousVersion(previousVersion);
    }
    
    public void QuestionDataOnDemand.setQuestEvent(Question obj, int index) {
        QuestionEvent questEvent = questionEventDataOnDemand.getRandomQuestionEvent();
        obj.setQuestEvent(questEvent);
    }
    
    public void QuestionDataOnDemand.setQuestionShortName(Question obj, int index) {
        String questionShortName = "questionShortName_" + index;
        if (questionShortName.length() > 255) {
            questionShortName = questionShortName.substring(0, 255);
        }
        obj.setQuestionShortName(questionShortName);
    }
    
    public void QuestionDataOnDemand.setQuestionSubVersion(Question obj, int index) {
        Integer questionSubVersion = new Integer(index);
        obj.setQuestionSubVersion(questionSubVersion);
    }
    
    public void QuestionDataOnDemand.setQuestionText(Question obj, int index) {
        String questionText = "questionText_" + index;
        if (questionText.length() > 9000) {
            questionText = questionText.substring(0, 9000);
        }
        obj.setQuestionText(questionText);
    }
    
    public void QuestionDataOnDemand.setQuestionType(Question obj, int index) {
        QuestionType questionType = questionTypeDataOnDemand.getRandomQuestionType();
        obj.setQuestionType(questionType);
    }
    
    public void QuestionDataOnDemand.setQuestionVersion(Question obj, int index) {
        Integer questionVersion = new Integer(index);
        obj.setQuestionVersion(questionVersion);
    }
    
    public void QuestionDataOnDemand.setStatus(Question obj, int index) {
        Status status = null;
        obj.setStatus(status);
    }
    
    public void QuestionDataOnDemand.setSubmitToReviewComitee(Question obj, int index) {
        Boolean submitToReviewComitee = Boolean.TRUE;
        obj.setSubmitToReviewComitee(submitToReviewComitee);
    }
    
    public Question QuestionDataOnDemand.getSpecificQuestion(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Question obj = data.get(index);
        Long id = obj.getId();
        return Question.findQuestion(id);
    }
    
    public Question QuestionDataOnDemand.getRandomQuestion() {
        init();
        Question obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Question.findQuestion(id);
    }
    
    public boolean QuestionDataOnDemand.modifyQuestion(Question obj) {
        return false;
    }
    
    public void QuestionDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Question.findQuestionEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Question' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Question>();
        for (int i = 0; i < 10; i++) {
            Question obj = getNewTransientQuestion(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
