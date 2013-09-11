package medizin.client.ui.widget.mathjax;

import medizin.client.ui.widget.IconButton;
import medizin.client.ui.widget.mathjax.MathJaxButton.MyMath20Symbol;
import medizin.client.ui.widget.mathjax.MathJaxButton.Symbol;
import medizin.client.util.MathJaxs;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MathJaxDialogImpl extends DialogBox implements MathJaxDialog,ClickHandler {

	private static MathJaxDialogImplUiBinder uiBinder = GWT.create(MathJaxDialogImplUiBinder.class);

	interface MathJaxDialogImplUiBinder extends UiBinder<Widget, MathJaxDialogImpl> {
	}

	@UiField TabBar symbolTab;
	@UiField SimplePanel content;
	@UiField TextArea txtInputBox;
	@UiField Label lblOutput;
	@UiField IconButton close;
	@UiField IconButton save;	
	
	private final VerticalPanel mathVerticalPanel = new VerticalPanel();
	private final VerticalPanel logicVerticalPanel = new VerticalPanel();
	private final VerticalPanel gkFunVerticalPanel = new VerticalPanel();
	private final VerticalPanel arrowVerticalPanel = new VerticalPanel();
	private final VerticalPanel symbolsVerticalPanel = new VerticalPanel();
	private boolean isSaved = false;
	
	public MathJaxDialogImpl() {
		setWidget(uiBinder.createAndBindUi(this));
		
		symbolTab.addTab("Math");
		symbolTab.addTab("Function");
		symbolTab.addTab("Logic");	
		symbolTab.addTab("Arrow");
		symbolTab.addTab("Symbol");
		setTitle("Equation Editor");
		setText("Equation Editor");
		setGlassEnabled(true);
		lblOutput.setWordWrap(true);
		lblOutput.setWidth("200px");
		lblOutput.setHeight("130px");
		
		symbolTab.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
			
			@Override
			public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {

				Integer value = event.getItem();
				
				if(value == 0) {
					// Math
					addMathToContent();
				}else if(value == 1) {
					// GK & Fun
					addGkFunToContent();
				}else if(value == 2) {
					 // Logic
					addLogicToContent();
				}else if(value == 3) {
					//arrows
					addArrowToContent();
				}else if(value == 4) {
					//other symbols
					addSymbolsToContent();
				}

				
			}

		});
		
		close.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				txtInputBox.setText("");
				isSaved = false;
				hide();
				
			}
		});
		
		save.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				isSaved = true;
				hide();
			}
		});
		
		txtInputBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {

				callMathJax(txtInputBox, lblOutput);
			}
		});

		txtInputBox.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {

				callMathJax(txtInputBox, lblOutput);
			}
		});
		txtInputBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				callMathJax(txtInputBox, lblOutput);
			}
		});
		createMathContent();
		createGkFunToContent();
		createLogicToContent();
		createArrowToContent();
		createSymbolsToContent();
		symbolTab.selectTab(0);
	}
	
	

	private Widget addMathBtn(MathJaxButton.Group group,MathJaxButton.Symbol symbol) {
		MathJaxButton button = new MathJaxButton();
		button.setValues(group, symbol);
		button.addClickHandler(this);
		return button;
	}


	private void addLogicToContent() {
		content.clear();
		content.add(logicVerticalPanel);
	}

	private void addGkFunToContent() {
		content.clear();
		content.add(gkFunVerticalPanel);
	}

	private void addMathToContent() {
		content.clear();
		content.add(mathVerticalPanel);
	}

	private void addSymbolsToContent() {
		content.clear();
		content.add(symbolsVerticalPanel);
	}

	private void addArrowToContent() {
		content.clear();
		content.add(arrowVerticalPanel);
	}

	@Override
	public void onClick(ClickEvent event) {
		Object source = event.getSource();
		
		if(source instanceof MathJaxButton) {
			MathJaxButton b = (MathJaxButton) source;
			Log.info("cursor position : " + txtInputBox.getCursorPos());
			StringBuilder text = new StringBuilder(txtInputBox.getText());
			text.insert(txtInputBox.getCursorPos(), b.getValue());
			txtInputBox.setText(text.toString());
			txtInputBox.setFocus(true);
			callMathJax(txtInputBox, lblOutput);
		}
		
	}

	private final void callMathJax(TextArea textArea, Label output) {
		String value = textArea.getValue();
		output.setText("\\[" + value + "\\]");
		MathJaxs.renderLatexResult(output.asWidget().getElement());
	}
	
	
	private void createSymbolsToContent() {

		symbolsVerticalPanel.clear();
		symbolsVerticalPanel.setWidth("400px");
		symbolsVerticalPanel.setHeight("348px");
		
		symbolsVerticalPanel.getElement().getStyle().setPadding(5, Unit.PX);
		
		{
			FlowPanel flowPanel = new FlowPanel();
			
			for (Symbol symbol : MathJaxButton.MySymbols20Symbol.values()) {
				flowPanel.add(addMathBtn(MathJaxButton.Group.MySymbol20, symbol));	
			}
			
			symbolsVerticalPanel.add(flowPanel);
			
		}
	}

	private void createArrowToContent() {
		arrowVerticalPanel.clear();
		arrowVerticalPanel.setWidth("400px");
		arrowVerticalPanel.setHeight("348px");
		arrowVerticalPanel.getElement().getStyle().setPadding(5, Unit.PX);
		
		{
			FlowPanel flowPanel = new FlowPanel();
			
			for (Symbol symbol : MathJaxButton.MyArrow20Symbol.values()) {
				flowPanel.add(addMathBtn(MathJaxButton.Group.MyArrow20, symbol));	
			}
			
			arrowVerticalPanel.add(flowPanel);
			
		}
	}
	
	private void createLogicToContent() {
		logicVerticalPanel.clear();
		logicVerticalPanel.setWidth("400px");
		logicVerticalPanel.setHeight("348px");
		logicVerticalPanel.getElement().getStyle().setPadding(5, Unit.PX);
		
		{
			FlowPanel flowPanel = new FlowPanel();
			
			for (Symbol symbol : MathJaxButton.MyLogic20Symbol.values()) {
				flowPanel.add(addMathBtn(MathJaxButton.Group.MyLogic20, symbol));	
			}
			
			logicVerticalPanel.add(flowPanel);
			
		}
	}


	private void createGkFunToContent() {
		gkFunVerticalPanel.clear();
		gkFunVerticalPanel.setWidth("400px");
		gkFunVerticalPanel.setHeight("348px");
		gkFunVerticalPanel.getElement().getStyle().setPadding(5, Unit.PX);
		
		{
			FlowPanel flowPanel = new FlowPanel();
			
			for (Symbol symbol : MathJaxButton.MyGreek20Symbol.values()) {
				flowPanel.add(addMathBtn(MathJaxButton.Group.MyGreek20, symbol));	
			}
			
			gkFunVerticalPanel.add(flowPanel);
			
		}
		
		{
			FlowPanel flowPanel = new FlowPanel();
			
			for (Symbol symbol : MathJaxButton.MyFun4020Symbol.values()) {
				flowPanel.add(addMathBtn(MathJaxButton.Group.MyFun4020, symbol));	
			}
			
			gkFunVerticalPanel.add(flowPanel);
			
		}
	}


	private void createMathContent() {
		mathVerticalPanel.clear();
		mathVerticalPanel.setWidth("400px");
		mathVerticalPanel.setHeight("340px");
		mathVerticalPanel.getElement().getStyle().setPadding(5, Unit.PX);
		
		{
			FlowPanel myMath20FlowPanel = new FlowPanel();
		
			for (MyMath20Symbol symbol : MathJaxButton.MyMath20Symbol.values()) {
				myMath20FlowPanel.add(addMathBtn(MathJaxButton.Group.MyMath20, symbol));	
			}
			
			mathVerticalPanel.add(myMath20FlowPanel);
		}
		
		{
			FlowPanel flowPanel = new FlowPanel();
		
			for (Symbol symbol : MathJaxButton.MyMath3528Symbol.values()) {
				flowPanel.add(addMathBtn(MathJaxButton.Group.MyMath3528, symbol));	
			}
			
			mathVerticalPanel.add(flowPanel);
		}
		
		{
			FlowPanel flowPanel = new FlowPanel();
		
			for (Symbol symbol : MathJaxButton.MyMath3545Symbol.values()) {
				flowPanel.add(addMathBtn(MathJaxButton.Group.MyMath3545, symbol));	
			}
			
			mathVerticalPanel.add(flowPanel);
		}
		
		{
			FlowPanel flowPanel = new FlowPanel();
		
			for (Symbol symbol : MathJaxButton.MyMathHeigh45Symbol.values()) {
				flowPanel.add(addMathBtn(MathJaxButton.Group.MyMathHeigh45, symbol));	
			}
			
			mathVerticalPanel.add(flowPanel);
		}
		
		
	}

	public String getEquation() {
		if(isSaved) {
			return "\\[" + txtInputBox.getText() + "\\]";	
		} else {
			return "";
		}
	}

}
