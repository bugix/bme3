package medizin.server.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Keyword {

    @NotNull
    @Column(unique = true)
    @Size(min = 2, max = 45)
    private String name;
    
   /* @ManyToMany(cascade = CascadeType.ALL, mappedBy = "keywords")
    private Set<Question> questions = new HashSet<Question>();*/
    
    public static Question findKeywordByStringOrAddKeyword(String keywordStr, Question question)
    {
    	Keyword keywordVal = new Keyword();
    	EntityManager em = entityManager();
    	String sql = "SELECT k FROM Keyword k where k.name = '" + keywordStr + "'"; 
    	TypedQuery<Keyword> query = em.createQuery(sql, Keyword.class);
    	
    	if (query.getResultList() != null && query.getResultList().size() > 0)
    		keywordVal = query.getResultList().get(0);
    	else
    	{
    		Keyword keyword = new Keyword();
    		keyword.setName(keywordStr);
    		keyword.persist();
    		
    		keywordVal = keyword;
    	}
    	
    	if (question != null)
    	{
    		Set<Keyword> keywordSet = question.getKeywords();
    		keywordSet.add(keywordVal);
    		
    		question.setKeywords(keywordSet);
    		question.persist();
    	}
    	
    	return question;
    }
    
    public static Question deleteKeywordFromQuestion(Keyword keyword, Question question)
    {
    	Set<Keyword> keywordSet = question.getKeywords();
    	
    	for (Keyword keywordVal : keywordSet)
    	{
    		if (keywordVal.getId().equals(keyword.getId()))
    		{
    			keywordSet.remove(keywordVal);
    			break;
    		}
    	}
    	
    	question.setKeywords(keywordSet);
    	question.persist();
    	
    	return question;
    }
    
    public static Integer countKeywordByQuestion(Long questionId)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT k FROM Question q JOIN q.keywords k WHERE q.id = " + questionId;
    	TypedQuery<Keyword> query = em.createQuery(sql, Keyword.class);
    	return query.getResultList().size();
    }
    
    public static List<Keyword> findKeywordByQuestion(Long questionId, int start, int length)
    {
    	EntityManager em = entityManager();
    	String sql = "SELECT k FROM Question q JOIN q.keywords k WHERE q.id = " + questionId;
    	TypedQuery<Keyword> query = em.createQuery(sql, Keyword.class);
    	query.setFirstResult(start);
    	query.setMaxResults(length);
    	return query.getResultList();
    }
}
