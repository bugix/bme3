package medizin.server.domain;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Person {

	private static Logger log = Logger.getLogger(Person.class);
	
    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Size(max = 50)
    private String prename;

    @Size(max = 50)
    private String shidId;
    
    @NotNull
    @Column(unique = true)
    @Size(min = 7, max = 50)
    private String email;

    @NotNull
    @Column(unique = true)
    @Size(min = 7, max = 50)
    private String alternativEmail;

    @Size(min = 5, max = 50)
    private String phoneNumber;

    @NotNull
    @Value("false")
    private Boolean isAdmin;

    @NotNull
    @Value("false")
    private Boolean isAccepted;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    private Set<QuestionAccess> questionAccesses = new HashSet<QuestionAccess>();
    
    public void loginPerson(){
		HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		session.setAttribute("shibdId", this.shidId);
    }
    
  
	public static Person findPersonByShibId(String shibdId) {
		 return entityManager().createQuery("select o from Person o WHERE o.shidId LIKE '" + shibdId + "'", Person.class).getSingleResult();
	}
	

	public static Person myGetLoggedPerson(){
		HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		Enumeration attNames = session.getAttributeNames();
		while ( attNames.hasMoreElements()){
			
			log.info( attNames.nextElement().toString());
		}
		
		log.info("ShibdId" + session.getAttribute("shibdId"));
		if(session.getAttribute("shibdId") == null){
			session.setAttribute("shibdId", "LHDAHSDFHDKJFH747835");
		}
		Person person = findPersonByShibId(session.getAttribute("shibdId").toString());
		return person;
	}
}
