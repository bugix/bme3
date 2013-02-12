package medizin.server.domain;

import javax.persistence.Enumerated;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import medizin.shared.Gender;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Doctor {

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

    private Boolean isActive;
}
