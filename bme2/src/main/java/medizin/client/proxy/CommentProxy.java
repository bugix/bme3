// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.Comment", locator = "medizin.server.locator.CommentLocator")
@RooGwtProxy(value = "medizin.server.domain.Comment", readOnly = { "version", "id" })
public interface CommentProxy extends EntityProxy {

    abstract Long getId();

    abstract String getComment();

    abstract void setComment(String comment);

    abstract Integer getVersion();
}
