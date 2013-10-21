package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.ClassificationTopic;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.ClassificationTopic")
@Component
public class ClassificationTopicLocator extends Locator<ClassificationTopic, Long> {

    public ClassificationTopic create(Class<? extends medizin.server.domain.ClassificationTopic> clazz) {
        return new ClassificationTopic();
    }

    public ClassificationTopic find(Class<? extends medizin.server.domain.ClassificationTopic> clazz, Long id) {
        return ClassificationTopic.findClassificationTopic(id);
    }

    public Class<medizin.server.domain.ClassificationTopic> getDomainType() {
        return ClassificationTopic.class;
    }

    public Long getId(ClassificationTopic classificationTopic) {
        return classificationTopic.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(ClassificationTopic classificationTopic) {
        return classificationTopic.getVersion();
    }
}
