package medizin.server.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class StudentToAssesment {

	 private static Logger log = Logger.getLogger(StudentToAssesment.class);
	 
	 @NotNull
     @Value("false")
 	 @Column(columnDefinition="BIT", length = 1)
	 private Boolean isEnrolled;
	 
	 @ManyToOne
	 @NotNull
	 private Assesment assesment;
	 
	 @ManyToOne
	 @NotNull
	 private Student student;
	 
	 public static Long findStudentByStudIdAndAssesmentId(Long studentId, Long assessmentId)
	 {
		 CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		 CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		 Root<StudentToAssesment> from = criteriaQuery.from(StudentToAssesment.class);
		 criteriaQuery.select(criteriaBuilder.count(from));
		 
		 criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(from.get("student").get("id"), studentId), criteriaBuilder.equal(from.get("assesment").get("id"), assessmentId)));

		 TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
		 return query.getSingleResult();
	 }

	 public static Long countStudentToAssesmentByAssesment(Long assessmentId)
	 {
		 CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		 CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		 Root<StudentToAssesment> from = criteriaQuery.from(StudentToAssesment.class);
		 criteriaQuery.select(criteriaBuilder.count(from));
		 
		 criteriaQuery.where(criteriaBuilder.equal(from.get("assesment").get("id"), assessmentId));

		 TypedQuery<Long> query = entityManager().createQuery(criteriaQuery);
		 return query.getSingleResult();			
	 }
	 
	 public static List<StudentToAssesment> findStudentToAssesmentByAssesment(Long assesmentId, int start, int length)
	 {
		 CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		 CriteriaQuery<StudentToAssesment> criteriaQuery = criteriaBuilder.createQuery(StudentToAssesment.class);
		 Root<StudentToAssesment> from = criteriaQuery.from(StudentToAssesment.class);
		 
		 criteriaQuery.orderBy(criteriaBuilder.asc(from.get("student").get("name")));
		 
		 criteriaQuery.where(criteriaBuilder.equal(from.get("assesment").get("id"), assesmentId));

		 TypedQuery<StudentToAssesment> query = entityManager().createQuery(criteriaQuery);
		 query.setFirstResult(start);
		 query.setMaxResults(length);
		 return query.getResultList();		
	 }
	 
	 public static List<Student> findStudentsByAssesment(Long assesmentId, Boolean isEnrolled)
	 {
		 log.info("in findStudentsByAssesment with assessmentID " + assesmentId + " and isEnrolled is " + isEnrolled);
		 CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		 CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
		 
		 Root<StudentToAssesment> from = criteriaQuery.from(StudentToAssesment.class);
		 criteriaQuery.select(from.<Student>get("student"));
		 
		 criteriaQuery.orderBy(criteriaBuilder.asc(from.get("student").get("name")));
		 Predicate predicate = criteriaBuilder.equal(from.get("assesment").get("id"), assesmentId);
		 Predicate predicate2 = criteriaBuilder.equal(from.get("isEnrolled"), isEnrolled);
		 criteriaQuery.where(criteriaBuilder.and(predicate,predicate2));

		 TypedQuery<Student> query = entityManager().createQuery(criteriaQuery);
		 
		 return query.getResultList();		
	 }
}
