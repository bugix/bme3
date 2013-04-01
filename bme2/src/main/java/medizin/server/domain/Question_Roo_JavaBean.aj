// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import java.util.Date;
import java.util.Set;
import medizin.server.domain.Answer;
import medizin.server.domain.AssesmentQuestion;
import medizin.server.domain.Comment;
import medizin.server.domain.Keyword;
import medizin.server.domain.Mc;
import medizin.server.domain.Person;
import medizin.server.domain.Question;
import medizin.server.domain.QuestionEvent;
import medizin.server.domain.QuestionResource;
import medizin.server.domain.QuestionType;
import medizin.server.domain.UserAccessRights;
import medizin.shared.Status;

privileged aspect Question_Roo_JavaBean {
    
    public String Question.getQuestionShortName() {
        return this.questionShortName;
    }
    
    public void Question.setQuestionShortName(String questionShortName) {
        this.questionShortName = questionShortName;
    }
    
    public String Question.getQuestionText() {
        return this.questionText;
    }
    
    public void Question.setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    
    public String Question.getPicturePath() {
        return this.picturePath;
    }
    
    public void Question.setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
    
    public Integer Question.getQuestionVersion() {
        return this.questionVersion;
    }
    
    public void Question.setQuestionVersion(Integer questionVersion) {
        this.questionVersion = questionVersion;
    }
    
    public Integer Question.getQuestionSubVersion() {
        return this.questionSubVersion;
    }
    
    public void Question.setQuestionSubVersion(Integer questionSubVersion) {
        this.questionSubVersion = questionSubVersion;
    }
    
    public Boolean Question.getIsAcceptedRewiever() {
        return this.isAcceptedRewiever;
    }
    
    public void Question.setIsAcceptedRewiever(Boolean isAcceptedRewiever) {
        this.isAcceptedRewiever = isAcceptedRewiever;
    }
    
    public Boolean Question.getSubmitToReviewComitee() {
        return this.submitToReviewComitee;
    }
    
    public void Question.setSubmitToReviewComitee(Boolean submitToReviewComitee) {
        this.submitToReviewComitee = submitToReviewComitee;
    }
    
    public Boolean Question.getIsAcceptedAdmin() {
        return this.isAcceptedAdmin;
    }
    
    public void Question.setIsAcceptedAdmin(Boolean isAcceptedAdmin) {
        this.isAcceptedAdmin = isAcceptedAdmin;
    }
    
    public Boolean Question.getIsAcceptedAuthor() {
        return this.isAcceptedAuthor;
    }
    
    public void Question.setIsAcceptedAuthor(Boolean isAcceptedAuthor) {
        this.isAcceptedAuthor = isAcceptedAuthor;
    }
    
    public Question Question.getPreviousVersion() {
        return this.previousVersion;
    }
    
    public void Question.setPreviousVersion(Question previousVersion) {
        this.previousVersion = previousVersion;
    }
    
    public Set<Keyword> Question.getKeywords() {
        return this.keywords;
    }
    
    public void Question.setKeywords(Set<Keyword> keywords) {
        this.keywords = keywords;
    }
    
    public QuestionEvent Question.getQuestEvent() {
        return this.questEvent;
    }
    
    public void Question.setQuestEvent(QuestionEvent questEvent) {
        this.questEvent = questEvent;
    }
    
    public Comment Question.getComment() {
        return this.comment;
    }
    
    public void Question.setComment(Comment comment) {
        this.comment = comment;
    }
    
    public QuestionType Question.getQuestionType() {
        return this.questionType;
    }
    
    public void Question.setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }
    
    public Status Question.getStatus() {
        return this.status;
    }
    
    public void Question.setStatus(Status status) {
        this.status = status;
    }
    
    public Set<Mc> Question.getMcs() {
        return this.mcs;
    }
    
    public void Question.setMcs(Set<Mc> mcs) {
        this.mcs = mcs;
    }
    
    public Set<Answer> Question.getAnswers() {
        return this.answers;
    }
    
    public void Question.setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }
    
    public Set<QuestionResource> Question.getQuestionResources() {
        return this.questionResources;
    }
    
    public void Question.setQuestionResources(Set<QuestionResource> questionResources) {
        this.questionResources = questionResources;
    }
    
    public Set<UserAccessRights> Question.getQuestionAccess() {
        return this.questionAccess;
    }
    
    public void Question.setQuestionAccess(Set<UserAccessRights> questionAccess) {
        this.questionAccess = questionAccess;
    }
    
    public Date Question.getDateAdded() {
        return this.dateAdded;
    }
    
    public void Question.setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
    
    public Date Question.getDateChanged() {
        return this.dateChanged;
    }
    
    public void Question.setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }
    
    public Person Question.getRewiewer() {
        return this.rewiewer;
    }
    
    public void Question.setRewiewer(Person rewiewer) {
        this.rewiewer = rewiewer;
    }
    
    public Person Question.getAutor() {
        return this.autor;
    }
    
    public void Question.setAutor(Person autor) {
        this.autor = autor;
    }
    
    public Boolean Question.getIsReadOnly() {
        return this.isReadOnly;
    }
    
    public void Question.setIsReadOnly(Boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }
    
    public Set<AssesmentQuestion> Question.getAssesmentQuestionSet() {
        return this.assesmentQuestionSet;
    }
    
    public void Question.setAssesmentQuestionSet(Set<AssesmentQuestion> assesmentQuestionSet) {
        this.assesmentQuestionSet = assesmentQuestionSet;
    }
    
}
