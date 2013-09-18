package medizin.client.proxy;

import java.util.List;

import medizin.shared.utils.PersonAccessRight;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = PersonAccessRight.class)
public interface PersonAccessRightProxy extends ValueProxy {

	public Boolean getIsAdmin();
	
	public void setIsAdmin(Boolean isAdmin);
	
	public Boolean getIsInstitutionalAdmin();

	public void setIsInstitutionalAdmin(Boolean isInstitutionalAdmin);

	public List<UserAccessRightsProxy> getQuestionEventAccList();

	public void setQuestionEventAccList(List<UserAccessRightsProxy> questionEventAccList);
	
	public List<UserAccessRightsProxy> getQuestionAccList();

	public void setQuestionAccList(List<UserAccessRightsProxy> questionAccList);
}
