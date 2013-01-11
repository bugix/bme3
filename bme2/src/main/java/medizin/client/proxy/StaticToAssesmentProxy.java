// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.StaticToAssesment", locator = "medizin.server.locator.StaticToAssesmentLocator")
@RooGwtProxy(value = "medizin.server.domain.StaticToAssesment", readOnly = { "version", "id" })
public interface StaticToAssesmentProxy extends EntityProxy {

    abstract Long getId();

    abstract Integer getSortOrder();

    abstract void setSortOrder(Integer sortOrder);

    abstract StaticContentProxy getStaticContent();

    abstract void setStaticContent(StaticContentProxy staticContent);

    abstract AssesmentProxy getAssesment();

    abstract void setAssesment(AssesmentProxy assesment);

    abstract Integer getVersion();
}
