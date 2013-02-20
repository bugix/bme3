package medizin.server.domain;

import javax.persistence.Column;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
		session.setAttribute("institutionId", this.getId());
    }
    
    public static void fillCurrentInstitutionNull()
    {
    	HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		session.setAttribute("institutionId", null);
    }

    public static Institution myGetInstitutionToWorkWith(){
    	HttpSession session = RequestFactoryServlet.getThreadLocalRequest().getSession();
		Long instId = (Long) session.getAttribute("institutionId");
		return Institution.findInstitution(instId);
    }
}
