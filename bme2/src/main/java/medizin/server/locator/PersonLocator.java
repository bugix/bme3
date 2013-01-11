package medizin.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import medizin.server.domain.Person;
import org.springframework.roo.addon.gwt.RooGwtLocator;
import org.springframework.stereotype.Component;

@RooGwtLocator("medizin.server.domain.Person")
@Component
public class PersonLocator extends Locator<Person, Long> {

    public Person create(Class<? extends medizin.server.domain.Person> clazz) {
        return new Person();
    }

    public Person find(Class<? extends medizin.server.domain.Person> clazz, Long id) {
        return Person.findPerson(id);
    }

    public Class<medizin.server.domain.Person> getDomainType() {
        return Person.class;
    }

    public Long getId(Person person) {
        return person.getId();
    }

    public Class<java.lang.Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(Person person) {
        return person.getVersion();
    }
}
