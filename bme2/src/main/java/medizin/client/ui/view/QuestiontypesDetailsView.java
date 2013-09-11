package medizin.client.ui.view;

import medizin.client.proxy.QuestionTypeProxy;

import com.google.common.base.Function;
import com.google.gwt.user.client.ui.IsWidget;


public interface QuestiontypesDetailsView extends IsWidget {
    void setName(String helloName);

    void setDelegate(Delegate delegate);
    void setValue(QuestionTypeProxy proxy);

	interface Delegate {
		void deleteClicked();

		void editClicked();
		

		void newClicked(String institutionName);

		void newClicked();

		void getQuestionCount(QuestionTypeProxy proxy, Function<Long, Void> function);
	}
}
