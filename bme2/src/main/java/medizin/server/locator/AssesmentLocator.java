package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.Assesment;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.Assesment")
@Component
public class AssesmentLocator extends Locator<Assesment, Long> {

    public Assesment create(Class<? extends medizin.server.domain.Assesment> clazz) {
        return new Assesment();
    }

    public Assesment find(Class<? extends medizin.server.domain.Assesment> clazz, Long id) {
        return Assesment.findAssesment(id);
    }

    public Class<medizin.server.domain.Assesment> getDomainType() {
        return Assesment.class;
    }

    public Long getId(Assesment assesment) {
        return assesment.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(Assesment assesment) {
        return assesment.getVersion();
    }
}
