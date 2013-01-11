// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.Answer")
@ServiceName("medizin.server.domain.Answer")
public interface AnswerRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countAnswers();

    abstract Request<java.util.List<medizin.client.proxy.AnswerProxy>> findAllAnswers();

    abstract Request<java.util.List<medizin.client.proxy.AnswerProxy>> findAnswerEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.AnswerProxy> findAnswer(Long id);

    abstract InstanceRequest<medizin.client.proxy.AnswerProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.AnswerProxy, java.lang.Void> remove();
}
