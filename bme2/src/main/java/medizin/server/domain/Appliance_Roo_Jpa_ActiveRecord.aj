// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import medizin.server.domain.Appliance;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Appliance_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Appliance.entityManager;
    
    public static final EntityManager Appliance.entityManager() {
        EntityManager em = new Appliance().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Appliance.countAppliances() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Appliance o", Long.class).getSingleResult();
    }
    
    public static List<Appliance> Appliance.findAllAppliances() {
        return entityManager().createQuery("SELECT o FROM Appliance o", Appliance.class).getResultList();
    }
    
    public static Appliance Appliance.findAppliance(Long id) {
        if (id == null) return null;
        return entityManager().find(Appliance.class, id);
    }
    
    public static List<Appliance> Appliance.findApplianceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Appliance o", Appliance.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Appliance.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Appliance.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Appliance attached = Appliance.findAppliance(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Appliance.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Appliance.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Appliance Appliance.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Appliance merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}