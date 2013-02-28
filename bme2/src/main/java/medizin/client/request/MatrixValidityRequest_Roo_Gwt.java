// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.MatrixValidity")
@ServiceName("medizin.server.domain.MatrixValidity")
public interface MatrixValidityRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countMatrixValiditys();

    abstract Request<java.util.List<medizin.client.proxy.MatrixValidityProxy>> findAllMatrixValiditys();

    abstract Request<java.util.List<medizin.client.proxy.MatrixValidityProxy>> findMatrixValidityEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.MatrixValidityProxy> findMatrixValidity(Long id);

    abstract InstanceRequest<medizin.client.proxy.MatrixValidityProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.MatrixValidityProxy, java.lang.Void> remove();
}
