package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.Institution;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.Institution")
@Component
public class InstitutionLocator extends Locator<Institution, Long> {

    public Institution create(Class<? extends medizin.server.domain.Institution> clazz) {
        return new Institution();
    }

    public Institution find(Class<? extends medizin.server.domain.Institution> clazz, Long id) {
        return Institution.findInstitution(id);
    }

    public Class<medizin.server.domain.Institution> getDomainType() {
        return Institution.class;
    }

    public Long getId(Institution institution) {
        return institution.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(Institution institution) {
        return institution.getVersion();
    }
}
