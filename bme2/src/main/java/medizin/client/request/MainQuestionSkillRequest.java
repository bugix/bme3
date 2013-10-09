package medizin.client.request;

import java.util.List;

import medizin.client.proxy.MainQuestionSkillProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.MainQuestionSkill")
@ServiceName("medizin.server.domain.MainQuestionSkill")
public interface MainQuestionSkillRequest extends MainQuestionSkillRequest_Roo_Gwt {
	
	Request<Integer> countMainQuestionSkillByQuestionId(Long questionId);
	
	Request<List<MainQuestionSkillProxy>> findMainQuestionSkillByQuestionId(Long questionId, int start, int length);
}
