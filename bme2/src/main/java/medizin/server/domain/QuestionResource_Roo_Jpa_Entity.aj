// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import medizin.server.domain.QuestionResource;

privileged aspect QuestionResource_Roo_Jpa_Entity {
    
    declare @type: QuestionResource: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long QuestionResource.id;
    
    @Version
    @Column(name = "version")
    private Integer QuestionResource.version;
    
    public Long QuestionResource.getId() {
        return this.id;
    }
    
    public void QuestionResource.setId(Long id) {
        this.id = id;
    }
    
    public Integer QuestionResource.getVersion() {
        return this.version;
    }
    
    public void QuestionResource.setVersion(Integer version) {
        this.version = version;
    }
    
}