// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Date;
import java.util.Set;
import medizin.shared.Status;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.Question", locator = "medizin.server.locator.QuestionLocator")
@RooGwtProxy(value = "medizin.server.domain.Question", readOnly = { "version", "id" })
public interface QuestionProxy extends EntityProxy {

    abstract Long getId();

    abstract String getQuestionShortName();

    abstract void setQuestionShortName(String questionShortName);

    abstract String getQuestionText();

    abstract void setQuestionText(String questionText);

    abstract Integer getQuestionVersion();

    abstract void setQuestionVersion(Integer questionVersion);

    abstract Integer getQuestionSubVersion();

    abstract void setQuestionSubVersion(Integer questionSubVersion);

    abstract Boolean getIsAcceptedRewiever();

    abstract void setIsAcceptedRewiever(Boolean isAcceptedRewiever);

    abstract Boolean getSubmitToReviewComitee();

    abstract void setSubmitToReviewComitee(Boolean submitToReviewComitee);

    abstract Boolean getIsAcceptedAdmin();

    abstract void setIsAcceptedAdmin(Boolean isAcceptedAdmin);

    abstract Boolean getIsAcceptedAuthor();

    abstract void setIsAcceptedAuthor(Boolean isAcceptedAuthor);

    abstract Boolean getIsForcedActive();

    abstract void setIsForcedActive(Boolean isForcedActive);

    abstract medizin.client.proxy.QuestionProxy getPreviousVersion();

    abstract void setPreviousVersion(medizin.client.proxy.QuestionProxy previousVersion);

    abstract Set<medizin.client.proxy.KeywordProxy> getKeywords();

    abstract void setKeywords(Set<medizin.client.proxy.KeywordProxy> keywords);

    abstract QuestionEventProxy getQuestEvent();

    abstract void setQuestEvent(QuestionEventProxy questEvent);

    abstract CommentProxy getComment();

    abstract void setComment(CommentProxy comment);

    abstract QuestionTypeProxy getQuestionType();

    abstract void setQuestionType(QuestionTypeProxy questionType);

    abstract Status getStatus();

    abstract void setStatus(Status status);

    abstract Set<medizin.client.proxy.McProxy> getMcs();

    abstract void setMcs(Set<medizin.client.proxy.McProxy> mcs);

    abstract Set<medizin.client.proxy.AnswerProxy> getAnswers();

    abstract void setAnswers(Set<medizin.client.proxy.AnswerProxy> answers);

    abstract Set<medizin.client.proxy.QuestionResourceProxy> getQuestionResources();

    abstract void setQuestionResources(Set<medizin.client.proxy.QuestionResourceProxy> questionResources);

    abstract Set<medizin.client.proxy.UserAccessRightsProxy> getQuestionAccess();

    abstract void setQuestionAccess(Set<medizin.client.proxy.UserAccessRightsProxy> questionAccess);

    abstract Date getDateAdded();

    abstract void setDateAdded(Date dateAdded);

    abstract Date getDateChanged();

    abstract void setDateChanged(Date dateChanged);

    abstract PersonProxy getRewiewer();

    abstract void setRewiewer(PersonProxy rewiewer);

    abstract PersonProxy getAutor();

    abstract void setAutor(PersonProxy autor);

    abstract Boolean getIsReadOnly();

    abstract void setIsReadOnly(Boolean isReadOnly);

    abstract Set<medizin.client.proxy.AssesmentQuestionProxy> getAssesmentQuestionSet();

    abstract void setAssesmentQuestionSet(Set<medizin.client.proxy.AssesmentQuestionProxy> assesmentQuestionSet);

    abstract PersonProxy getCreatedBy();

    abstract void setCreatedBy(PersonProxy createdBy);

    abstract PersonProxy getModifiedBy();

    abstract void setModifiedBy(PersonProxy modifiedBy);

    abstract Integer getVersion();
}
