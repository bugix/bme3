package medizin.client.util;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;


public final class MathJaxs {
	
	public static final native void renderLatexResult(com.google.gwt.dom.client.Element e) /*-{
		//$wnd.MathJax.Hub.Queue(["Typeset",$wnd.MathJax.Hub]);       
		if (typeof $wnd.MathJax !== 'undefined' && $wnd.MathJax != null) {
			//alert("level 1");
			if (typeof $wnd.MathJax.Hub !== 'undefined'
					&& $wnd.MathJax.Hub != null) {
				//alert("level 2");
				if (typeof $wnd.MathJax.Hub.Typeset == 'function') {
					//alert("level 3");
					$wnd.MathJax.Hub.Typeset(e);
				}
			}
		}
	}-*/;

	public static void delayRenderLatexResult(final Element bodyElement) {
		new Timer() {
			
			@Override
			public void run() {
				renderLatexResult(bodyElement);
				this.cancel();
			}
		}.schedule(200);
	}
	
}
