// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.AnswerToAssQuestion", locator = "medizin.server.locator.AnswerToAssQuestionLocator")
@RooGwtProxy(value = "medizin.server.domain.AnswerToAssQuestion", readOnly = { "version", "id" })
public interface AnswerToAssQuestionProxy extends EntityProxy {

    abstract Long getId();

    abstract Integer getSortOrder();

    abstract void setSortOrder(Integer sortOrder);

    abstract AnswerProxy getAnswers();

    abstract void setAnswers(AnswerProxy answers);

    abstract AssesmentQuestionProxy getAssesmentQuestion();

    abstract void setAssesmentQuestion(AssesmentQuestionProxy assesmentQuestion);

    abstract Integer getVersion();
}
