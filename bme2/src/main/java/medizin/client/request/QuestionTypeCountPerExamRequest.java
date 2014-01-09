package medizin.client.request;

import java.util.List;

import medizin.client.proxy.QuestionTypeCountPerExamProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.QuestionTypeCountPerExam")
@ServiceName("medizin.server.domain.QuestionTypeCountPerExam")
public interface QuestionTypeCountPerExamRequest extends QuestionTypeCountPerExamRequest_Roo_Gwt {
	
	Request<java.lang.Long> countQuestionTypeCountByAssesmentNonRoo(Long id);
	Request<List<QuestionTypeCountPerExamProxy>> findQuestionTypeCountByAssesmentNonRoo(Long id, int start, int max);
//	InstanceRequest<medizin.client.managed.request.QuestionTypeCountPerExamProxy, java.lang.Void>  moveUp();
//	InstanceRequest<medizin.client.managed.request.QuestionTypeCountPerExamProxy, java.lang.Void>  moveDown();
	Request<QuestionTypeCountPerExamProxy> findQuestionTypeCountByAssesmentAndOrderNonRoo(Long assesmentId, int order);
	Request<List<QuestionTypeCountPerExamProxy>> findQuestionTypesCountSortedByAssesmentNonRoo(Long assesmentId);
	InstanceRequest<QuestionTypeCountPerExamProxy, Void> moveUp();
	InstanceRequest<QuestionTypeCountPerExamProxy, Void> moveDown();
	Request<Boolean> removeAndUpdateOrder(QuestionTypeCountPerExamProxy questionTypeCountPerExam);	    
	
}
