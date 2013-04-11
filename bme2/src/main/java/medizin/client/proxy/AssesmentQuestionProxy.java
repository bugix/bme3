// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Date;
import java.util.Set;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.AssesmentQuestion", locator = "medizin.server.locator.AssesmentQuestionLocator")
@RooGwtProxy(value = "medizin.server.domain.AssesmentQuestion", readOnly = { "version", "id" })
public interface AssesmentQuestionProxy extends EntityProxy {

    abstract Long getId();

    abstract Integer getOrderAversion();

    abstract void setOrderAversion(Integer orderAversion);

    abstract Integer getOrderBversion();

    abstract void setOrderBversion(Integer orderBversion);

    abstract Double getTrenschaerfe();

    abstract void setTrenschaerfe(Double trenschaerfe);

    abstract Double getSchwierigkeit();

    abstract void setSchwierigkeit(Double schwierigkeit);

    abstract Boolean getIsAssQuestionAcceptedRewiever();

    abstract void setIsAssQuestionAcceptedRewiever(Boolean isAssQuestionAcceptedRewiever);

    abstract Boolean getIsAssQuestionAcceptedAdmin();

    abstract void setIsAssQuestionAcceptedAdmin(Boolean isAssQuestionAcceptedAdmin);

    abstract Boolean getIsAssQuestionAdminProposal();

    abstract void setIsAssQuestionAdminProposal(Boolean isAssQuestionAdminProposal);

    abstract Boolean getIsAssQuestionAcceptedAutor();

    abstract void setIsAssQuestionAcceptedAutor(Boolean isAssQuestionAcceptedAutor);

    abstract Boolean getIsForcedByAdmin();

    abstract void setIsForcedByAdmin(Boolean isForcedByAdmin);

    abstract QuestionProxy getQuestion();

    abstract void setQuestion(QuestionProxy question);

    abstract AssesmentProxy getAssesment();

    abstract void setAssesment(AssesmentProxy assesment);

    abstract Set<medizin.client.proxy.AnswerToAssQuestionProxy> getAnswersToAssQuestion();

    abstract void setAnswersToAssQuestion(Set<medizin.client.proxy.AnswerToAssQuestionProxy> answersToAssQuestion);

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
