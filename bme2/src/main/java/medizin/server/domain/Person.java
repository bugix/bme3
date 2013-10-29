package medizin.server.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FlushModeType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import medizin.server.utils.ServerConstants;
import medizin.shared.utils.PersonAccessRight;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.common.collect.Lists;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Person {

    private static Logger log = Logger.getLogger(Person.class);

    @NotNull(message="nameNotNull")
    @Size(min=1,max = 50,message="nameMaxSize")
    private String name;

    @NotNull(message="prenameNotNull")
    @Size(min=1,max = 50,message="prenameMaxSize")
    private String prename;

    @Size(max = 50,message="shibbolethMaxSize")
    private String shidId;

    @NotNull(message="emailNotNull")
    @Column(unique = true)
    @Size(min = 7, max = 50,message="emailMinMaxSize")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$",message="emailNotValid")
    private String email;

    //@Column(unique = true)
    @Size(min = 0, max = 50,message="alternativeEmailMinMaxSize")
    @Pattern(regexp = "^$|^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$",message="alternativeEmailNotValid")
    private String alternativEmail;

    @NotNull(message="phoneNumberNotNull")
    @Size(min = 5, max = 50,message="phoneNumberMinMaxSize")
    private String phoneNumber;

    @NotNull
    @Value("false")
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isAdmin;

    @NotNull
    @Value("false")
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isAccepted;

    @NotNull
    @Value("false")
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isDoctor;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    private Set<UserAccessRights> questionAccesses = new HashSet<UserAccessRights>();

    @OneToOne
    private Doctor doctor;

    public void loginPerson() {
        HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
        
        String shibId = this.shidId;
        
        if (shibId == null || shibId.isEmpty())
        {
        	this.shidId = String.valueOf(new Date().getTime());
        	this.persist();
        }
        
        session.setAttribute(ServerConstants.SESSION_SHIBD_ID_KEY, this.shidId);
        fetchLoggedPersonAccessRights();
    }

    public void loginPerson(HttpSession session,String shibId) {
        
        if (StringUtils.isBlank(shibId))
        {
        	throw new IllegalArgumentException("Shibboleth ID cannot be empty");
        }
        
        if(!shibId.equals(this.shidId)) {
        	this.shidId = shibId;
        	this.persist();	
        	log.info("Updated shibid : " + this.shidId);
        }
        session.setAttribute(ServerConstants.SESSION_SHIBD_ID_KEY, this.shidId);
    }
    
    public static medizin.server.domain.Person findPersonByShibId(String shibdId) {
    	TypedQuery<Person> query =  entityManager().createQuery("select o from Person o WHERE o.shidId LIKE '" + shibdId + "'", Person.class);
    	
    	List<Person> resultList = query.getResultList();
    	
    	if(resultList == null || resultList.isEmpty()) {
    		return null;
    	}else {
    		return resultList.get(0);
    	}
    }

    public static medizin.server.domain.Person myGetLoggedPerson() {
        HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
       /* Enumeration attNames = session.getAttributeNames();
        while (attNames.hasMoreElements()) {
            log.info(attNames.nextElement().toString());
        }*/
        //log.info("ShibdId" + session.getAttribute("shibdId"));
        /*if (session.getAttribute("shibdId") == null) {
            session.setAttribute("shibdId", "LHDAHSDFHDKJFH747835");
        }*/
        Person person = null;
        if (session.getAttribute(ServerConstants.SESSION_SHIBD_ID_KEY) != null)
        {
        	person = findPersonByShibId(session.getAttribute(ServerConstants.SESSION_SHIBD_ID_KEY).toString());
        }
        return person;
    }
    
    public static Person myGetLoggedPerson(HttpSession session) {
        Person person = null;
        if (session.getAttribute(ServerConstants.SESSION_SHIBD_ID_KEY) != null)
        {
        	person = findPersonByShibId(session.getAttribute(ServerConstants.SESSION_SHIBD_ID_KEY).toString());
        }
        return person;
    }
    
    public static Boolean checkAdminRightToLoggedPerson()
    {
    	Person person = Person.myGetLoggedPerson();
    	Institution institution = Institution.myGetInstitutionToWorkWith();
    	
    	if (person != null)
    	{
            
            if (person.getIsAdmin())
            {
            	return true;
            }
            else
            {
        		if (institution != null)
        		{
            		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
            		CriteriaQuery<UserAccessRights> criteriaQuery = criteriaBuilder.createQuery(UserAccessRights.class);
            		Root<UserAccessRights> from = criteriaQuery.from(UserAccessRights.class);
            		
            		Predicate pre1 = criteriaBuilder.equal(from.get("person").get("id"), person.getId());
            		Predicate pre2 = criteriaBuilder.equal(from.get("institution").get("id"), institution.getId());
            		
            		criteriaQuery.where(criteriaBuilder.and(pre1, pre2));
            		
            		TypedQuery<UserAccessRights> query = entityManager().createQuery(criteriaQuery);
            		
            		if (query.getResultList().size() > 0)
            			return true;
            		else
            			return false;
        		}
        		else
        			return null;
            }
    	}
    	else
    	{
    		return null;
    	}
    	
    }
    
    public static List<Person> getAllPersons(int start,int end){
    	log.info("inside getAllPersons");
    	Person userLoggedIn =myGetLoggedPerson();
    	Institution institution = Institution.myGetInstitutionToWorkWith();
    	List<Person> resultList = Lists.newArrayList();
    	
		if(userLoggedIn == null || institution == null) {
			return resultList;
		}
    	
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
    	CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
    	Root<Person> from = criteriaQuery.from(Person.class);
    	criteriaQuery.orderBy(criteriaBuilder.asc(from.get("id")));
    	
    	try{

	    	if(userLoggedIn.getIsAdmin()){
	    		Predicate pre1 = criteriaBuilder.notEqual(from.get("id"),userLoggedIn.getId());
		    	criteriaQuery.where(pre1);
	    	}
	    	else{
		    	Predicate pre1 = criteriaBuilder.notEqual(from.get("id"),userLoggedIn.getId());
		    	Predicate pre2 = criteriaBuilder.notEqual(from.get("isAdmin"),Boolean.TRUE);
		    	
		    	criteriaQuery.where(criteriaBuilder.and(pre1,pre2));
	    	}
	    	
	    	TypedQuery<Person> query =entityManager().createQuery(criteriaQuery);
	    	query.setFirstResult(start);
	    	query.setMaxResults(end);
	    	resultList= query.getResultList();
	    	
    	}catch (Exception e) {
			e.printStackTrace();
			log.info("Error Whil getting persons" + e.getStackTrace().toString());
			return resultList;
		}
    	return resultList;
    }

	public static Long findAllPersonCount(){
		
		log.info("inside getAllPersonCount");
		Long value=0l;
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Person> from = criteriaQuery.from(Person.class);
		criteriaQuery.orderBy(criteriaBuilder.asc(from.get("id")));
		
		try{
			Person userLoggedIn = myGetLoggedPerson();
			Institution institution = Institution.myGetInstitutionToWorkWith();
			
			if(userLoggedIn == null || institution == null) {
				return 0l;
			}
			
			criteriaQuery.select(criteriaBuilder.count(from));
	    	if(userLoggedIn.getIsAdmin()){
	    		Predicate pre1 = criteriaBuilder.notEqual(from.get("id"),userLoggedIn.getId());
		    	criteriaQuery.where(pre1);
	    	}
	    	else{
		    	Predicate pre1 = criteriaBuilder.notEqual(from.get("id"),userLoggedIn.getId());
		    	Predicate pre2 = criteriaBuilder.notEqual(from.get("isAdmin"),Boolean.TRUE);
		    	
		    	criteriaQuery.where(criteriaBuilder.and(pre1,pre2));
	    	}
	    	
	    	TypedQuery<Long> query =entityManager().createQuery(criteriaQuery);
	    	
	    	value= query.getSingleResult();
	    	
		}catch (Exception e) {
			e.printStackTrace();
			log.info("Error Whil getting person count" + e.getStackTrace().toString());
			return value;
		}
		
		return value;
	}

	public static PersonAccessRight fetchLoggedPersonAccessRights()
	{
		PersonAccessRight personAccess = new PersonAccessRight();
		Person person = myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		
		if (person != null && institution != null)
		{
			Long instId = institution.getId();
			
			if (person.getIsAdmin())
			{
				personAccess.setIsAdmin(true);
			}
			else
			{
				CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
				CriteriaQuery<UserAccessRights> criteriaQuery = criteriaBuilder.createQuery(UserAccessRights.class);
				Root<UserAccessRights> from = criteriaQuery.from(UserAccessRights.class);
				
				Predicate pre1 = criteriaBuilder.equal(from.get("person").get("id"), person.getId());				 
				Predicate pre2 = criteriaBuilder.equal(from.get("institution").get("id"), instId); 
				
				criteriaQuery.where(criteriaBuilder.and(pre1,pre2));
				
				TypedQuery<UserAccessRights> query = entityManager().createQuery(criteriaQuery);
				
				if (query.getResultList().size() > 0)
				{
					personAccess.setIsInstitutionalAdmin(true);
				}
				else
				{
					Predicate pre11 = criteriaBuilder.equal(from.get("person").get("id"), person.getId());				 
					Predicate pre12 = criteriaBuilder.equal(from.get("questionEvent").get("institution").get("id"), instId); 
					
					criteriaQuery.where(criteriaBuilder.and(pre11,pre12));
					
					TypedQuery<UserAccessRights> query1 = entityManager().createQuery(criteriaQuery);
				
					personAccess.setQuestionEventAccList(query1.getResultList());
					
					Predicate pre21 = criteriaBuilder.equal(from.get("person").get("id"), person.getId());				 
					Predicate pre22 = criteriaBuilder.equal(from.get("question").get("questEvent").get("institution").get("id"), instId); 
					
					criteriaQuery.where(criteriaBuilder.and(pre21,pre22));
					
					TypedQuery<UserAccessRights> query2 = entityManager().createQuery(criteriaQuery);
											
					personAccess.setQuestionAccList(query2.getResultList());
				}
			}
		}
		
		putPersonAccessInSession(personAccess);
		return personAccess;
    }

	private static void putPersonAccessInSession(PersonAccessRight personAccess) {
		HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		session.setAttribute(ServerConstants.SESSION_PERSON_ACCESS_KEY, personAccess);
	}
	
	public static PersonAccessRight fetchPersonAccessFromSession()
	{
		PersonAccessRight personAccessRights = null;
		HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		Object object = session.getAttribute(ServerConstants.SESSION_PERSON_ACCESS_KEY);
		if (object != null && object instanceof PersonAccessRight){
			personAccessRights = (PersonAccessRight) object;
		}
		else{
			personAccessRights = fetchLoggedPersonAccessRights();
		}
		
		return personAccessRights;
	}
	
	public static PersonAccessRight fetchPersonAccessFromSession(HttpSession session)
	{
		PersonAccessRight personAccessRights = null;
		Object object = session.getAttribute(ServerConstants.SESSION_PERSON_ACCESS_KEY);
		if (object != null && object instanceof PersonAccessRight){
			personAccessRights = (PersonAccessRight) object;
		}
		return personAccessRights;
	}
	

	public static Person findPersonByEmail(String email) {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
		Root<Person> from = criteriaQuery.from(Person.class);
		
		Predicate pre1 = criteriaBuilder.equal(from.get("email"), email);
		
		criteriaQuery.where(pre1);
		
		TypedQuery<Person> query = entityManager().createQuery(criteriaQuery);
		List<Person> resultList = query.getResultList();
    	
    	if(resultList == null || resultList.isEmpty()) {
    		return null;
    	} else {
    		return resultList.get(0);
    	}
	}
	
	 public static Person findLoggedPersonByShibId() {
		 HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
	     String personShibId = "";
	     
	     if (session.getAttribute(ServerConstants.SESSION_SHIBD_ID_KEY) != null)
	     {
	    	 personShibId = session.getAttribute(ServerConstants.SESSION_SHIBD_ID_KEY).toString();
	     }
		 
		 TypedQuery<Person> query =  entityManager().createQuery("select o from Person o WHERE o.shidId LIKE '" + personShibId + "'", Person.class);
	    
		 query.setFlushMode(FlushModeType.COMMIT);
		 
		 List<Person> resultList = query.getResultList();
	    	
		 if(resultList == null || resultList.isEmpty()) {
			 return null;
		 }
		 else {
			 return resultList.get(0);
		 }
	 }
}
