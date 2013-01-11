// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.Keyword")
@ServiceName("medizin.server.domain.Keyword")
public interface KeywordRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countKeywords();

    abstract Request<java.util.List<medizin.client.proxy.KeywordProxy>> findAllKeywords();

    abstract Request<java.util.List<medizin.client.proxy.KeywordProxy>> findKeywordEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.KeywordProxy> findKeyword(Long id);

    abstract InstanceRequest<medizin.client.proxy.KeywordProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.KeywordProxy, java.lang.Void> remove();
}
