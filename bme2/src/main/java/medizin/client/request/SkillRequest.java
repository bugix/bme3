package medizin.client.request;

import java.util.List;

import medizin.client.proxy.SkillProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.Skill")
@ServiceName("medizin.server.domain.Skill")
public interface SkillRequest extends SkillRequest_Roo_Gwt {
	
	Request<Integer> countSkillBySearchCriteria(Long mainClassificationId, Long classificationTopicId, Long topicId, Long skillLevlId, Long applianceId);
	
	Request<List<SkillProxy>> findSkillBySearchCriteria(int start, int length, Long mainClassificationId, Long classificationTopicId, Long topicId, Long skillLevlId, Long applianceId);
}
