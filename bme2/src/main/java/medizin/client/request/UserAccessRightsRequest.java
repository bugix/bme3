package medizin.client.request;

import java.util.List;

import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.UserAccessRightsProxy;
import medizin.shared.AccessRights;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.UserAccessRights")
@ServiceName("medizin.server.domain.UserAccessRights")
public interface UserAccessRightsRequest extends UserAccessRightsRequest_Roo_Gwt {
	
	Request<java.lang.Long> countQuestionEventAccessByPersonNonRoo(Long personId);
    Request<List<UserAccessRightsProxy>> findQuestionEventAccessByPersonNonRooNonRoo(java.lang.Long personId, int firstResult, int maxResults);
	Request<List<UserAccessRightsProxy>> findQuestionAccessQuestionByPersonNonRoo(Long personId, int firstResult, int maxResults);
	Request<java.lang.Long>  countQuestionAccessQuestionByPersonNonRoo(Long personId);
	
	Request<List<UserAccessRightsProxy>> findInstiuteAccessByPerson(Long personId, int firstResult, int maxResults);
	Request<java.lang.Long>  countInstiuteAccessByPerson(Long personId);
	
	
	Request<Boolean> checkInstitutionalAdmin();
	
	Request<List<InstitutionProxy>> findInstituionFromQuestionAccessByPerson(Long personId);

	Request<List<UserAccessRightsProxy>> checkAddAnswerRightsByQuestionAndPerson(Long personid, Long questionid);
	
	Request<List<UserAccessRightsProxy>> findQuestionEventAccessByPerson(Long personId);
	
	Request<List<UserAccessRightsProxy>> findQuestionAccessByPerson(Long personId);
	
	Request<Boolean> persistQuestionEventAccess(AccessRights rights, Long personId, Long questionEventId);
	
	Request<Boolean> persistQuestionAccess(AccessRights rights, Long personId, Long questionId);
}
