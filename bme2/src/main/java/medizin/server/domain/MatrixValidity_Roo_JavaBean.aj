// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import medizin.server.domain.Answer;
import medizin.server.domain.MatrixValidity;
import medizin.shared.Validity;

privileged aspect MatrixValidity_Roo_JavaBean {
    
    public Answer MatrixValidity.getAnswerX() {
        return this.answerX;
    }
    
    public void MatrixValidity.setAnswerX(Answer answerX) {
        this.answerX = answerX;
    }
    
    public Answer MatrixValidity.getAnswerY() {
        return this.answerY;
    }
    
    public void MatrixValidity.setAnswerY(Answer answerY) {
        this.answerY = answerY;
    }
    
    public Validity MatrixValidity.getValidity() {
        return this.validity;
    }
    
    public void MatrixValidity.setValidity(Validity validity) {
        this.validity = validity;
    }
    
}
