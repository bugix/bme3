package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.StudentToAssesment;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.StudentToAssesment")
@Component
public class StudentToAssesmentLocator extends Locator<StudentToAssesment, Long> {

    public StudentToAssesment create(Class<? extends medizin.server.domain.StudentToAssesment> clazz) {
        return new StudentToAssesment();
    }

    public StudentToAssesment find(Class<? extends medizin.server.domain.StudentToAssesment> clazz, Long id) {
        return StudentToAssesment.findStudentToAssesment(id);
    }

    public Class<medizin.server.domain.StudentToAssesment> getDomainType() {
        return StudentToAssesment.class;
    }

    public Long getId(StudentToAssesment studentToAssesment) {
        return studentToAssesment.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(StudentToAssesment studentToAssesment) {
        return studentToAssesment.getVersion();
    }
}
