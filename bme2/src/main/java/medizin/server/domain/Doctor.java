package medizin.server.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import medizin.shared.Gender;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Doctor {

	private static Logger log = Logger.getLogger(Doctor.class);
	
	@Enumerated
    private Gender gender;

    @Size(max = 40)
    private String title;

    @Size(max = 40)
    private String name;

    @Size(max = 40)
    private String preName;

    @Size(max = 40)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")
    private String email;

    @Size(max = 30)
    private String telephone;
    
    @Column(columnDefinition="BIT", length = 1)
    private Boolean isActive;
    
 public static List<Doctor> findAllDoctorByNameASC(){
    	
    	CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<Doctor> criteriaQuery = criteriaBuilder.createQuery(Doctor.class);
		Root<Doctor> from = criteriaQuery.from(Doctor.class);
		criteriaQuery.orderBy(criteriaBuilder.asc(from.get("name")));
		 
		TypedQuery<Doctor> query = entityManager().createQuery(criteriaQuery);
	    log.info("ADVANCED QUERY : " + query.unwrap(Query.class).getQueryString());
	    return query.getResultList();
    	
    }
}
