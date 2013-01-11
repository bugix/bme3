// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Date;
import java.util.Set;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.Question", locator = "medizin.server.locator.QuestionLocator")
@RooGwtProxy(value = "medizin.server.domain.Question", readOnly = { "version", "id" })
public interface QuestionProxy extends EntityProxy {

    abstract Long getId();

    abstract String getQuestionText();

    abstract void setQuestionText(String questionText);

    abstract String getPicturePath();

    abstract void setPicturePath(String picturePath);

    abstract Double getQuestionVersion();

    abstract void setQuestionVersion(Double questionVersion);

    abstract Boolean getIsAcceptedRewiever();

    abstract void setIsAcceptedRewiever(Boolean isAcceptedRewiever);

    abstract Boolean getIsAcceptedAdmin();

    abstract void setIsAcceptedAdmin(Boolean isAcceptedAdmin);

    abstract Boolean getIsActive();

    abstract void setIsActive(Boolean isActive);

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

    abstract Set<medizin.client.proxy.McProxy> getMcs();

    abstract void setMcs(Set<medizin.client.proxy.McProxy> mcs);

    abstract Set<medizin.client.proxy.AnswerProxy> getAnswers();

    abstract void setAnswers(Set<medizin.client.proxy.AnswerProxy> answers);

    abstract Date getDateAdded();

    abstract void setDateAdded(Date dateAdded);

    abstract Date getDateChanged();

    abstract void setDateChanged(Date dateChanged);

    abstract PersonProxy getRewiewer();

    abstract void setRewiewer(PersonProxy rewiewer);

    abstract PersonProxy getAutor();

    abstract void setAutor(PersonProxy autor);

    abstract Integer getVersion();
}
