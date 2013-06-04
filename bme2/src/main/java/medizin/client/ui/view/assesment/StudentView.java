package medizin.client.ui.view.assesment;

import medizin.client.proxy.StudentToAssesmentProxy;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.IsWidget;

public interface StudentView extends IsWidget {
	
	public interface Presenter {
       
    }

 	interface Delegate {
		void importClicked();
		
		void deactivateClicked(StudentToAssesmentProxy studentToAssesmentProxy);
	}

    CellTable<StudentToAssesmentProxy> getTable();
    
    String[] getPaths();
    
    void setDelegate(Delegate delegate);
    
    public Hidden getHidden();

    void setPresenter(Presenter systemStartActivity);
}
