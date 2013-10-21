package medizin.client.request;

import java.util.List;

import medizin.client.proxy.MinorQuestionSkillProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.MinorQuestionSkill")
@ServiceName("medizin.server.domain.MinorQuestionSkill")
public interface MinorQuestionSkillRequest extends MinorQuestionSkillRequest_Roo_Gwt {
	
	Request<Integer> countMinorQuestionSkillByQuestionId(Long questionId);
	
	Request<List<MinorQuestionSkillProxy>> findMinorQuestionSkillByQuestionId(Long questionId, int start, int length);
}
