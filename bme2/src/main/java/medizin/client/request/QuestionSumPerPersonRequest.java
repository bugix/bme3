package medizin.client.request;

import java.util.List;

import medizin.client.proxy.QuestionSumPerPersonProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.QuestionSumPerPerson")
@ServiceName("medizin.server.domain.QuestionSumPerPerson")
public interface QuestionSumPerPersonRequest extends QuestionSumPerPersonRequest_Roo_Gwt {
	
	Request<java.lang.Long>  countQuestionSumPerPersonByAssesmentNonRoo(Long assesmentId);
	Request<List<QuestionSumPerPersonProxy>>  findQuestionSumPerPersonByAssesmentNonRoo(Long assesmentId, int start, int max);
	InstanceRequest<QuestionSumPerPersonProxy, Void> moveUp();
	InstanceRequest<QuestionSumPerPersonProxy, Void> moveDown();
	Request<QuestionSumPerPersonProxy>  findQuestionSumPerPersonByEventNonRoo(Long questionEventId);
}
