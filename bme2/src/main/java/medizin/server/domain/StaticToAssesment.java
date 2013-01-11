package medizin.server.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class StaticToAssesment {

    @NotNull
    @Min(0L)
    private Integer sortOrder;

    @NotNull
    @ManyToOne
    private StaticContent staticContent;

    @NotNull
    @ManyToOne
    private Assesment assesment;
}
