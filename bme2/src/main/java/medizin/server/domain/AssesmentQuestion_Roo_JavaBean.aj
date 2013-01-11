// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import java.util.Date;
import java.util.Set;
import medizin.server.domain.AnswerToAssQuestion;
import medizin.server.domain.Assesment;
import medizin.server.domain.AssesmentQuestion;
import medizin.server.domain.Person;
import medizin.server.domain.Question;

privileged aspect AssesmentQuestion_Roo_JavaBean {
    
    public Integer AssesmentQuestion.getOrderAversion() {
        return this.orderAversion;
    }
    
    public void AssesmentQuestion.setOrderAversion(Integer orderAversion) {
        this.orderAversion = orderAversion;
    }
    
    public Integer AssesmentQuestion.getOrderBversion() {
        return this.orderBversion;
    }
    
    public void AssesmentQuestion.setOrderBversion(Integer orderBversion) {
        this.orderBversion = orderBversion;
    }
    
    public Double AssesmentQuestion.getTrenschaerfe() {
        return this.trenschaerfe;
    }
    
    public void AssesmentQuestion.setTrenschaerfe(Double trenschaerfe) {
        this.trenschaerfe = trenschaerfe;
    }
    
    public Double AssesmentQuestion.getSchwierigkeit() {
        return this.schwierigkeit;
    }
    
    public void AssesmentQuestion.setSchwierigkeit(Double schwierigkeit) {
        this.schwierigkeit = schwierigkeit;
    }
    
    public Boolean AssesmentQuestion.getIsAssQuestionAcceptedRewiever() {
        return this.isAssQuestionAcceptedRewiever;
    }
    
    public void AssesmentQuestion.setIsAssQuestionAcceptedRewiever(Boolean isAssQuestionAcceptedRewiever) {
        this.isAssQuestionAcceptedRewiever = isAssQuestionAcceptedRewiever;
    }
    
    public Boolean AssesmentQuestion.getIsAssQuestionAcceptedAdmin() {
        return this.isAssQuestionAcceptedAdmin;
    }
    
    public void AssesmentQuestion.setIsAssQuestionAcceptedAdmin(Boolean isAssQuestionAcceptedAdmin) {
        this.isAssQuestionAcceptedAdmin = isAssQuestionAcceptedAdmin;
    }
    
    public Boolean AssesmentQuestion.getIsAssQuestionAdminProposal() {
        return this.isAssQuestionAdminProposal;
    }
    
    public void AssesmentQuestion.setIsAssQuestionAdminProposal(Boolean isAssQuestionAdminProposal) {
        this.isAssQuestionAdminProposal = isAssQuestionAdminProposal;
    }
    
    public Boolean AssesmentQuestion.getIsAssQuestionAcceptedAutor() {
        return this.isAssQuestionAcceptedAutor;
    }
    
    public void AssesmentQuestion.setIsAssQuestionAcceptedAutor(Boolean isAssQuestionAcceptedAutor) {
        this.isAssQuestionAcceptedAutor = isAssQuestionAcceptedAutor;
    }
    
    public Question AssesmentQuestion.getQuestion() {
        return this.question;
    }
    
    public void AssesmentQuestion.setQuestion(Question question) {
        this.question = question;
    }
    
    public Assesment AssesmentQuestion.getAssesment() {
        return this.assesment;
    }
    
    public void AssesmentQuestion.setAssesment(Assesment assesment) {
        this.assesment = assesment;
    }
    
    public Set<AnswerToAssQuestion> AssesmentQuestion.getAnswersToAssQuestion() {
        return this.answersToAssQuestion;
    }
    
    public void AssesmentQuestion.setAnswersToAssQuestion(Set<AnswerToAssQuestion> answersToAssQuestion) {
        this.answersToAssQuestion = answersToAssQuestion;
    }
    
    public Date AssesmentQuestion.getDateAdded() {
        return this.dateAdded;
    }
    
    public void AssesmentQuestion.setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
    
    public Date AssesmentQuestion.getDateChanged() {
        return this.dateChanged;
    }
    
    public void AssesmentQuestion.setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }
    
    public Person AssesmentQuestion.getRewiewer() {
        return this.rewiewer;
    }
    
    public void AssesmentQuestion.setRewiewer(Person rewiewer) {
        this.rewiewer = rewiewer;
    }
    
    public Person AssesmentQuestion.getAutor() {
        return this.autor;
    }
    
    public void AssesmentQuestion.setAutor(Person autor) {
        this.autor = autor;
    }
    
}
