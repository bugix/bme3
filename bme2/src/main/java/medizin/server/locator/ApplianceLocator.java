package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.Appliance;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.Appliance")
@Component
public class ApplianceLocator extends Locator<Appliance, Long> {

    public Appliance create(Class<? extends medizin.server.domain.Appliance> clazz) {
        return new Appliance();
    }

    public Appliance find(Class<? extends medizin.server.domain.Appliance> clazz, Long id) {
        return Appliance.findAppliance(id);
    }

    public Class<medizin.server.domain.Appliance> getDomainType() {
        return Appliance.class;
    }

    public Long getId(Appliance appliance) {
        return appliance.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(Appliance appliance) {
        return appliance.getVersion();
    }
}
