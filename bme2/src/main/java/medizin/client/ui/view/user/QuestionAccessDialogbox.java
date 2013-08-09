package medizin.client.ui.view.user;

import java.util.List;

import medizin.client.proxy.InstitutionProxy;
import medizin.client.proxy.QuestionEventProxy;
import medizin.client.proxy.QuestionProxy;
import medizin.client.proxy.UserAccessRightsProxy;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.EventHandlingValueHolderItem;
import medizin.client.ui.widget.widgetsnewcustomsuggestbox.test.client.ui.widget.suggest.impl.DefaultSuggestBox;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

public interface QuestionAccessDialogbox extends IsWidget {
 

    void setPresenter(Presenter activityInstitution);
    void display();

    public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void addClicked(medizin.shared.AccessRights rights, QuestionProxy questionEvent);

		void filterInstitutionQuestionChanged(Long value); //change
		void filterEventQuestionChanged(Long value); //change

		void filterQuestionChanged(String text);
		
		void filterSearchTextChanged(boolean value);
		void filterSearchKeywordChanged(boolean value);


	}

    CellTable<QuestionProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    DefaultSuggestBox<InstitutionProxy, EventHandlingValueHolderItem<InstitutionProxy>> getSearchInstitution();
	DefaultSuggestBox<QuestionEventProxy, EventHandlingValueHolderItem<QuestionEventProxy>> getSearchEvent();
	
	void setUserAccessRightsList(List<UserAccessRightsProxy> userAccessRightsList);

}
