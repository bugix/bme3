// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Set;
import medizin.shared.BlockingTypes;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.QuestionTypeCountPerExam", locator = "medizin.server.locator.QuestionTypeCountPerExamLocator")
@RooGwtProxy(value = "medizin.server.domain.QuestionTypeCountPerExam", readOnly = { "version", "id" })
public interface QuestionTypeCountPerExamProxy extends EntityProxy {

    abstract Long getId();

    abstract Set<medizin.client.proxy.QuestionTypeProxy> getQuestionTypesAssigned();

    abstract void setQuestionTypesAssigned(Set<medizin.client.proxy.QuestionTypeProxy> questionTypesAssigned);

    abstract AssesmentProxy getAssesment();

    abstract void setAssesment(AssesmentProxy assesment);

    abstract Integer getQuestionTypeCount();

    abstract void setQuestionTypeCount(Integer questionTypeCount);

    abstract Integer getSort_order();

    abstract void setSort_order(Integer sort_order);

    abstract BlockingTypes getBlockingType();

    abstract void setBlockingType(BlockingTypes blockingType);

    abstract Integer getVersion();
}
