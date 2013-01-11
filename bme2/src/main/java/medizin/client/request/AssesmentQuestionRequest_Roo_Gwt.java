// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.AssesmentQuestion")
@ServiceName("medizin.server.domain.AssesmentQuestion")
public interface AssesmentQuestionRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countAssesmentQuestions();

    abstract Request<java.util.List<medizin.client.proxy.AssesmentQuestionProxy>> findAllAssesmentQuestions();

    abstract Request<java.util.List<medizin.client.proxy.AssesmentQuestionProxy>> findAssesmentQuestionEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.AssesmentQuestionProxy> findAssesmentQuestion(Long id);

    abstract InstanceRequest<medizin.client.proxy.AssesmentQuestionProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.AssesmentQuestionProxy, java.lang.Void> remove();
}
