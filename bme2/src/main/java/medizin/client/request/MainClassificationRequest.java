package medizin.client.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

import medizin.client.proxy.MainClassificationProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

@RooGwtUnmanagedRequest("medizin.server.domain.MainClassification")
@ServiceName("medizin.server.domain.MainClassification")
public interface MainClassificationRequest extends MainClassificationRequest_Roo_Gwt {
	abstract Request<List<MainClassificationProxy>> findAllMainClassificationByDescASC();
}
