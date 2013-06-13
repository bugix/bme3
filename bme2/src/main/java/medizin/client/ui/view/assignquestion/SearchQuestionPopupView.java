package medizin.client.ui.view.assignquestion;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public interface SearchQuestionPopupView extends IsWidget{

	 public interface Presenter {
	        void goTo(Place place);
	    }
		/**
		 * Implemented by the owner of the view.
		 */
		interface Delegate {
			// TODO define methods to be delegated!
		}
		
	    void setDelegate(Delegate delegate);
	    
	    void setPresenter(Presenter systemStartActivity);
	    
		public Label getQuestionShortNameLbl();

		public TextBox getQuestionShortNameTxt();

		public Label getQuestionIdLbl() ;

		public IntegerBox getQuestionIdTxt();

		public Label getQuestionTypeLbl();

		public TextBox getQuestionTypeTxt();

		public Button getSearchBtn() ;

		public Button getResetBtn() ;
		
		public void reset();
		
		

}
