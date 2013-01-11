package medizin.client.request;

import java.util.List;

import medizin.client.proxy.QuestionAccessProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.QuestionAccess")
@ServiceName("medizin.server.domain.QuestionAccess")
public interface QuestionAccessRequest extends QuestionAccessRequest_Roo_Gwt {
	
	Request<java.lang.Long> countQuestionEventAccessByPersonNonRoo(Long personId);
    Request<List<QuestionAccessProxy>> findQuestionEventAccessByPersonNonRooNonRoo(java.lang.Long personId, int firstResult, int maxResults);
	Request<List<QuestionAccessProxy>> findQuestionAccessQuestionByPersonNonRoo(Long personId, int firstResult, int maxResults);
	Request<java.lang.Long>  countQuestionAccessQuestionByPersonNonRoo(Long personId);
}
