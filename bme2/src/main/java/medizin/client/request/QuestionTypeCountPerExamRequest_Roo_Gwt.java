// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.QuestionTypeCountPerExam")
@ServiceName("medizin.server.domain.QuestionTypeCountPerExam")
public interface QuestionTypeCountPerExamRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countQuestionTypeCountPerExams();

    abstract Request<java.util.List<medizin.client.proxy.QuestionTypeCountPerExamProxy>> findAllQuestionTypeCountPerExams();

    abstract Request<java.util.List<medizin.client.proxy.QuestionTypeCountPerExamProxy>> findQuestionTypeCountPerExamEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.QuestionTypeCountPerExamProxy> findQuestionTypeCountPerExam(Long id);

    abstract InstanceRequest<medizin.client.proxy.QuestionTypeCountPerExamProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.QuestionTypeCountPerExamProxy, java.lang.Void> remove();
}
