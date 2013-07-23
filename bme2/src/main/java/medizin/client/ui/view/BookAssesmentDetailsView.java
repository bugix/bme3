package medizin.client.ui.view;

import medizin.client.ui.view.BookAssesmentDetailsView.Delegate;
import medizin.client.proxy.AssesmentProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public interface BookAssesmentDetailsView extends IsWidget {
    
	
	public interface Delegate {

		void createAssementBook(boolean createAVesion);

		Long getAssignemtId();

		void shuffleAssementQuestionsAnswers();

	}


	void setName(String helloName);

    void setPresenter(Presenter activityBookAssesment);

    public interface Presenter {
        void goTo(Place place);
    }

	AbsolutePanel getWorkingArea();

	void reload(AssesmentProxy assesmentProxy);

	ScrollPanel getScrollContainer();

	void setDelegate(Delegate delegate);

	void addButtons();
	
	
    
}
