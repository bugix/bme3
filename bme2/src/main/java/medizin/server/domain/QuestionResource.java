package medizin.server.domain;

import java.util.Set;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import medizin.shared.MultimediaType;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class QuestionResource {
	private static final Logger log = Logger.getLogger(QuestionResource.class);

	@NotNull
	private String path;

	@NotNull
	private Integer sequenceNumber;

	@NotNull
	private MultimediaType type;

	@NotNull
	@ManyToOne
	private Question question;
	
	
	public static void persistSet(Set<QuestionResource> questionResources) {
		
		log.info("persist set");
		
		for (QuestionResource questionResource : questionResources) {
			//questionResource.question = question;
			questionResource.persist();
		}
	}
	
	public static void removeSelectedQuestionResource(Long qestionResourceId) {
		
		log.info("to delete the selected resource");
		
		QuestionResource questionResource = QuestionResource.findQuestionResource(qestionResourceId);
		questionResource.remove();
	}
			

}
