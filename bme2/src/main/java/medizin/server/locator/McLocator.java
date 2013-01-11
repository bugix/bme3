package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.Mc;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.Mc")
@Component
public class McLocator extends Locator<Mc, Long> {

    public Mc create(Class<? extends medizin.server.domain.Mc> clazz) {
        return new Mc();
    }

    public Mc find(Class<? extends medizin.server.domain.Mc> clazz, Long id) {
        return Mc.findMc(id);
    }

    public Class<medizin.server.domain.Mc> getDomainType() {
        return Mc.class;
    }

    public Long getId(Mc mc) {
        return mc.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(Mc mc) {
        return mc.getVersion();
    }
}
