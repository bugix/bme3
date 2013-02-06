package medizin.server.domain;

import java.io.File;
import java.util.Set;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import medizin.server.utils.BMEUtils;
import medizin.shared.MultimediaType;
import medizin.shared.utils.SharedConstant;

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
	
	public static void removeSelectedQuestionResource(Long questionResourceId) {
		
		log.info("to delete the selected resource : " + questionResourceId);
		
		QuestionResource questionResource = QuestionResource.findQuestionResource(questionResourceId);
		
		if(questionResource != null) {
		
			final String path = questionResource.getPath(); 
			questionResource.remove();
			
			String appPath = BMEUtils.getRealPath(path);
			String sysPath = SharedConstant.getUploadBaseDIRPath() + path;
			log.info("applicatin Path : " + appPath);
			log.info("system Path : " + sysPath);
			try {
				File real = new File(appPath);
				if(real.exists()) {
					real.delete();
				}
				
				File sys = new File(sysPath);
				if(sys.exists()) {
					sys.delete();
				}
			}catch(Exception e ) {
				log.error(e.getMessage(),e);
			}
		}else {
			log.error("Question resource null.");
		}
		
	}
	
	public static void deleteFiles(Set<String> paths) {
		
		for (String path : paths) {
		
			String appPath = BMEUtils.getRealPath(path);
			String sysPath = SharedConstant.getUploadBaseDIRPath() + path;
			log.info("applicatin Path : " + appPath);
			log.info("system Path : " + sysPath);
			try {
				File real = new File(appPath);
				if(real.exists()) {
					real.delete();
				}
				
				File sys = new File(sysPath);
				if(sys.exists()) {
					sys.delete();
				}
			}catch(Exception e ) {
				log.error(e.getMessage(),e);
			}
		}
		
	}

}
