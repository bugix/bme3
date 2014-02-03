package medizin.server.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.FlushModeType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.SetJoin;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import medizin.client.ui.widget.Sorting;
import medizin.server.utils.ServerConstants;
import medizin.shared.Status;
import medizin.shared.utils.PersonAccessRight;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person",fetch=FetchType.LAZY)
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
        
        if(shibId.equals(this.shidId) == false) {
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

	public static Person findPersonByNameAndPrename(String name ,String prename) {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
    	CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
    	Root<Person> from = criteriaQuery.from(Person.class);
    	Predicate pre1 = criteriaBuilder.equal(from.get("name"), name);
    	Predicate pre2 = criteriaBuilder.equal(from.get("prename"), prename);
    	criteriaQuery.where(criteriaBuilder.and(pre1,pre2));
		TypedQuery<Person> query = entityManager().createQuery(criteriaQuery);
		List<Person> resultList = query.getResultList();
		if(resultList != null && resultList.size() > 0) {
			return resultList.get(0);
		}
		return null;
	}
	
	 
	 public static List<Person> findAllPeopleNotAcceptedQuestionAnswerAssesment() {
		 Set<Person> persons = Sets.newHashSet();
		 persons.addAll(findPersonAutorCountForNotAccptedQuestion());
		 persons.addAll(findPersonReviewerCountForNotAccptedQuestion());
		 persons.addAll(findPersonForNotAddedAssessmentQuestion());
		 persons.addAll(findPersonAuthorCountForNotAccptedAnswer());
		 persons.addAll(findPersonReviewerCountForNotAccptedAnswer());
		 return Lists.newArrayList(persons);
	 }
	 
	 
	private static List<Person> findPersonAutorCountForNotAccptedQuestion() {

		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");

		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
		Root<Question> from = criteriaQuery.from(Question.class);
		Selection<Person> path = from.get("autor");
		criteriaQuery.select(path);
		criteriaQuery.distinct(true);

		Predicate pre1 = criteriaBuilder.and(criteriaBuilder.equal(from.get("questEvent").get("institution").get("id"),institution.getId()), criteriaBuilder.notEqual(from.get("status"), Status.DEACTIVATED), criteriaBuilder.notEqual(from.get("isForcedActive"), true));
		Predicate pre3 = criteriaBuilder.equal(from.get("isAcceptedAuthor"), false);

		pre1 = criteriaBuilder.and(pre1, pre3);
		
		criteriaQuery.where(pre1);

		TypedQuery<Person> q = entityManager().createQuery(criteriaQuery);
		log.info("All persons : " + q.unwrap(Query.class).getQueryString());
		return q.getResultList();
	}
	
	private static List<Person> findPersonReviewerCountForNotAccptedQuestion() {

		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");

		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
		Root<Question> from = criteriaQuery.from(Question.class);
		Selection<Person> path = from.get("rewiewer");
		criteriaQuery.select(path);
		criteriaQuery.distinct(true);

		Predicate pre1 = criteriaBuilder.and(criteriaBuilder.equal(from.get("questEvent").get("institution").get("id"),institution.getId()), criteriaBuilder.notEqual(from.get("status"), Status.DEACTIVATED), criteriaBuilder.notEqual(from.get("isForcedActive"), true));
		Predicate pre3 = criteriaBuilder.equal(from.get("isAcceptedRewiever"), false);

		pre1 = criteriaBuilder.and(pre1, pre3);
		
		criteriaQuery.where(pre1);

		TypedQuery<Person> q = entityManager().createQuery(criteriaQuery);
		log.info("All persons : " + q.unwrap(Query.class).getQueryString());
		return q.getResultList();
	}
	public static List<Person> findPersonAuthorCountForNotAccptedAnswer()
	{
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Person> cq = cb.createQuery(Person.class);
		Root<Answer> from = cq.from(Answer.class);
		
		Selection<Person> path = from.get("autor");
		cq.select(path);
		cq.distinct(true);
		
		Predicate pre1 = cb.and(cb.equal(from.get("question").get("questEvent").get("institution").get("id"),institution.getId()), cb.notEqual(from.get("status"), Status.DEACTIVATED), cb.notEqual(from.get("isForcedActive"), true));
		Predicate pre2 = cb.equal(from.get("isAnswerAcceptedAutor"), false);
		pre1 = cb.and(pre1, pre2);
		
		cq.where(pre1);
		
       TypedQuery<Person> q = entityManager().createQuery(cq);
       
       return q.getResultList();
	}
	public static List<Person> findPersonReviewerCountForNotAccptedAnswer()
	{
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");
		
		CriteriaBuilder cb = entityManager().getCriteriaBuilder();
		CriteriaQuery<Person> cq = cb.createQuery(Person.class);
		Root<Answer> from = cq.from(Answer.class);
		
		Selection<Person> path = from.get("rewiewer");
		cq.select(path);
		cq.distinct(true);
		
		Predicate pre1 = cb.and(cb.equal(from.get("question").get("questEvent").get("institution").get("id"),institution.getId()), cb.notEqual(from.get("status"), Status.DEACTIVATED), cb.notEqual(from.get("isForcedActive"), true));
		Predicate pre2 = cb.equal(from.get("isAnswerAcceptedReviewWahrer"), false);
		pre1 = cb.and(pre1, pre2);
		
		cq.where(pre1);
		
       TypedQuery<Person> q = entityManager().createQuery(cq);
       
       return q.getResultList();
	}
	
	private static List<Person> findPersonForNotAddedAssessmentQuestion() {
		Person loggedUser = Person.myGetLoggedPerson();
		Institution institution = Institution.myGetInstitutionToWorkWith();
		if (loggedUser == null || institution == null)
			throw new IllegalArgumentException("The person and institution arguments are required");

		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
		Root<Assesment> from = criteriaQuery.from(Assesment.class);
		criteriaQuery.distinct(true);

		Date dateClosed = new Date();
		Date dateOpen = new Date();
		Boolean isClosed = false;

		SetJoin<Assesment, QuestionSumPerPerson> questionSumPerPersonSetJoin = from.joinSet("questionSumPerPerson", JoinType.LEFT);
		Expression<Date> closedDateExp = from.get("dateClosed");
		Expression<Date> openDateExp = from.get("dateOpen");
		Predicate pre2 = criteriaBuilder.greaterThanOrEqualTo(closedDateExp,dateClosed);
		Predicate pre3 = criteriaBuilder.lessThanOrEqualTo(openDateExp,dateOpen);
		Predicate pre4 = criteriaBuilder.equal(from.get("isClosed"), isClosed);
		Predicate pre5 = criteriaBuilder.equal(from.get("institution").get("id"), institution.getId());

		criteriaQuery.where(criteriaBuilder.and(pre2, pre3, pre4, pre5));
		
		Selection<Person> path =questionSumPerPersonSetJoin.get("responsiblePerson");
				
		criteriaQuery.select(path);
		
		TypedQuery<Person> query = entityManager().createQuery(criteriaQuery);
		
		return query.getResultList();
	}
	
	public static List<Person> findAllPeopleByNameASC(){
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
		Root<Person> from = criteriaQuery.from(Person.class);
		criteriaQuery.orderBy(criteriaBuilder.asc(from.get("name")));
		 
		 TypedQuery<Person> query = entityManager().createQuery(criteriaQuery);
	     log.info("ADVANCED QUERY : " + query.unwrap(Query.class).getQueryString());
	     return query.getResultList();
    	
    }
	
	public static Long countAllUsersOfGivenSearch(String searchValue){
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Person> from = criteriaQuery.from(Person.class);
		criteriaQuery.select(criteriaBuilder.count(from));
		
		final Predicate predicate = predicateUsersOfGivenSearch(searchValue,criteriaBuilder, from);
		criteriaQuery.where(predicate);
		
		TypedQuery<Long> q = entityManager().createQuery(criteriaQuery);
		log.info("Query String: " + q.unwrap(Query.class).getQueryString());
		return q.getSingleResult();
		
	}

	private static Predicate predicateUsersOfGivenSearch(String searchValue,CriteriaBuilder criteriaBuilder, Root<Person> from) {
		
		final Expression<String> nameExp = from.get("name");
		final Expression<String> preNameExp = from.get("prename");
		final Expression<String> emailExp = from.get("email");
		
		final Predicate predicate = criteriaBuilder.or(criteriaBuilder.like(nameExp, "%" + searchValue + "%"), criteriaBuilder.like(preNameExp, "%" + searchValue + "%"),criteriaBuilder.like(emailExp, "%" + searchValue + "%"));
		return predicate;
	}
	
	public static List<Person> findAllUsersOfGivenSearch(String sortColumn,Sorting sortOrder,Integer start, Integer length,String searchValue){
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
		Root<Person> from = criteriaQuery.from(Person.class);
		
		final Predicate predicate = predicateUsersOfGivenSearch(searchValue,criteriaBuilder, from);
		criteriaQuery.where(predicate);
		
		if(sortOrder==Sorting.ASC)
		{
			criteriaQuery.orderBy(criteriaBuilder.asc(from.get(sortColumn)));
		}else{
			criteriaQuery.orderBy(criteriaBuilder.desc(from.get(sortColumn)));
		}
		TypedQuery<Person> q = entityManager().createQuery(criteriaQuery);
		q.setFirstResult(start);
		q.setMaxResults(length);
		log.info("Query String: " + q.unwrap(Query.class).getQueryString());
		return q.getResultList();

	}
	
	public static Integer findRangeStartForPerson(Long personId,String sortColumn,Sorting sortOrder,String searchValue,Integer length) {
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Person> from = criteriaQuery.from(Person.class);
		criteriaQuery.select(from.<Long>get("id"));
		
		final Predicate predicate = predicateUsersOfGivenSearch(searchValue,criteriaBuilder, from);
		criteriaQuery.where(predicate);
		
		if(sortOrder==Sorting.ASC)
		{
			criteriaQuery.orderBy(criteriaBuilder.asc(from.get(sortColumn)));
		}else{
			criteriaQuery.orderBy(criteriaBuilder.desc(from.get(sortColumn)));
		}
		TypedQuery<Long> q = entityManager().createQuery(criteriaQuery);
		List<Long> list = q.getResultList();
		log.info("Query String: " + q.unwrap(Query.class).getQueryString());
		return (list.indexOf(personId) / length) * length;
		
	}
}
