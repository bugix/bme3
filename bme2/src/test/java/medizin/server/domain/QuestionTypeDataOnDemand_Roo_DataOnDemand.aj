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
import medizin.server.domain.InstitutionDataOnDemand;
import medizin.server.domain.QuestionType;
import medizin.server.domain.QuestionTypeDataOnDemand;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.SelectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect QuestionTypeDataOnDemand_Roo_DataOnDemand {
    
    declare @type: QuestionTypeDataOnDemand: @Component;
    
    private Random QuestionTypeDataOnDemand.rnd = new SecureRandom();
    
    private List<QuestionType> QuestionTypeDataOnDemand.data;
    
    @Autowired
    InstitutionDataOnDemand QuestionTypeDataOnDemand.institutionDataOnDemand;
    
    public QuestionType QuestionTypeDataOnDemand.getNewTransientQuestionType(int index) {
        QuestionType obj = new QuestionType();
        setAcceptNonKeyword(obj, index);
        setAllowOneToOneAss(obj, index);
        setAllowTyping(obj, index);
        setAllowZoomIn(obj, index);
        setAllowZoomOut(obj, index);
        setAnswerLength(obj, index);
        setColumns(obj, index);
        setDescription(obj, index);
        setDiffBetAnswer(obj, index);
        setImageHeight(obj, index);
        setImageProportion(obj, index);
        setImageWidth(obj, index);
        setIsDictionaryKeyword(obj, index);
        setKeywordCount(obj, index);
        setKeywordHighlight(obj, index);
        setLengthShortAnswer(obj, index);
        setLinearPercentage(obj, index);
        setLinearPoint(obj, index);
        setLongName(obj, index);
        setMaxBytes(obj, index);
        setMaxLength(obj, index);
        setMaxWordCount(obj, index);
        setMinAutoCompleteLetter(obj, index);
        setMinLength(obj, index);
        setMinWordCount(obj, index);
        setMultimediaType(obj, index);
        setQueHaveImage(obj, index);
        setQueHaveSound(obj, index);
        setQueHaveVideo(obj, index);
        setQuestionLength(obj, index);
        setQuestionType(obj, index);
        setRichText(obj, index);
        setSelectionType(obj, index);
        setShortName(obj, index);
        setShowAutocomplete(obj, index);
        setShowFilterDialog(obj, index);
        setSumAnswer(obj, index);
        setSumFalseAnswer(obj, index);
        setSumTrueAnswer(obj, index);
        setThumbHeight(obj, index);
        setThumbWidth(obj, index);
        return obj;
    }
    
    public void QuestionTypeDataOnDemand.setAcceptNonKeyword(QuestionType obj, int index) {
        Boolean acceptNonKeyword = Boolean.TRUE;
        obj.setAcceptNonKeyword(acceptNonKeyword);
    }
    
    public void QuestionTypeDataOnDemand.setAllowOneToOneAss(QuestionType obj, int index) {
        Boolean allowOneToOneAss = Boolean.TRUE;
        obj.setAllowOneToOneAss(allowOneToOneAss);
    }
    
    public void QuestionTypeDataOnDemand.setAllowTyping(QuestionType obj, int index) {
        Boolean allowTyping = Boolean.TRUE;
        obj.setAllowTyping(allowTyping);
    }
    
    public void QuestionTypeDataOnDemand.setAllowZoomIn(QuestionType obj, int index) {
        Boolean allowZoomIn = Boolean.TRUE;
        obj.setAllowZoomIn(allowZoomIn);
    }
    
    public void QuestionTypeDataOnDemand.setAllowZoomOut(QuestionType obj, int index) {
        Boolean allowZoomOut = Boolean.TRUE;
        obj.setAllowZoomOut(allowZoomOut);
    }
    
    public void QuestionTypeDataOnDemand.setAnswerLength(QuestionType obj, int index) {
        Integer answerLength = new Integer(index);
        obj.setAnswerLength(answerLength);
    }
    
    public void QuestionTypeDataOnDemand.setColumns(QuestionType obj, int index) {
        Integer columns = new Integer(index);
        obj.setColumns(columns);
    }
    
    public void QuestionTypeDataOnDemand.setDescription(QuestionType obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void QuestionTypeDataOnDemand.setDiffBetAnswer(QuestionType obj, int index) {
        Double diffBetAnswer = new Integer(index).doubleValue();
        obj.setDiffBetAnswer(diffBetAnswer);
    }
    
    public void QuestionTypeDataOnDemand.setImageHeight(QuestionType obj, int index) {
        Integer imageHeight = new Integer(index);
        obj.setImageHeight(imageHeight);
    }
    
    public void QuestionTypeDataOnDemand.setImageProportion(QuestionType obj, int index) {
        String imageProportion = "imageProportion_" + index;
        obj.setImageProportion(imageProportion);
    }
    
    public void QuestionTypeDataOnDemand.setImageWidth(QuestionType obj, int index) {
        Integer imageWidth = new Integer(index);
        obj.setImageWidth(imageWidth);
    }
    
    public void QuestionTypeDataOnDemand.setIsDictionaryKeyword(QuestionType obj, int index) {
        Boolean isDictionaryKeyword = Boolean.TRUE;
        obj.setIsDictionaryKeyword(isDictionaryKeyword);
    }
    
    public void QuestionTypeDataOnDemand.setKeywordCount(QuestionType obj, int index) {
        Integer keywordCount = new Integer(index);
        obj.setKeywordCount(keywordCount);
    }
    
    public void QuestionTypeDataOnDemand.setKeywordHighlight(QuestionType obj, int index) {
        Boolean keywordHighlight = Boolean.TRUE;
        obj.setKeywordHighlight(keywordHighlight);
    }
    
    public void QuestionTypeDataOnDemand.setLengthShortAnswer(QuestionType obj, int index) {
        Integer lengthShortAnswer = new Integer(index);
        obj.setLengthShortAnswer(lengthShortAnswer);
    }
    
    public void QuestionTypeDataOnDemand.setLinearPercentage(QuestionType obj, int index) {
        Double linearPercentage = new Integer(index).doubleValue();
        obj.setLinearPercentage(linearPercentage);
    }
    
    public void QuestionTypeDataOnDemand.setLinearPoint(QuestionType obj, int index) {
        Boolean linearPoint = Boolean.TRUE;
        obj.setLinearPoint(linearPoint);
    }
    
    public void QuestionTypeDataOnDemand.setLongName(QuestionType obj, int index) {
        String longName = "longName_" + index;
        obj.setLongName(longName);
    }
    
    public void QuestionTypeDataOnDemand.setMaxBytes(QuestionType obj, int index) {
        Integer maxBytes = new Integer(index);
        obj.setMaxBytes(maxBytes);
    }
    
    public void QuestionTypeDataOnDemand.setMaxLength(QuestionType obj, int index) {
        Integer maxLength = new Integer(index);
        obj.setMaxLength(maxLength);
    }
    
    public void QuestionTypeDataOnDemand.setMaxWordCount(QuestionType obj, int index) {
        Integer maxWordCount = new Integer(index);
        obj.setMaxWordCount(maxWordCount);
    }
    
    public void QuestionTypeDataOnDemand.setMinAutoCompleteLetter(QuestionType obj, int index) {
        Integer minAutoCompleteLetter = new Integer(index);
        obj.setMinAutoCompleteLetter(minAutoCompleteLetter);
    }
    
    public void QuestionTypeDataOnDemand.setMinLength(QuestionType obj, int index) {
        Integer minLength = new Integer(index);
        obj.setMinLength(minLength);
    }
    
    public void QuestionTypeDataOnDemand.setMinWordCount(QuestionType obj, int index) {
        Integer minWordCount = new Integer(index);
        obj.setMinWordCount(minWordCount);
    }
    
    public void QuestionTypeDataOnDemand.setMultimediaType(QuestionType obj, int index) {
        MultimediaType multimediaType = null;
        obj.setMultimediaType(multimediaType);
    }
    
    public void QuestionTypeDataOnDemand.setQueHaveImage(QuestionType obj, int index) {
        Boolean queHaveImage = Boolean.TRUE;
        obj.setQueHaveImage(queHaveImage);
    }
    
    public void QuestionTypeDataOnDemand.setQueHaveSound(QuestionType obj, int index) {
        Boolean queHaveSound = Boolean.TRUE;
        obj.setQueHaveSound(queHaveSound);
    }
    
    public void QuestionTypeDataOnDemand.setQueHaveVideo(QuestionType obj, int index) {
        Boolean queHaveVideo = Boolean.TRUE;
        obj.setQueHaveVideo(queHaveVideo);
    }
    
    public void QuestionTypeDataOnDemand.setQuestionLength(QuestionType obj, int index) {
        Integer questionLength = new Integer(index);
        obj.setQuestionLength(questionLength);
    }
    
    public void QuestionTypeDataOnDemand.setQuestionType(QuestionType obj, int index) {
        QuestionTypes questionType = null;
        obj.setQuestionType(questionType);
    }
    
    public void QuestionTypeDataOnDemand.setRichText(QuestionType obj, int index) {
        Boolean richText = Boolean.TRUE;
        obj.setRichText(richText);
    }
    
    public void QuestionTypeDataOnDemand.setSelectionType(QuestionType obj, int index) {
        SelectionType selectionType = null;
        obj.setSelectionType(selectionType);
    }
    
    public void QuestionTypeDataOnDemand.setShortName(QuestionType obj, int index) {
        String shortName = "shortName_" + index;
        obj.setShortName(shortName);
    }
    
    public void QuestionTypeDataOnDemand.setShowAutocomplete(QuestionType obj, int index) {
        Boolean showAutocomplete = Boolean.TRUE;
        obj.setShowAutocomplete(showAutocomplete);
    }
    
    public void QuestionTypeDataOnDemand.setShowFilterDialog(QuestionType obj, int index) {
        Boolean showFilterDialog = Boolean.TRUE;
        obj.setShowFilterDialog(showFilterDialog);
    }
    
    public void QuestionTypeDataOnDemand.setSumAnswer(QuestionType obj, int index) {
        Integer sumAnswer = new Integer(index);
        obj.setSumAnswer(sumAnswer);
    }
    
    public void QuestionTypeDataOnDemand.setSumFalseAnswer(QuestionType obj, int index) {
        Integer sumFalseAnswer = new Integer(index);
        obj.setSumFalseAnswer(sumFalseAnswer);
    }
    
    public void QuestionTypeDataOnDemand.setSumTrueAnswer(QuestionType obj, int index) {
        Integer sumTrueAnswer = new Integer(index);
        obj.setSumTrueAnswer(sumTrueAnswer);
    }
    
    public void QuestionTypeDataOnDemand.setThumbHeight(QuestionType obj, int index) {
        Integer thumbHeight = new Integer(index);
        obj.setThumbHeight(thumbHeight);
    }
    
    public void QuestionTypeDataOnDemand.setThumbWidth(QuestionType obj, int index) {
        Integer thumbWidth = new Integer(index);
        obj.setThumbWidth(thumbWidth);
    }
    
    public QuestionType QuestionTypeDataOnDemand.getSpecificQuestionType(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        QuestionType obj = data.get(index);
        Long id = obj.getId();
        return QuestionType.findQuestionType(id);
    }
    
    public QuestionType QuestionTypeDataOnDemand.getRandomQuestionType() {
        init();
        QuestionType obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return QuestionType.findQuestionType(id);
    }
    
    public boolean QuestionTypeDataOnDemand.modifyQuestionType(QuestionType obj) {
        return false;
    }
    
    public void QuestionTypeDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = QuestionType.findQuestionTypeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'QuestionType' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<QuestionType>();
        for (int i = 0; i < 10; i++) {
            QuestionType obj = getNewTransientQuestionType(i);
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
