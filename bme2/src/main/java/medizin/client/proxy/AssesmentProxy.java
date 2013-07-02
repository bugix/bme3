// WARNING: THIS FILE IS MANAGED BY SPRING ROO.

package medizin.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyForName;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.roo.addon.gwt.RooGwtProxy;

@ProxyForName(value = "medizin.server.domain.Assesment", locator = "medizin.server.locator.AssesmentLocator")
@RooGwtProxy(value = "medizin.server.domain.Assesment", readOnly = { "version", "id" })
public interface AssesmentProxy extends EntityProxy {

    abstract Long getId();

    abstract String getName();

    abstract void setName(String name);

    abstract Integer getRememberBeforeClosing();

    abstract void setRememberBeforeClosing(Integer rememberBeforeClosing);

    abstract Date getDateOfAssesment();

    abstract void setDateOfAssesment(Date dateOfAssesment);

    abstract Date getDateOpen();

    abstract void setDateOpen(Date dateOpen);

    abstract Date getDateClosed();

    abstract void setDateClosed(Date dateClosed);

    abstract String getPlace();

    abstract void setPlace(String place);

    abstract String getLogo();

    abstract void setLogo(String logo);

    abstract Boolean getIsClosed();

    abstract void setIsClosed(Boolean isClosed);

    abstract Integer getAssesmentVersion();

    abstract void setAssesmentVersion(Integer assesmentVersion);

    abstract McProxy getMc();

    abstract void setMc(McProxy mc);

    abstract medizin.client.proxy.AssesmentProxy getRepeFor();

    abstract void setRepeFor(medizin.client.proxy.AssesmentProxy repeFor);

    abstract Integer getPercentSameQuestion();

    abstract void setPercentSameQuestion(Integer percentSameQuestion);

    abstract Set<medizin.client.proxy.QuestionSumPerPersonProxy> getQuestionSumPerPerson();

    abstract void setQuestionSumPerPerson(Set<medizin.client.proxy.QuestionSumPerPersonProxy> questionSumPerPerson);

    abstract Set<medizin.client.proxy.AssesmentQuestionProxy> getAssesmentQuestions();

    abstract void setAssesmentQuestions(Set<medizin.client.proxy.AssesmentQuestionProxy> assesmentQuestions);

    abstract InstitutionProxy getInstitution();

    abstract void setInstitution(InstitutionProxy institution);

    abstract List<medizin.client.proxy.QuestionTypeCountPerExamProxy> getQuestionTypeCountPerExams();

    abstract void setQuestionTypeCountPerExams(List<medizin.client.proxy.QuestionTypeCountPerExamProxy> questionTypeCountPerExams);

    abstract Integer getVersion();
}
