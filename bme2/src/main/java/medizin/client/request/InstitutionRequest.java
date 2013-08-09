package medizin.client.request;

import java.util.List;

import medizin.client.proxy.InstitutionProxy;
import medizin.client.ui.widget.Sorting;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.Institution")
@ServiceName("medizin.server.domain.Institution")
public interface InstitutionRequest extends InstitutionRequest_Roo_Gwt {
	
	InstanceRequest<InstitutionProxy, Void> mySetCurrentInstitution();

	Request<InstitutionProxy> myGetInstitutionToWorkWith();
	
	Request<Void> fillCurrentInstitutionNull();
	
	Request<List<InstitutionProxy>> findInstitutionByName(String text, Long personId, int start, int length);
	
	Request<Long> countInstitutionByName(String text, Long personId);
	
	Request<Long> countAllInstitutions(String text);
			
	Request<List<InstitutionProxy>> findAllInstitutions(Integer start, Integer length, String sortname, Sorting sortorder, String searchValue);
	
	Request<Long> countAllInstitutionsBySearchValue(String searchText, Long personId);
	
	Request<List<InstitutionProxy>> findAllInstitutionsBySearchValue(String searchText, Long personId, int start, int length);
}
