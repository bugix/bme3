package medizin.client.ui.view.question.keyword;

import medizin.client.proxy.KeywordProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

public interface QuestionKeywordView extends IsWidget {

	void setDelegate(Delegate delegate);

	public interface Delegate {

		void keywordAddButtonClicked(String text, QuestionProxy proxy);

		void deleteKeywordClicked(KeywordProxy keyword, QuestionProxy proxy);

		void initKeywordView();

	}

	public IconButton getKeywordAddButton();

	public CellTable<KeywordProxy> getKeywordTable();

	public DefaultSuggestBox<KeywordProxy, EventHandlingValueHolderItem<KeywordProxy>> getKeywordSuggestBox();

	void initKeyword(boolean isAnswerEditable);

	void initKeywordView();

	void setQuestionProxy(QuestionProxy proxy);
}
