package medizin.client.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

import medizin.client.proxy.InstitutionProxy;
import medizin.server.domain.Institution;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

@RooGwtUnmanagedRequest("medizin.server.domain.Institution")
@ServiceName("medizin.server.domain.Institution")
public interface InstitutionRequest extends InstitutionRequest_Roo_Gwt {
	
	InstanceRequest<InstitutionProxy, Void> mySetCurrentInstitution();

	Request<InstitutionProxy> myGetInstitutionToWorkWith();
	
	Request<Void> fillCurrentInstitutionNull();
	
	Request<List<InstitutionProxy>> findInstitutionByName(String text, int start, int length);
	
	Request<Long> countInstitutionByName(String text);
}
