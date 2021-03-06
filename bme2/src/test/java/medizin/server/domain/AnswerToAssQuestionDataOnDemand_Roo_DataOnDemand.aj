// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import medizin.server.domain.Answer;
import medizin.server.domain.AnswerDataOnDemand;
import medizin.server.domain.AnswerToAssQuestion;
import medizin.server.domain.AnswerToAssQuestionDataOnDemand;
import medizin.server.domain.AssesmentQuestion;
import medizin.server.domain.AssesmentQuestionDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect AnswerToAssQuestionDataOnDemand_Roo_DataOnDemand {
    
    declare @type: AnswerToAssQuestionDataOnDemand: @Component;
    
    private Random AnswerToAssQuestionDataOnDemand.rnd = new SecureRandom();
    
    private List<AnswerToAssQuestion> AnswerToAssQuestionDataOnDemand.data;
    
    @Autowired
    AnswerDataOnDemand AnswerToAssQuestionDataOnDemand.answerDataOnDemand;
    
    @Autowired
    AssesmentQuestionDataOnDemand AnswerToAssQuestionDataOnDemand.assesmentQuestionDataOnDemand;
    
    public AnswerToAssQuestion AnswerToAssQuestionDataOnDemand.getNewTransientAnswerToAssQuestion(int index) {
        AnswerToAssQuestion obj = new AnswerToAssQuestion();
        setAnswers(obj, index);
        setAssesmentQuestion(obj, index);
        setSortOrder(obj, index);
        return obj;
    }
    
    public void AnswerToAssQuestionDataOnDemand.setAnswers(AnswerToAssQuestion obj, int index) {
        Answer answers = answerDataOnDemand.getRandomAnswer();
        obj.setAnswers(answers);
    }
    
    public void AnswerToAssQuestionDataOnDemand.setAssesmentQuestion(AnswerToAssQuestion obj, int index) {
        AssesmentQuestion assesmentQuestion = assesmentQuestionDataOnDemand.getRandomAssesmentQuestion();
        obj.setAssesmentQuestion(assesmentQuestion);
    }
    
    public void AnswerToAssQuestionDataOnDemand.setSortOrder(AnswerToAssQuestion obj, int index) {
        Integer sortOrder = new Integer(index);
        obj.setSortOrder(sortOrder);
    }
    
    public AnswerToAssQuestion AnswerToAssQuestionDataOnDemand.getSpecificAnswerToAssQuestion(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        AnswerToAssQuestion obj = data.get(index);
        Long id = obj.getId();
        return AnswerToAssQuestion.findAnswerToAssQuestion(id);
    }
    
    public AnswerToAssQuestion AnswerToAssQuestionDataOnDemand.getRandomAnswerToAssQuestion() {
        init();
        AnswerToAssQuestion obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return AnswerToAssQuestion.findAnswerToAssQuestion(id);
    }
    
    public boolean AnswerToAssQuestionDataOnDemand.modifyAnswerToAssQuestion(AnswerToAssQuestion obj) {
        return false;
    }
    
    public void AnswerToAssQuestionDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = AnswerToAssQuestion.findAnswerToAssQuestionEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'AnswerToAssQuestion' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<AnswerToAssQuestion>();
        for (int i = 0; i < 10; i++) {
            AnswerToAssQuestion obj = getNewTransientAnswerToAssQuestion(i);
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
