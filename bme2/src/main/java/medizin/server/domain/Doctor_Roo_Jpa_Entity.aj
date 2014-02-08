// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import medizin.server.domain.Doctor;

privileged aspect Doctor_Roo_Jpa_Entity {
    
    declare @type: Doctor: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long Doctor.id;
    
    @Version
    @Column(name = "version")
    private Integer Doctor.version;
    
    public Long Doctor.getId() {
        return this.id;
    }
    
    public void Doctor.setId(Long id) {
        this.id = id;
    }
    
    public Integer Doctor.getVersion() {
        return this.version;
    }
    
    public void Doctor.setVersion(Integer version) {
        this.version = version;
    }
    
}