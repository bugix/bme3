package medizin.shared;

import java.util.Arrays;
import java.util.List;

public enum BlockingTypes {

	NON_BLOCLING,PERSONAL_BLOCKING,GLOBAL_BLOCKING;
	
	  public static List<BlockingTypes> getValues() {
		  BlockingTypes[] values = BlockingTypes.values();
		  
		  return Arrays.asList(values);
		    
		  }

}
