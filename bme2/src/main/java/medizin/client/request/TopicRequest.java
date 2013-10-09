package medizin.client.request;

import java.util.List;

import medizin.client.proxy.TopicProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.Topic")
@ServiceName("medizin.server.domain.Topic")
public interface TopicRequest extends TopicRequest_Roo_Gwt {
	
	Request<List<TopicProxy>> findTopicByClassificationTopicId(Long classificationTopicId);
}
