package medizin.client.request;

import java.util.List;

import medizin.client.proxy.PersonAccessRightProxy;
import medizin.client.proxy.PersonProxy;
import medizin.client.ui.widget.Sorting;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.Person")
@ServiceName("medizin.server.domain.Person")
public interface PersonRequest extends PersonRequest_Roo_Gwt {
	
	InstanceRequest<PersonProxy, Void> loginPerson();

	Request<PersonProxy> myGetLoggedPerson();
	
	Request<Boolean> checkAdminRightToLoggedPerson();
	
	Request<List<PersonProxy>> getAllPersons(int start,int end);
	
	Request<Long> findAllPersonCount();
	
	Request<PersonAccessRightProxy> fetchLoggedPersonAccessRights();

	Request<List<PersonProxy>> findAllPeopleNotAcceptedQuestionAnswerAssesment();
	
	Request<List<PersonProxy>> findAllPeopleByNameASC();

	Request<List<PersonProxy>> findAllUsersOfGivenSearch(String sortColumn,Sorting sortOrder,Integer start, Integer length,String value);

	Request<Long> countAllUsersOfGivenSearch(String value);

	Request<Integer> findRangeStartForPerson(Long personId,String sortColumn,Sorting sortOrder,String value, Integer length);
}