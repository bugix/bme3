package medizin.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import medizin.client.ui.widget.Sorting;
import medizin.server.utils.ServerConstants;
import medizin.shared.AccessRights;

import org.hibernate.Query;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Institution {

    @NotNull
    @Column(unique = true)
    @Size(min = 3, max = 60)
    private String institutionName;
    
    public void mySetCurrentInstitution(){
    	HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		session.setAttribute(ServerConstants.SESSION_INSTITUTION_ID_KEY, this.getId());
		Person.fetchLoggedPersonAccessRights();
    }
    
    public static void fillCurrentInstitutionNull()
    {
    	HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		session.setAttribute(ServerConstants.SESSION_INSTITUTION_ID_KEY, null);
    }

    public static Institution myGetInstitutionToWorkWith(){
    	HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		Long instId = null;
		if(session.getAttribute(ServerConstants.SESSION_INSTITUTION_ID_KEY) != null)
			instId  = (Long) session.getAttribute(ServerConstants.SESSION_INSTITUTION_ID_KEY);
		else
			return null;
					
		return Institution.findInstitution(instId);
    }
    
    public static List<Institution> findInstitutionByName(String text, Long personId, int start, int length)
    {
    	Person loggedPerson = Person.myGetLoggedPerson();
    	Institution loggedInstitution = Institution.myGetInstitutionToWorkWith();
    	if (loggedPerson == null || loggedInstitution == null)
    		return new ArrayList<Institution>();
    	
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
 		CriteriaQuery<Institution> criteriaQuery = criteriaBuilder.createQuery(Institution.class);
 		Root<Institution> from = criteriaQuery.from(Institution.class);
 		Expression<String> exp = from.get("institutionName");
 		
 		Subquery<UserAccessRights> subQuery = criteriaQuery.subquery(UserAccessRights.class);
 		Root userAccessRightsRoot = subQuery.from(UserAccessRights.class);
 		Predicate subPre1 = criteriaBuilder.equal(userAccessRightsRoot.get("person").get("id"), personId);
 		Predicate subPre2 = userAccessRightsRoot.get("institution").isNotNull();
 		subQuery.select(userAccessRightsRoot.get("institution").get("id")).where(criteriaBuilder.and(subPre1, subPre2));
 		
 		Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(exp, "%" + text +"%"), criteriaBuilder.not(criteriaBuilder.in(from.get("id")).value(subQuery)));
 		
 		if (loggedPerson.getIsAdmin() == false)
 		{
 			Predicate pre = criteriaBuilder.equal(from.get("id"), loggedInstitution.getId());
 			predicate = criteriaBuilder.and(pre, predicate);
 		}
 		
 		criteriaQuery.where(predicate);
 		
 		TypedQuery<Institution> query = entityManager().createQuery(criteriaQuery);
 		query.setFirstResult(start);
 		query.setMaxResults(length);
 		
 		return query.getResultList();
    }
    
    public static Long countInstitutionByName(String text, Long personId)
    {
    	Person loggedPerson = Person.myGetLoggedPerson();
    	Institution loggedInstitution = Institution.myGetInstitutionToWorkWith();
    	if (loggedPerson == null || loggedInstitution == null)
    		return 0l;
    	
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
 		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
 		Root<Institution> from = criteriaQuery.from(Institution.class);
 		criteriaQuery.select(criteriaBuilder.count(from)); 		
 		Expression<String> exp = from.get("institutionName");
 		
 		Subquery<UserAccessRights> subQuery = criteriaQuery.subquery(UserAccessRights.class);
 		Root userAccessRightsRoot = subQuery.from(UserAccessRights.class);
 		Predicate subPre1 = criteriaBuilder.equal(userAccessRightsRoot.get("person").get("id"), personId);
 		Predicate subPre2 = userAccessRightsRoot.get("institution").isNotNull();
 		subQuery.select(userAccessRightsRoot.get("institution").get("id")).where(criteriaBuilder.and(subPre1, subPre2));
 		
 		Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(exp, "%" + text +"%"), criteriaBuilder.not(criteriaBuilder.in(from.get("id")).value(subQuery)));
 		
 		if (loggedPerson.getIsAdmin() == false)
 		{
 			Predicate pre = criteriaBuilder.equal(from.get("id"), loggedInstitution.getId());
 			predicate = criteriaBuilder.and(pre, predicate);
 		}
 			
 		criteriaQuery.where(predicate);
 		
 		TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
 		
 		System.out.println("QUERY : " + query.unwrap(Query.class).getQueryString());
 		
 		return query.getSingleResult();
    }
    public static Long countAllInstitutions(String searchValue) {
        	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
    		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
    		Root<Institution> from = criteriaQuery.from(Institution.class);
    		criteriaQuery.select(criteriaBuilder.count(from));				
    		
    		Expression<String> shortNameExp = from.get("institutionName");
     		criteriaQuery.where(criteriaBuilder.or(criteriaBuilder.like(shortNameExp, "%" + searchValue + "%")));
     		
    		TypedQuery<Long> result = entityManager().createQuery(criteriaQuery);
        	return result.getSingleResult();
        }    
    public static List<Institution> findAllInstitutions(Integer start,Integer end,String sortBy,Sorting sortOrder,String searchValue) 
    {
		
    	//System.out.println("in find");
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
 		CriteriaQuery<Institution> criteriaQuery = criteriaBuilder.createQuery(Institution.class);
 		Root<Institution> from = criteriaQuery.from(Institution.class);
 		criteriaQuery.select(from);
 		if(sortOrder==Sorting.ASC)
 		{
 			criteriaQuery.orderBy(criteriaBuilder.asc(from.get(sortBy)));
 		}
 		else
 		{
 			criteriaQuery.orderBy(criteriaBuilder.desc(from.get(sortBy)));
 		}
 		
 		
 		Expression<String> shortNameExp = from.get("institutionName");
 		
 		criteriaQuery.where(criteriaBuilder.or(criteriaBuilder.like(shortNameExp, "%" + searchValue + "%")));
 		
 		TypedQuery<Institution> q = entityManager().createQuery(criteriaQuery);
 		q.setFirstResult(start);
 		q.setMaxResults(end);
 		return q.getResultList();
    }
    
    public static Long countAllInstitutionsBySearchValue(String searchText, Long personId)
    {
    	Person loggedPerson = Person.myGetLoggedPerson();
    	if (loggedPerson == null)
    		throw new IllegalArgumentException("logged person argument is required");
    	
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<Institution> from = criteriaQuery.from(Institution.class);
		criteriaQuery.select(criteriaBuilder.count(from));				
		
		Expression<String> shortNameExp = from.get("institutionName");
		Predicate pre1 = criteriaBuilder.like(shortNameExp, "%" + searchText + "%");
		
		if (personId != null && personId != 0)
		{
			if (loggedPerson.getIsAdmin() == false)
			{
				Subquery<UserAccessRights> subQry = criteriaQuery.subquery(UserAccessRights.class);
				Root queAccRoot = subQry.from(UserAccessRights.class);
				
				Predicate subPre1 = criteriaBuilder.equal(queAccRoot.get("accRights"), AccessRights.AccPrimaryAdmin);
				Predicate subPre2 = criteriaBuilder.equal(queAccRoot.get("person").get("id"), personId);
				
				subQry.select(queAccRoot.get("institution").get("id"))
								.where(criteriaBuilder.and(subPre1, subPre2));
				
				Predicate mainpre2 = criteriaBuilder.in(from.get("id")).value(subQry);
				
				pre1 = criteriaBuilder.and(pre1, mainpre2);
			}
		}
		
 		criteriaQuery.where(pre1);
 		
		TypedQuery<Long> result = entityManager().createQuery(criteriaQuery);
    	return result.getSingleResult();
    }
    
    public static List<Institution> findAllInstitutionsBySearchValue(String searchText, Long personId, int start, int length)
    {
    	Person loggedPerson = Person.myGetLoggedPerson();
    	if (loggedPerson == null)
    		throw new IllegalArgumentException("logged person argument is required");
    	
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Institution> criteriaQuery = criteriaBuilder.createQuery(Institution.class);
		Root<Institution> from = criteriaQuery.from(Institution.class);
		
		criteriaQuery.orderBy(criteriaBuilder.asc(from.get("id")));
		
		Expression<String> shortNameExp = from.get("institutionName");
		Predicate pre1 = criteriaBuilder.like(shortNameExp, "%" + searchText + "%");
		
		if (personId != null && personId != 0)
		{
			if (loggedPerson.getIsAdmin() == false)
			{
				Subquery<UserAccessRights> subQry = criteriaQuery.subquery(UserAccessRights.class);
				Root queAccRoot = subQry.from(UserAccessRights.class);
				
				Predicate subPre1 = criteriaBuilder.equal(queAccRoot.get("accRights"), AccessRights.AccPrimaryAdmin.ordinal());
				Predicate subPre2 = criteriaBuilder.equal(queAccRoot.get("person").get("id"), personId);
				
				subQry.select(queAccRoot.get("institution").get("id"))
								.where(criteriaBuilder.and(subPre1, subPre2));
				
				Predicate mainpre2 = criteriaBuilder.in(from.get("id")).value(subQry);
				
				pre1 = criteriaBuilder.and(pre1, mainpre2);
			}
		}
		
 		criteriaQuery.where(pre1);
 		
		TypedQuery<Institution> result = entityManager().createQuery(criteriaQuery);
		
		result.setFirstResult(start);
		result.setMaxResults(length);
		
    	return result.getResultList();
    }
}

