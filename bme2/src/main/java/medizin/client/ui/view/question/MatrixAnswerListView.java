package medizin.client.ui.view.question;

import medizin.client.proxy.AnswerProxy;
import medizin.client.proxy.MatrixValidityProxy;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.cellview.client.CellTable;

public interface MatrixAnswerListView {

	public interface Presenter {
        void goTo(Place place);
    }
	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		void deleteAnswerClicked(MatrixValidityProxy Answer);
		//void editAnswerClicked();
		void addMatrixNewAnswerClicked();

	}

    CellTable<MatrixValidityProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
}
