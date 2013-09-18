package medizin.client.ui.view.question;

import medizin.client.proxy.MatrixValidityProxy;

import com.google.gwt.user.cellview.client.CellTable;

public interface MatrixAnswerListView {

	/**
	 * Implemented by the owner of the view.
	 */
	interface Delegate {
		/*void deleteAnswerClicked(MatrixValidityProxy Answer);*/
		//void editAnswerClicked();
		void addMatrixNewAnswerClicked();
		void editMatrixValidityClicked(MatrixValidityProxy matrixValidity);
		void deleteMatrixValidityClicked(MatrixValidityProxy matrixValidity);

	}

    CellTable<MatrixValidityProxy> getTable();
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
}
