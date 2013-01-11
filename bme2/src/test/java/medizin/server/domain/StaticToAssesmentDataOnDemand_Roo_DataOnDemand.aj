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
import medizin.server.domain.Assesment;
import medizin.server.domain.AssesmentDataOnDemand;
import medizin.server.domain.StaticContent;
import medizin.server.domain.StaticContentDataOnDemand;
import medizin.server.domain.StaticToAssesment;
import medizin.server.domain.StaticToAssesmentDataOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect StaticToAssesmentDataOnDemand_Roo_DataOnDemand {
    
    declare @type: StaticToAssesmentDataOnDemand: @Component;
    
    private Random StaticToAssesmentDataOnDemand.rnd = new SecureRandom();
    
    private List<StaticToAssesment> StaticToAssesmentDataOnDemand.data;
    
    @Autowired
    AssesmentDataOnDemand StaticToAssesmentDataOnDemand.assesmentDataOnDemand;
    
    @Autowired
    StaticContentDataOnDemand StaticToAssesmentDataOnDemand.staticContentDataOnDemand;
    
    public StaticToAssesment StaticToAssesmentDataOnDemand.getNewTransientStaticToAssesment(int index) {
        StaticToAssesment obj = new StaticToAssesment();
        setAssesment(obj, index);
        setSortOrder(obj, index);
        setStaticContent(obj, index);
        return obj;
    }
    
    public void StaticToAssesmentDataOnDemand.setAssesment(StaticToAssesment obj, int index) {
        Assesment assesment = assesmentDataOnDemand.getRandomAssesment();
        obj.setAssesment(assesment);
    }
    
    public void StaticToAssesmentDataOnDemand.setSortOrder(StaticToAssesment obj, int index) {
        Integer sortOrder = new Integer(index);
        if (sortOrder < 0) {
            sortOrder = 0;
        }
        obj.setSortOrder(sortOrder);
    }
    
    public void StaticToAssesmentDataOnDemand.setStaticContent(StaticToAssesment obj, int index) {
        StaticContent staticContent = staticContentDataOnDemand.getRandomStaticContent();
        obj.setStaticContent(staticContent);
    }
    
    public StaticToAssesment StaticToAssesmentDataOnDemand.getSpecificStaticToAssesment(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        StaticToAssesment obj = data.get(index);
        Long id = obj.getId();
        return StaticToAssesment.findStaticToAssesment(id);
    }
    
    public StaticToAssesment StaticToAssesmentDataOnDemand.getRandomStaticToAssesment() {
        init();
        StaticToAssesment obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return StaticToAssesment.findStaticToAssesment(id);
    }
    
    public boolean StaticToAssesmentDataOnDemand.modifyStaticToAssesment(StaticToAssesment obj) {
        return false;
    }
    
    public void StaticToAssesmentDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = StaticToAssesment.findStaticToAssesmentEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'StaticToAssesment' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<StaticToAssesment>();
        for (int i = 0; i < 10; i++) {
            StaticToAssesment obj = getNewTransientStaticToAssesment(i);
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
