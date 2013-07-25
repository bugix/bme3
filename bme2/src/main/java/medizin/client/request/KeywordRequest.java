package medizin.client.request;

import java.util.List;

import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.QuestionProxy;

import org.springframework.roo.addon.gwt.RooGwtUnmanagedRequest;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServiceName;

@RooGwtUnmanagedRequest("medizin.server.domain.Keyword")
@ServiceName("medizin.server.domain.Keyword")
public interface KeywordRequest extends KeywordRequest_Roo_Gwt {
	
	abstract Request<QuestionProxy> findKeywordByStringOrAddKeyword(String keywordStr, QuestionProxy questionProxy);
	
	abstract Request<QuestionProxy> deleteKeywordFromQuestion(KeywordProxy keyword, QuestionProxy question);
	
	abstract Request<Integer> countKeywordByQuestion(Long questionId);
	
	abstract Request<List<KeywordProxy>> findKeywordByQuestion(Long questionId, int start, int length);
}
