package medizin.shared.utils;

import java.util.ArrayList;
import java.util.List;

import medizin.server.domain.UserAccessRights;

public class PersonAccessRight {

	private Boolean isAdmin = false;
		
	private Boolean isInstitutionalAdmin = false;
	
	private List<UserAccessRights> questionEventAccList = new ArrayList<UserAccessRights>();
	
	private List<UserAccessRights> questionAccList = new ArrayList<UserAccessRights>();

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Boolean getIsInstitutionalAdmin() {
		return isInstitutionalAdmin;
	}

	public void setIsInstitutionalAdmin(Boolean isInstitutionalAdmin) {
		this.isInstitutionalAdmin = isInstitutionalAdmin;
	}

	public List<UserAccessRights> getQuestionEventAccList() {
		return questionEventAccList;
	}

	public void setQuestionEventAccList(List<UserAccessRights> questionEventAccList) {
		this.questionEventAccList = questionEventAccList;
	}

	public List<UserAccessRights> getQuestionAccList() {
		return questionAccList;
	}

	public void setQuestionAccList(List<UserAccessRights> questionAccList) {
		this.questionAccList = questionAccList;
	}
}
