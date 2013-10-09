// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.Topic", locator = "medizin.server.locator.TopicLocator")
@RooGwtProxy(value = "medizin.server.domain.Topic", readOnly = { "version", "id" })
public interface TopicProxy extends EntityProxy {

    abstract Long getId();

    abstract String getTopicDesc();

    abstract void setTopicDesc(String topicDesc);

    abstract ClassificationTopicProxy getClassificationTopic();

    abstract void setClassificationTopic(ClassificationTopicProxy classificationTopic);

    abstract Integer getVersion();
}
