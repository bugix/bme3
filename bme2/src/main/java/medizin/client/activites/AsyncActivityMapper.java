package medizin.client.activites;

import com.google.gwt.place.shared.Place;

/**
*
* @author manish
*/
public interface AsyncActivityMapper{
	
	void getActivity(Place place, ActivityCallbackHandler activityCallbackHandler);

}
