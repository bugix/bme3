// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.ClassificationTopic", locator = "medizin.server.locator.ClassificationTopicLocator")
@RooGwtProxy(value = "medizin.server.domain.ClassificationTopic", readOnly = { "version", "id" })
public interface ClassificationTopicProxy extends EntityProxy {

    abstract Long getId();

    abstract String getShortcut();

    abstract void setShortcut(String shortcut);

    abstract String getDescription();

    abstract void setDescription(String description);

    abstract MainClassificationProxy getMainClassification();

    abstract void setMainClassification(MainClassificationProxy mainClassification);

    abstract Integer getVersion();
}
