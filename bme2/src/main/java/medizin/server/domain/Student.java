package medizin.server.domain;

import java.util.List;

import javax.persistence.Enumerated;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Pattern;

import medizin.shared.Gender;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Student {
	
	 private static Logger log = Logger.getLogger(Student.class);
	
	 @Enumerated
	 private Gender gender;
	    
	 private String name;

	 private String preName;

	 @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")
	 private String email;
	        
	 private String studentId;
	    
	 private String street;
	    
	 private String city; 

	 public static List<Student> findStudentByStudentIdAndByEmail(String studentId, String emailId)
	 {
		 CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		 CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
		 Root<Student> from = criteriaQuery.from(Student.class);
		 
		 Predicate predicate = null;
		 
		 if ((!studentId.equals("")) && (!emailId.equals(emailId)))
		 {
			predicate = criteriaBuilder.and(criteriaBuilder.equal(from.get("studentId"), studentId), criteriaBuilder.equal(from.get("email"), emailId));
		 }
		 else if (studentId.equals(""))
		 {
			 predicate = criteriaBuilder.equal(from.get("email"), emailId);
		 }
		 else if (emailId.equals(""))
		 {
			 predicate = criteriaBuilder.equal(from.get("studentId"), studentId);
		 }
		 
		 if (predicate != null)
		 {
			 criteriaQuery.where(predicate);
		 }
		 
		 TypedQuery<Student> query = entityManager().createQuery(criteriaQuery);
		 
		 return query.getResultList();
	 }
}
