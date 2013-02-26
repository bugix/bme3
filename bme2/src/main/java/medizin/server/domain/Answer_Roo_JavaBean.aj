// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package medizin.server.domain;

import java.util.Date;
import medizin.client.shared.Validity;
import medizin.server.domain.Answer;
import medizin.server.domain.Comment;
import medizin.server.domain.Person;
import medizin.server.domain.Question;

privileged aspect Answer_Roo_JavaBean {
    
    public String Answer.getAnswerText() {
        return this.answerText;
    }
    
    public void Answer.setAnswerText(String answerText) {
        this.answerText = answerText;
    }
    
    public Boolean Answer.getIsAnswerActive() {
        return this.isAnswerActive;
    }
    
    public void Answer.setIsAnswerActive(Boolean isAnswerActive) {
        this.isAnswerActive = isAnswerActive;
    }
    
    public Boolean Answer.getIsMedia() {
        return this.isMedia;
    }
    
    public void Answer.setIsMedia(Boolean isMedia) {
        this.isMedia = isMedia;
    }
    
    public Boolean Answer.getIsAnswerAcceptedReviewWahrer() {
        return this.isAnswerAcceptedReviewWahrer;
    }
    
    public void Answer.setIsAnswerAcceptedReviewWahrer(Boolean isAnswerAcceptedReviewWahrer) {
        this.isAnswerAcceptedReviewWahrer = isAnswerAcceptedReviewWahrer;
    }
    
    public Boolean Answer.getIsAnswerAcceptedAutor() {
        return this.isAnswerAcceptedAutor;
    }
    
    public void Answer.setIsAnswerAcceptedAutor(Boolean isAnswerAcceptedAutor) {
        this.isAnswerAcceptedAutor = isAnswerAcceptedAutor;
    }
    
    public Boolean Answer.getIsAnswerAcceptedAdmin() {
        return this.isAnswerAcceptedAdmin;
    }
    
    public void Answer.setIsAnswerAcceptedAdmin(Boolean isAnswerAcceptedAdmin) {
        this.isAnswerAcceptedAdmin = isAnswerAcceptedAdmin;
    }
    
    public Validity Answer.getValidity() {
        return this.validity;
    }
    
    public void Answer.setValidity(Validity validity) {
        this.validity = validity;
    }
    
    public String Answer.getMediaPath() {
        return this.mediaPath;
    }
    
    public void Answer.setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }
    
    public Question Answer.getQuestion() {
        return this.question;
    }
    
    public void Answer.setQuestion(Question question) {
        this.question = question;
    }
    
    public Date Answer.getDateAdded() {
        return this.dateAdded;
    }
    
    public void Answer.setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
    
    public Date Answer.getDateChanged() {
        return this.dateChanged;
    }
    
    public void Answer.setDateChanged(Date dateChanged) {
        this.dateChanged = dateChanged;
    }
    
    public Person Answer.getRewiewer() {
        return this.rewiewer;
    }
    
    public void Answer.setRewiewer(Person rewiewer) {
        this.rewiewer = rewiewer;
    }
    
    public Person Answer.getAutor() {
        return this.autor;
    }
    
    public void Answer.setAutor(Person autor) {
        this.autor = autor;
    }
    
    public Comment Answer.getComment() {
        return this.comment;
    }
    
    public void Answer.setComment(Comment comment) {
        this.comment = comment;
    }
    
    public Boolean Answer.getSubmitToReviewComitee() {
        return this.submitToReviewComitee;
    }
    
    public void Answer.setSubmitToReviewComitee(Boolean submitToReviewComitee) {
        this.submitToReviewComitee = submitToReviewComitee;
    }
    
    public String Answer.getPoints() {
        return this.points;
    }
    
    public void Answer.setPoints(String points) {
        this.points = points;
    }
    
    public String Answer.getAdditionalKeywords() {
        return this.additionalKeywords;
    }
    
    public void Answer.setAdditionalKeywords(String additionalKeywords) {
        this.additionalKeywords = additionalKeywords;
    }
    
    public Integer Answer.getSequenceNumber() {
        return this.sequenceNumber;
    }
    
    public void Answer.setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
}
