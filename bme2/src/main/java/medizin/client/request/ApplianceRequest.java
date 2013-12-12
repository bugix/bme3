package medizin.client.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

import medizin.client.proxy.ApplianceProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

@RooGwtUnmanagedRequest("medizin.server.domain.Appliance")
@ServiceName("medizin.server.domain.Appliance")
public interface ApplianceRequest extends ApplianceRequest_Roo_Gwt {
	
	Request<List<ApplianceProxy>> findAllAppliancesByShortcutASC();
}
