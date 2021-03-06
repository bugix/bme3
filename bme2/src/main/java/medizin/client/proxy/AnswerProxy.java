// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Date;
import medizin.shared.Status;
import medizin.shared.Validity;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.Answer", locator = "medizin.server.locator.AnswerLocator")
@RooGwtProxy(value = "medizin.server.domain.Answer", readOnly = { "version", "id" })
public interface AnswerProxy extends EntityProxy {

    abstract Long getId();

    abstract String getAnswerText();

    abstract void setAnswerText(String answerText);

    abstract Boolean getIsMedia();

    abstract void setIsMedia(Boolean isMedia);

    abstract Boolean getIsAnswerAcceptedReviewWahrer();

    abstract void setIsAnswerAcceptedReviewWahrer(Boolean isAnswerAcceptedReviewWahrer);

    abstract Boolean getIsAnswerAcceptedAutor();

    abstract void setIsAnswerAcceptedAutor(Boolean isAnswerAcceptedAutor);

    abstract Boolean getIsAnswerAcceptedAdmin();

    abstract void setIsAnswerAcceptedAdmin(Boolean isAnswerAcceptedAdmin);

    abstract Validity getValidity();

    abstract void setValidity(Validity validity);

    abstract String getMediaPath();

    abstract void setMediaPath(String mediaPath);

    abstract QuestionProxy getQuestion();

    abstract void setQuestion(QuestionProxy question);

    abstract Date getDateAdded();

    abstract void setDateAdded(Date dateAdded);

    abstract Date getDateChanged();

    abstract void setDateChanged(Date dateChanged);

    abstract PersonProxy getRewiewer();

    abstract void setRewiewer(PersonProxy rewiewer);

    abstract PersonProxy getAutor();

    abstract void setAutor(PersonProxy autor);

    abstract String getComment();

    abstract void setComment(String comment);

    abstract Boolean getSubmitToReviewComitee();

    abstract void setSubmitToReviewComitee(Boolean submitToReviewComitee);

    abstract String getPoints();

    abstract void setPoints(String points);

    abstract String getAdditionalKeywords();

    abstract void setAdditionalKeywords(String additionalKeywords);

    abstract Integer getSequenceNumber();

    abstract void setSequenceNumber(Integer sequenceNumber);

    abstract Status getStatus();

    abstract void setStatus(Status status);

    abstract Boolean getIsForcedActive();

    abstract void setIsForcedActive(Boolean isForcedActive);

    abstract PersonProxy getCreatedBy();

    abstract void setCreatedBy(PersonProxy createdBy);

    abstract PersonProxy getModifiedBy();

    abstract void setModifiedBy(PersonProxy modifiedBy);

    abstract Integer getVersion();
}
