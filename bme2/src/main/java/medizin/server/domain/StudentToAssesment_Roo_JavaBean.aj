// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import medizin.server.domain.Assesment;
import medizin.server.domain.Student;
import medizin.server.domain.StudentToAssesment;

privileged aspect StudentToAssesment_Roo_JavaBean {
    
    public Boolean StudentToAssesment.getIsEnrolled() {
        return this.isEnrolled;
    }
    
    public void StudentToAssesment.setIsEnrolled(Boolean isEnrolled) {
        this.isEnrolled = isEnrolled;
    }
    
    public Assesment StudentToAssesment.getAssesment() {
        return this.assesment;
    }
    
    public void StudentToAssesment.setAssesment(Assesment assesment) {
        this.assesment = assesment;
    }
    
    public Student StudentToAssesment.getStudent() {
        return this.student;
    }
    
    public void StudentToAssesment.setStudent(Student student) {
        this.student = student;
    }
    
}
