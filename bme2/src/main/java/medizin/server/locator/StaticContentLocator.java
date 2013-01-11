package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.StaticContent;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.StaticContent")
@Component
public class StaticContentLocator extends Locator<StaticContent, Long> {

    public StaticContent create(Class<? extends medizin.server.domain.StaticContent> clazz) {
        return new StaticContent();
    }

    public StaticContent find(Class<? extends medizin.server.domain.StaticContent> clazz, Long id) {
        return StaticContent.findStaticContent(id);
    }

    public Class<medizin.server.domain.StaticContent> getDomainType() {
        return StaticContent.class;
    }

    public Long getId(StaticContent staticContent) {
        return staticContent.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(StaticContent staticContent) {
        return staticContent.getVersion();
    }
}
