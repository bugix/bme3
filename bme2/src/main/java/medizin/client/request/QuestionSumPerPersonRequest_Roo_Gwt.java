// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.QuestionSumPerPerson")
@ServiceName("medizin.server.domain.QuestionSumPerPerson")
public interface QuestionSumPerPersonRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countQuestionSumPerpeople();

    abstract Request<java.util.List<medizin.client.proxy.QuestionSumPerPersonProxy>> findAllQuestionSumPerpeople();

    abstract Request<java.util.List<medizin.client.proxy.QuestionSumPerPersonProxy>> findQuestionSumPerPersonEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.QuestionSumPerPersonProxy> findQuestionSumPerPerson(Long id);

    abstract InstanceRequest<medizin.client.proxy.QuestionSumPerPersonProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.QuestionSumPerPersonProxy, java.lang.Void> remove();
}
