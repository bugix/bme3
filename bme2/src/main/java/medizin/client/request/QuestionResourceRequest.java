package medizin.client.request;

import java.util.Set;

import medizin.client.proxy.QuestionResourceProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.QuestionResource")
@ServiceName("medizin.server.domain.QuestionResource")
public interface QuestionResourceRequest extends QuestionResourceRequest_Roo_Gwt {

	Request<Void> persistSet(Set<QuestionResourceProxy> questionResources);

	Request<Void> removeSelectedQuestionResource(Long qestionResourceId);

	Request<Void> deleteFiles(Set<String> paths);
}
