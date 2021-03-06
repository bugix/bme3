// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import medizin.server.domain.DoctorDataOnDemand;
import medizin.server.domain.Person;
import medizin.server.domain.PersonDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect PersonDataOnDemand_Roo_DataOnDemand {
    
    declare @type: PersonDataOnDemand: @Component;
    
    private Random PersonDataOnDemand.rnd = new SecureRandom();
    
    private List<Person> PersonDataOnDemand.data;
    
    @Autowired
    DoctorDataOnDemand PersonDataOnDemand.doctorDataOnDemand;
    
    public Person PersonDataOnDemand.getNewTransientPerson(int index) {
        Person obj = new Person();
        setAlternativEmail(obj, index);
        setEmail(obj, index);
        setIsAccepted(obj, index);
        setIsAdmin(obj, index);
        setIsDoctor(obj, index);
        setName(obj, index);
        setPhoneNumber(obj, index);
        setPrename(obj, index);
        setShidId(obj, index);
        return obj;
    }
    
    public void PersonDataOnDemand.setAlternativEmail(Person obj, int index) {
        String alternativEmail = "foo" + index + "@bar.com";
        if (alternativEmail.length() > 50) {
            alternativEmail = alternativEmail.substring(0, 50);
        }
        obj.setAlternativEmail(alternativEmail);
    }
    
    public void PersonDataOnDemand.setEmail(Person obj, int index) {
        String email = "foo" + index + "@bar.com";
        if (email.length() > 50) {
            email = new Random().nextInt(10) + email.substring(1, 50);
        }
        obj.setEmail(email);
    }
    
    public void PersonDataOnDemand.setIsAccepted(Person obj, int index) {
        Boolean isAccepted = Boolean.TRUE;
        obj.setIsAccepted(isAccepted);
    }
    
    public void PersonDataOnDemand.setIsAdmin(Person obj, int index) {
        Boolean isAdmin = Boolean.TRUE;
        obj.setIsAdmin(isAdmin);
    }
    
    public void PersonDataOnDemand.setIsDoctor(Person obj, int index) {
        Boolean isDoctor = Boolean.TRUE;
        obj.setIsDoctor(isDoctor);
    }
    
    public void PersonDataOnDemand.setName(Person obj, int index) {
        String name = "name_" + index;
        if (name.length() > 50) {
            name = name.substring(0, 50);
        }
        obj.setName(name);
    }
    
    public void PersonDataOnDemand.setPhoneNumber(Person obj, int index) {
        String phoneNumber = "phoneNumber_" + index;
        if (phoneNumber.length() > 50) {
            phoneNumber = phoneNumber.substring(0, 50);
        }
        obj.setPhoneNumber(phoneNumber);
    }
    
    public void PersonDataOnDemand.setPrename(Person obj, int index) {
        String prename = "prename_" + index;
        if (prename.length() > 50) {
            prename = prename.substring(0, 50);
        }
        obj.setPrename(prename);
    }
    
    public void PersonDataOnDemand.setShidId(Person obj, int index) {
        String shidId = "shidId_" + index;
        if (shidId.length() > 50) {
            shidId = shidId.substring(0, 50);
        }
        obj.setShidId(shidId);
    }
    
    public Person PersonDataOnDemand.getSpecificPerson(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Person obj = data.get(index);
        Long id = obj.getId();
        return Person.findPerson(id);
    }
    
    public Person PersonDataOnDemand.getRandomPerson() {
        init();
        Person obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Person.findPerson(id);
    }
    
    public boolean PersonDataOnDemand.modifyPerson(Person obj) {
        return false;
    }
    
    public void PersonDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Person.findPersonEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Person' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Person>();
        for (int i = 0; i < 10; i++) {
            Person obj = getNewTransientPerson(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
