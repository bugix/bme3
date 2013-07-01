package medizin.server.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import medizin.shared.utils.PersonAccessRight;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Assesment {

    @NotNull
    @Size(min = 5, max = 255)
    private String name;
    
    // remeber examiner before closing in days
    @NotNull
    private Integer rememberBeforeClosing;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateOfAssesment;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateOpen;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateClosed;

    @NotNull
    @Size(min = 5, max = 100)
    private String place;

    @NotNull
    @Size(min = 5, max = 255)
    private String logo;

    @NotNull
    @Value("false")
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isClosed;

    @NotNull
    @Value("1")
    private Integer assesmentVersion;

    @NotNull
    @ManyToOne
    private Mc mc;

    @OneToOne
    private medizin.server.domain.Assesment repeFor;

    private Integer percentSameQuestion;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assesment")
    private Set<QuestionSumPerPerson> questionSumPerPerson = new HashSet<QuestionSumPerPerson>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assesment")
    private Set<AssesmentQuestion> assesmentQuestions = new HashSet<AssesmentQuestion>();
    
    @NotNull
    @ManyToOne
    Institution institution;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "assesment")
    private List<QuestionTypeCountPerExam> questionTypeCountPerExams=new ArrayList<QuestionTypeCountPerExam>();
    
    /* Business Login
     * 
     * Find Assessment
     * 
     * Left side
     * 
     * For Admin and Institutional Admin : Open Assessment between  start date and dateOfAssesment
     * 
     * For Examiner :  Open Assessment between  start date and dateClosed , and Assessment for which examiner is assigned
     */
    public static List<Assesment> findAssesmentsOpenBetween() {
    	Date dateClosed =new Date();
    	Date dateOpen = new Date(); 
    	Boolean isClosed=false;
    	
    	Logger log = Logger.getLogger(Assesment.class);
    	log.debug("Datum geschlossen: " + dateClosed);

        EntityManager em = Assesment.entityManager();
        
        Person userLoggedIn=Person.myGetLoggedPerson();
        Boolean isAdmin=userLoggedIn.getIsAdmin();
        PersonAccessRight accessRights=userLoggedIn.getLoggedPersonAccessRights();
        Boolean isInstitutionAdmin=accessRights.getIsInstitutionalAdmin();
        TypedQuery<Assesment> q=null;
        if(isAdmin || isInstitutionAdmin)//For Admin and Institutional Admin user
        	q = em.createQuery("SELECT Assesment FROM Assesment AS assesment WHERE assesment.dateOfAssesment >= :dateClosed  AND assesment.dateOpen <= :dateOpen  AND assesment.isClosed IS :isClosed", Assesment.class);
        else //for examiner
        {
        	q = em.createQuery("SELECT Assesment FROM Assesment AS assesment WHERE assesment.dateClosed >= :dateClosed  AND assesment.dateOpen <= :dateOpen  AND assesment.isClosed IS :isClosed", Assesment.class);
        	 
        }
        q.setParameter("dateClosed", dateClosed);
        q.setParameter("dateOpen", dateOpen);
        q.setParameter("isClosed", isClosed);
        
        List<Assesment> assesments=q.getResultList();
        
        if(!(isAdmin || isInstitutionAdmin))
        {
	        for(int i=0;i<assesments.size();i++)
	        {
	        	Assesment a=assesments.get(i);
	        	Set<QuestionSumPerPerson> questionSumPerPersons=a.getQuestionSumPerPerson();
	        	
	        	boolean flag=false;
	        	
	        	for(QuestionSumPerPerson questionSumPerPerson:questionSumPerPersons)
	        	{
	        		if(questionSumPerPerson.getResponsiblePerson().getId().longValue()==userLoggedIn.getId().longValue())
	            	{
	            		flag=true;
	            		break;
	            	}
	        		
	        	}
	
	    		if(!flag || questionSumPerPersons.size()==0)
	    		{
	    			assesments.remove(i);
	    			i--;
	    		}
	        }
        }
        
        return assesments;
    }
    
    public static List<Assesment> findActiveAssesments() {
    	Date dateOfAssesment =new Date();
    	Date dateOpen = new Date(); 
    	//Boolean isClosed=false;
    	
    	//Logger log = Logger.getLogger(Assesment.class);
    	//log.debug("Datum geschlossen: " + dateClosed);

        EntityManager em = Assesment.entityManager();
        TypedQuery<Assesment> q = em.createQuery("SELECT Assesment FROM Assesment AS assesment WHERE assesment.dateOfAssesment >= :dateOfAssesment  AND assesment.dateOpen <= :dateOpen", Assesment.class);
        q.setParameter("dateOfAssesment", dateOfAssesment);
        q.setParameter("dateOpen", dateOpen);
      //  q.setParameter("isClosed", isClosed);
        return q.getResultList();
    }
    
    
    public static List<Assesment> findAssesmentByInsitute(int firstResult, int maxResults)
    {
    	EntityManager em = Assesment.entityManager();
    	
    	Institution activeInstitute=Institution.myGetInstitutionToWorkWith();
    	//create query  	
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
    	CriteriaQuery<Assesment> criteriaQuery = criteriaBuilder
				.createQuery(Assesment.class);
    	//from
    	Root<Assesment> from = criteriaQuery.from(Assesment.class);
    	
    	criteriaQuery.where(criteriaBuilder.equal(from.get("institution"), activeInstitute));
    	TypedQuery<Assesment> q=entityManager().createQuery(criteriaQuery);
    	q.setFirstResult(firstResult);
    	q.setMaxResults(maxResults);
    	return q.getResultList();
    }
}
