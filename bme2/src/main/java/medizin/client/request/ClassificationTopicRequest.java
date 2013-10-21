package medizin.client.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

import medizin.client.proxy.ClassificationTopicProxy;
import medizin.server.domain.ClassificationTopic;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

@RooGwtUnmanagedRequest("medizin.server.domain.ClassificationTopic")
@ServiceName("medizin.server.domain.ClassificationTopic")
public interface ClassificationTopicRequest extends ClassificationTopicRequest_Roo_Gwt {

	Request<List<ClassificationTopicProxy>> findClassificationTopicByMainClassification(Long mainClassificationId);
}
