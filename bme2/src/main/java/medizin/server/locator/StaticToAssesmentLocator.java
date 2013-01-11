package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.StaticToAssesment;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.StaticToAssesment")
@Component
public class StaticToAssesmentLocator extends Locator<StaticToAssesment, Long> {

    public StaticToAssesment create(Class<? extends medizin.server.domain.StaticToAssesment> clazz) {
        return new StaticToAssesment();
    }

    public StaticToAssesment find(Class<? extends medizin.server.domain.StaticToAssesment> clazz, Long id) {
        return StaticToAssesment.findStaticToAssesment(id);
    }

    public Class<medizin.server.domain.StaticToAssesment> getDomainType() {
        return StaticToAssesment.class;
    }

    public Long getId(StaticToAssesment staticToAssesment) {
        return staticToAssesment.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(StaticToAssesment staticToAssesment) {
        return staticToAssesment.getVersion();
    }
}
