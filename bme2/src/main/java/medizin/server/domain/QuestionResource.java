package medizin.server.domain;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.persistence.ManyToOne;
import javax.persistence.PostRemove;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import medizin.server.utils.BMEUtils;
import medizin.shared.MultimediaType;
import medizin.shared.utils.SharedConstant;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.google.common.collect.Sets;

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
	
	private Integer imageHeight;
	
	private Integer imageWidth;
	
	public static void persistSet(Set<QuestionResource> questionResources) {
		
		log.info("persist set");
		for (QuestionResource questionResource : questionResources) {
			QuestionResource resource = QuestionResource.findQuestionResourceByPathAndQuestion(questionResource.getPath(), questionResource.getQuestion());
			if(resource != null) {
				resource.setPath(questionResource.getPath());
				resource.setQuestion(questionResource.getQuestion());
				resource.setSequenceNumber(questionResource.getSequenceNumber());
				resource.setType(questionResource.getType());
				resource.persist();
			}else {
				questionResource.persist();
			}
		}
	}
	
	public static List<QuestionResource> findQuestionResourceByQuestion(Question question) {
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<QuestionResource> criteriaQuery = criteriaBuilder.createQuery(QuestionResource.class);
		Root<QuestionResource> from = criteriaQuery.from(QuestionResource.class);
		criteriaQuery.where(criteriaBuilder.equal(from.get("question").get("id"), question.getId()));
		TypedQuery<QuestionResource> query = entityManager().createQuery(criteriaQuery);
		log.info("Query is : " + query.unwrap(Query.class).getQueryString());
		return query.getResultList();
	}

	public static QuestionResource findQuestionResourceByPathAndQuestion(String path,Question question) {
		
		CriteriaBuilder criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<QuestionResource> criteriaQuery = criteriaBuilder.createQuery(QuestionResource.class);
		Root<QuestionResource> from = criteriaQuery.from(QuestionResource.class);
		Predicate p1 = criteriaBuilder.equal(from.get("question").get("id"), question.getId());
		Predicate p2 = criteriaBuilder.equal(from.get("path"), path);
		criteriaQuery.where(criteriaBuilder.and(p1,p2));
		TypedQuery<QuestionResource> query = entityManager().createQuery(criteriaQuery);
		log.info("Query is : " + query.unwrap(Query.class).getQueryString());
		List<QuestionResource> list = query.getResultList();
		
		if(list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
		
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
	
	@PostRemove 
	void onPostRemove() {
		log.info("in post remove method of questionResource");
		if(this instanceof QuestionResource) {
			QuestionResource resource = (QuestionResource) this;
			deleteFiles(Sets.newHashSet(resource.getPath()));
		}
	}
	
	public static void removeQuestionResource(Long qestionResourceId) {
		QuestionResource questionResource = QuestionResource.findQuestionResource(qestionResourceId);
		if(questionResource != null) {
			questionResource.remove();	
		}
	}	
}
