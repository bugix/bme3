package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.Topic;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.Topic")
@Component
public class TopicLocator extends Locator<Topic, Long> {

    public Topic create(Class<? extends medizin.server.domain.Topic> clazz) {
        return new Topic();
    }

    public Topic find(Class<? extends medizin.server.domain.Topic> clazz, Long id) {
        return Topic.findTopic(id);
    }

    public Class<medizin.server.domain.Topic> getDomainType() {
        return Topic.class;
    }

    public Long getId(Topic topic) {
        return topic.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(Topic topic) {
        return topic.getVersion();
    }
}
