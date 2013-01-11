package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

import medizin.client.proxy.InstitutionProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

@RooGwtUnmanagedRequest("medizin.server.domain.Institution")
@ServiceName("medizin.server.domain.Institution")
public interface InstitutionRequest extends InstitutionRequest_Roo_Gwt {
	
	InstanceRequest<InstitutionProxy, Void> mySetCurrentInstitution();

	Request<InstitutionProxy> myGetInstitutionToWorkWith();
}
