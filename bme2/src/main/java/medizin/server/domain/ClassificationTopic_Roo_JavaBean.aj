// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import medizin.server.domain.ClassificationTopic;
import medizin.server.domain.MainClassification;

privileged aspect ClassificationTopic_Roo_JavaBean {
    
    public String ClassificationTopic.getShortcut() {
        return this.shortcut;
    }
    
    public void ClassificationTopic.setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }
    
    public String ClassificationTopic.getDescription() {
        return this.description;
    }
    
    public void ClassificationTopic.setDescription(String description) {
        this.description = description;
    }
    
    public MainClassification ClassificationTopic.getMainClassification() {
        return this.mainClassification;
    }
    
    public void ClassificationTopic.setMainClassification(MainClassification mainClassification) {
        this.mainClassification = mainClassification;
    }
    
}
