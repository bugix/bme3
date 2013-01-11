// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.request;

import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServiceName;
import org.springframework.roo.addon.gwt.RooGwtRequest;

@RooGwtRequest("medizin.server.domain.Comment")
@ServiceName("medizin.server.domain.Comment")
public interface CommentRequest_Roo_Gwt extends RequestContext {

    abstract Request<java.lang.Long> countComments();

    abstract Request<java.util.List<medizin.client.proxy.CommentProxy>> findAllComments();

    abstract Request<java.util.List<medizin.client.proxy.CommentProxy>> findCommentEntries(int firstResult, int maxResults);

    abstract Request<medizin.client.proxy.CommentProxy> findComment(Long id);

    abstract InstanceRequest<medizin.client.proxy.CommentProxy, java.lang.Void> persist();

    abstract InstanceRequest<medizin.client.proxy.CommentProxy, java.lang.Void> remove();
}
