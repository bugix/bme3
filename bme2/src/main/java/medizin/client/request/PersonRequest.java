package medizin.client.request;

import medizin.client.proxy.PersonProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.Person")
@ServiceName("medizin.server.domain.Person")
public interface PersonRequest extends PersonRequest_Roo_Gwt {
	
	InstanceRequest<PersonProxy, Void> loginPerson();

	Request<PersonProxy> myGetLoggedPerson();
}
