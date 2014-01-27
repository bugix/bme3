// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import medizin.shared.MultimediaType;
import medizin.shared.QuestionTypes;
import medizin.shared.SelectionType;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.QuestionType", locator = "medizin.server.locator.QuestionTypeLocator")
@RooGwtProxy(value = "medizin.server.domain.QuestionType", readOnly = { "version", "id" })
public interface QuestionTypeProxy extends EntityProxy {

    abstract Long getId();

    abstract String getShortName();

    abstract void setShortName(String shortName);

    abstract String getLongName();

    abstract void setLongName(String longName);

    abstract String getDescription();

    abstract void setDescription(String description);

    abstract InstitutionProxy getInstitution();

    abstract void setInstitution(InstitutionProxy institution);

    abstract QuestionTypes getQuestionType();

    abstract void setQuestionType(QuestionTypes questionType);

    abstract Integer getSumAnswer();

    abstract void setSumAnswer(Integer sumAnswer);

    abstract Integer getSumTrueAnswer();

    abstract void setSumTrueAnswer(Integer sumTrueAnswer);

    abstract Integer getSumFalseAnswer();

    abstract void setSumFalseAnswer(Integer sumFalseAnswer);

    abstract Integer getQuestionLength();

    abstract void setQuestionLength(Integer questionLength);

    abstract Integer getAnswerLength();

    abstract void setAnswerLength(Integer answerLength);

    abstract Double getDiffBetAnswer();

    abstract void setDiffBetAnswer(Double diffBetAnswer);

    abstract Boolean getQueHaveImage();

    abstract void setQueHaveImage(Boolean queHaveImage);

    abstract Boolean getQueHaveVideo();

    abstract void setQueHaveVideo(Boolean queHaveVideo);

    abstract Boolean getQueHaveSound();

    abstract void setQueHaveSound(Boolean queHaveSound);

    abstract Integer getKeywordCount();

    abstract void setKeywordCount(Integer keywordCount);

    abstract Boolean getShowAutocomplete();

    abstract void setShowAutocomplete(Boolean showAutocomplete);

    abstract Boolean getIsDictionaryKeyword();

    abstract void setIsDictionaryKeyword(Boolean isDictionaryKeyword);

    abstract Boolean getAllowTyping();

    abstract void setAllowTyping(Boolean allowTyping);

    abstract Integer getMinAutoCompleteLetter();

    abstract void setMinAutoCompleteLetter(Integer minAutoCompleteLetter);

    abstract Boolean getAcceptNonKeyword();

    abstract void setAcceptNonKeyword(Boolean acceptNonKeyword);

    abstract Integer getLengthShortAnswer();

    abstract void setLengthShortAnswer(Integer lengthShortAnswer);

    abstract Integer getImageWidth();

    abstract void setImageWidth(Integer imageWidth);

    abstract Integer getImageHeight();

    abstract void setImageHeight(Integer imageHeight);

    abstract String getImageProportion();

    abstract void setImageProportion(String imageProportion);

    abstract Boolean getAllowOneToOneAss();

    abstract void setAllowOneToOneAss(Boolean allowOneToOneAss);

    abstract Boolean getLinearPoint();

    abstract void setLinearPoint(Boolean linearPoint);

    abstract Double getLinearPercentage();

    abstract void setLinearPercentage(Double linearPercentage);

    abstract Boolean getRichText();

    abstract void setRichText(Boolean richText);

    abstract MultimediaType getMultimediaType();

    abstract void setMultimediaType(MultimediaType multimediaType);

    abstract SelectionType getSelectionType();

    abstract void setSelectionType(SelectionType selectionType);

    abstract Integer getColumns();

    abstract void setColumns(Integer columns);

    abstract Integer getThumbWidth();

    abstract void setThumbWidth(Integer thumbWidth);

    abstract Integer getThumbHeight();

    abstract void setThumbHeight(Integer thumbHeight);

    abstract Boolean getAllowZoomOut();

    abstract void setAllowZoomOut(Boolean allowZoomOut);

    abstract Boolean getAllowZoomIn();

    abstract void setAllowZoomIn(Boolean allowZoomIn);

    abstract Integer getMaxBytes();

    abstract void setMaxBytes(Integer maxBytes);

    abstract Boolean getKeywordHighlight();

    abstract void setKeywordHighlight(Boolean keywordHighlight);

    abstract Integer getMinLength();

    abstract void setMinLength(Integer minLength);

    abstract Integer getMaxLength();

    abstract void setMaxLength(Integer maxLength);

    abstract Integer getMinWordCount();

    abstract void setMinWordCount(Integer minWordCount);

    abstract Integer getMaxWordCount();

    abstract void setMaxWordCount(Integer maxWordCount);

    abstract Boolean getShowFilterDialog();

    abstract void setShowFilterDialog(Boolean showFilterDialog);

    abstract Integer getVersion();
}
