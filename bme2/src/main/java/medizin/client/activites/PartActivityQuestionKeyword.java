package medizin.client.activites;

import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

import medizin.client.factory.receiver.BMEReceiver;
import medizin.client.factory.request.McAppRequestFactory;
import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.view.question.keyword.QuestionKeywordView;
import medizin.client.ui.widget.dialogbox.ConfirmationDialogBox;
import medizin.client.ui.widget.process.AppLoader;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.simple.DefaultSuggestOracle;
import medizin.shared.i18n.BmeConstants;

public class PartActivityQuestionKeyword implements QuestionKeywordView.Delegate {

	public BmeConstants constants = GWT.create(BmeConstants.class);
	private final McAppRequestFactory requests;
	private final QuestionKeywordView view;
	private QuestionProxy questionProxy;
	private final boolean isReadOnly;
	private final PartActivityQuestionKeyword thiz;
	
	public PartActivityQuestionKeyword(McAppRequestFactory requests,QuestionKeywordView view, boolean isReadOnly){
		this.thiz = this;
		this.requests = requests;
		this.view = view;
		this.view.setDelegate(this);
		this.isReadOnly = isReadOnly;
		
		if(isReadOnly == true) {
			view.getKeywordSuggestBox().removeFromParent();
			view.getKeywordAddButton().removeFromParent();
		}
	}
	
	public void setQuestionProxy(QuestionProxy questionProxy) {
		this.questionProxy = questionProxy;
	}
	
	@Override
	public void keywordAddButtonClicked(String text, final QuestionProxy proxy) {
		if(isReadOnly == true) {
			return;
		}
		
		Set<KeywordProxy> keywordProxySet = proxy.getKeywords();
		boolean flag = false;
		for (KeywordProxy keywordProxy : keywordProxySet)
		{
			if (keywordProxy.getName().equals(text))
			{
				flag = true;
				break;
			}
		}
		
		if (flag == false)
		{
			
			AppLoader.setCurrentLoader(view.getLoadingPopup());
			requests.keywordRequest().findKeywordByStringOrAddKeyword(text, proxy).with("keywords").fire(new BMEReceiver<QuestionProxy>() {

				@Override
				public void onSuccess(QuestionProxy response) {
					thiz.questionProxy = response;
					view.getKeywordSuggestBox().getTextField().setText("");
					initKeywordList();
				}
			});
		}
		else
		{
			view.getKeywordSuggestBox().getTextField().setText("");
			ConfirmationDialogBox.showOkDialogBox(constants.warning(), constants.keywordExist());
		}		
	}

	@Override
	public void deleteKeywordClicked(KeywordProxy keyword, QuestionProxy proxy) {
		if(isReadOnly == true) {
			return;
		}		
		AppLoader.setCurrentLoader(view.getLoadingPopup());
		requests.keywordRequest().deleteKeywordFromQuestion(keyword, proxy).with("previousVersion","keywords","questEvent","questionType","mcs", "rewiewer", "autor","questionResources","answers").fire(new BMEReceiver<QuestionProxy>() {

			@Override
			public void onSuccess(QuestionProxy response) {
				questionProxy = response;
				view.getKeywordSuggestBox().getTextField().setText("");
				initKeywordList();
			}
		});
	}
	
	public void initKeywordView() {
		view.setQuestionProxy(questionProxy);
		if(isReadOnly == false) {
			//Added this to get all keywords an ASC by name - Manish
			requests.keywordRequest().findAllKeywordsByNameASC().fire(new BMEReceiver<List<KeywordProxy>>() {
			//requests.keywordRequest().findAllKeywords().fire(new BMEReceiver<List<KeywordProxy>>() {

				@Override
				public void onSuccess(List<KeywordProxy> response) {
					DefaultSuggestOracle<KeywordProxy> suggestOracle1 = (DefaultSuggestOracle<KeywordProxy>) view.getKeywordSuggestBox().getSuggestOracle();
					suggestOracle1.setPossiblilities(response);
					view.getKeywordSuggestBox().setSuggestOracle(suggestOracle1);
					
					view.getKeywordSuggestBox().setRenderer(new AbstractRenderer<KeywordProxy>() {

						@Override
						public String render(KeywordProxy object) {
							return object == null ? "" : object.getName();					
						}
					});
				}
			});
		}
		
		initKeywordList();
		
	}

	private void initKeywordList() {
		if (questionProxy != null && questionProxy.getKeywords() != null)
		{
			
			AppLoader.setNoLoader();
			requests.keywordRequest().countKeywordByQuestion(questionProxy.getId()).fire(new BMEReceiver<Integer>() {

				@Override
				public void onSuccess(final Integer response) {
					view.getKeywordTable().setRowCount(response);
					onKeywordTableRangeChanged();
				}
			});
			
			view.getKeywordTable().addRangeChangeHandler(new RangeChangeEvent.Handler() {
				
				@Override
				public void onRangeChange(final RangeChangeEvent event) {
					onKeywordTableRangeChanged();
				}
			});
		}
	}
	
	public void onKeywordTableRangeChanged()
	{
		final Range range = view.getKeywordTable().getVisibleRange();
		AppLoader.setNoLoader();
		requests.keywordRequest().findKeywordByQuestion(questionProxy.getId(), range.getStart(), range.getLength()).fire(new BMEReceiver<List<KeywordProxy>>() {

			@Override
			public void onSuccess(List<KeywordProxy> response) {
				view.getKeywordTable().setRowData(range.getStart(), response);
			}
		});
	}

}
