package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.Doctor;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.Doctor")
@Component
public class DoctorLocator extends Locator<Doctor, Long> {

    public Doctor create(Class<? extends medizin.server.domain.Doctor> clazz) {
        return new Doctor();
    }

    public Doctor find(Class<? extends medizin.server.domain.Doctor> clazz, Long id) {
        return Doctor.findDoctor(id);
    }

    public Class<medizin.server.domain.Doctor> getDomainType() {
        return Doctor.class;
    }

    public Long getId(Doctor doctor) {
        return doctor.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(Doctor doctor) {
        return doctor.getVersion();
    }
}
