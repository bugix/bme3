package medizin.client.request;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

import medizin.client.proxy.DoctorProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

@RooGwtUnmanagedRequest("medizin.server.domain.Doctor")
@ServiceName("medizin.server.domain.Doctor")
public interface DoctorRequest extends DoctorRequest_Roo_Gwt {
	abstract Request<List<DoctorProxy>> findAllDoctorByNameASC();
}
