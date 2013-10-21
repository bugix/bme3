// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import medizin.server.domain.MainClassification;
import org.springframework.transaction.annotation.Transactional;

privileged aspect MainClassification_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager MainClassification.entityManager;
    
    public static final EntityManager MainClassification.entityManager() {
        EntityManager em = new MainClassification().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long MainClassification.countMainClassifications() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MainClassification o", Long.class).getSingleResult();
    }
    
    public static List<MainClassification> MainClassification.findAllMainClassifications() {
        return entityManager().createQuery("SELECT o FROM MainClassification o", MainClassification.class).getResultList();
    }
    
    public static MainClassification MainClassification.findMainClassification(Long id) {
        if (id == null) return null;
        return entityManager().find(MainClassification.class, id);
    }
    
    public static List<MainClassification> MainClassification.findMainClassificationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MainClassification o", MainClassification.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void MainClassification.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void MainClassification.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            MainClassification attached = MainClassification.findMainClassification(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void MainClassification.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void MainClassification.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public MainClassification MainClassification.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MainClassification merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
