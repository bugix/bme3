// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import medizin.server.domain.UserAccessRights;
import org.springframework.transaction.annotation.Transactional;

privileged aspect UserAccessRights_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager UserAccessRights.entityManager;
    
    public static final EntityManager UserAccessRights.entityManager() {
        EntityManager em = new UserAccessRights().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long UserAccessRights.countUserAccessRightses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM UserAccessRights o", Long.class).getSingleResult();
    }
    
    public static List<UserAccessRights> UserAccessRights.findAllUserAccessRightses() {
        return entityManager().createQuery("SELECT o FROM UserAccessRights o", UserAccessRights.class).getResultList();
    }
    
    public static UserAccessRights UserAccessRights.findUserAccessRights(Long id) {
        if (id == null) return null;
        return entityManager().find(UserAccessRights.class, id);
    }
    
    public static List<UserAccessRights> UserAccessRights.findUserAccessRightsEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UserAccessRights o", UserAccessRights.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void UserAccessRights.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void UserAccessRights.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            UserAccessRights attached = UserAccessRights.findUserAccessRights(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void UserAccessRights.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void UserAccessRights.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public UserAccessRights UserAccessRights.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        UserAccessRights merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
