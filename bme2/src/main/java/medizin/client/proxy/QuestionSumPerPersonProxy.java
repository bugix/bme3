// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.QuestionSumPerPerson", locator = "medizin.server.locator.QuestionSumPerPersonLocator")
@RooGwtProxy(value = "medizin.server.domain.QuestionSumPerPerson", readOnly = { "version", "id" })
public interface QuestionSumPerPersonProxy extends EntityProxy {

    abstract Long getId();

    abstract PersonProxy getResponsiblePerson();

    abstract void setResponsiblePerson(PersonProxy responsiblePerson);

    abstract QuestionEventProxy getQuestionEvent();

    abstract void setQuestionEvent(QuestionEventProxy questionEvent);

    abstract AssesmentProxy getAssesment();

    abstract void setAssesment(AssesmentProxy assesment);

    abstract Integer getQuestionSum();

    abstract void setQuestionSum(Integer questionSum);

    abstract Integer getSort_order();

    abstract void setSort_order(Integer sort_order);

    abstract Integer getVersion();
}
