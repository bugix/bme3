package medizin.client.ui.view.roo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import medizin.client.proxy.McProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;

public class McSetEditor extends McSetEditor_Roo_Gwt {

    @UiField
    FlexTable table;

    @UiField(provided = true)
    @Ignore
    ValueListBox<McProxy> picker = new ValueListBox<McProxy>(medizin.client.ui.view.roo.McProxyRenderer.instance(), new com.google.web.bindery.requestfactory.gwt.ui.client.EntityProxyKeyProvider<McProxy>());

    @UiField
    Button add;

    @UiField
    HTMLPanel editorPanel;

    @UiField
    Button clickToEdit;

    @UiField
    HTMLPanel viewPanel;

    @UiField
    Label viewLabel;

    @UiField
    Style style;

    boolean editing = false;

    private Set<McProxy> values;

    private final List<McProxy> displayedList;

    public McSetEditor() {
        initWidget(GWT.<Binder>create(Binder.class).createAndBindUi(this));
        Driver driver = GWT.<Driver>create(Driver.class);
        ListEditor<McProxy, NameLabel> listEditor = ListEditor.of(new NameLabelSource());
        driver.initialize(listEditor);
        driver.display(new ArrayList<McProxy>());
        displayedList = listEditor.getList();
        editing = false;
    }

    @UiHandler("add")
    public void addClicked(ClickEvent e) {
        if (picker.getValue() == null) {
            return;
        }
        for (McProxy proxy : displayedList) {
            if (proxy.getMcName().equals(picker.getValue().getMcName())) {
                return;
            }
        }
        displayedList.add(picker.getValue());
        viewLabel.setText(makeFlatList(displayedList));
        addToTable(picker.getValue());
    }

    @UiHandler("clickToEdit")
    public void clickToEditClicked(ClickEvent e) {
        toggleEditorMode();
    }

    @Override
    public void flush() {
    }

    @Override
    public Set<medizin.client.proxy.McProxy> getValue() {
        if (values == null && displayedList.size() == 0) {
            return null;
        }
        return new HashSet<McProxy>(displayedList);
    }

    public void onLoad() {
        makeEditable(false);
    }

    @Override
    public void onPropertyChange(String... strings) {
    }

    public void setAcceptableValues(Collection<medizin.client.proxy.McProxy> proxies) {
        picker.setAcceptableValues(proxies);
    }

    @Override
    public void setDelegate(EditorDelegate<java.util.Set<medizin.client.proxy.McProxy>> editorDelegate) {
    }

    @Override
    public void setValue(Set<medizin.client.proxy.McProxy> values) {
        this.values = values;
        makeEditable(editing = false);
        if (displayedList != null) {
            displayedList.clear();
            table.clear();
            if (values != null) {
                for (McProxy e : values) {
                    displayedList.add(e);
                    addToTable(e);
                }
            }
        }
        viewLabel.setText(makeFlatList(values));
    }
    
    public void setNewValue(Set<medizin.client.proxy.McProxy> values) {
        this.values = values;
        
        if (displayedList != null) {
            displayedList.clear();
            table.clear();
            if (values != null) {
                for (McProxy e : values) {
                    displayedList.add(e);
                    addToTable(e);
                }
            }
        }
        viewLabel.setText(makeFlatList(values));
    }

    private void addToTable(McProxy value) {
        addToTable(value, displayedList.size() - 1);
    }

    private void addToTable(McProxy value, int index) {
        final int finalIndex = index;
        if (value != null) {
            table.setText(finalIndex, 0, medizin.client.ui.view.roo.McProxyRenderer.instance().render(value));
            Button removeEntryButton = new Button("x");
            removeEntryButton.addClickHandler(new ClickHandler() {

                public void onClick(final ClickEvent event) {
                    displayedList.remove(finalIndex);
                    table.removeRow(finalIndex);
                    table.clear();
                    viewLabel.setText(makeFlatList(displayedList));
                    Set<McProxy> s=new HashSet<McProxy>(displayedList);
                    setNewValue(s);
                }
            });
            table.setWidget(finalIndex, 1, removeEntryButton);
        }
    }

    private void makeEditable(boolean editable) {
        if (editable) {
            editorPanel.setStylePrimaryName(style.editorPanelVisible());
            viewPanel.setStylePrimaryName(style.viewPanelHidden());
            clickToEdit.setText("Done");
        } else {
            editorPanel.setStylePrimaryName(style.editorPanelHidden());
            viewPanel.setStylePrimaryName(style.viewPanelVisible());
            clickToEdit.setText("Edit");
        }
    }

    private String makeFlatList(Collection<medizin.client.proxy.McProxy> values) {
        return CollectionRenderer.of(medizin.client.ui.view.roo.McProxyRenderer.instance()).render(values);
    }

    private void toggleEditorMode() {
        editing = !editing;
        makeEditable(editing);
    }

    interface Binder extends UiBinder<Widget, McSetEditor> {
    }

    interface Driver extends RequestFactoryEditorDriver<List<McProxy>, ListEditor<McProxy, NameLabel>> {
    }

    class NameLabel extends Composite implements LeafValueEditor<McProxy> {

        final Label mcNameEditor = new Label();

        private McProxy proxy = null;

        public NameLabel() {
            this(null);
        }

        public NameLabel(EventBus eventBus) {
            initWidget(mcNameEditor);
        }

        public void setValue(McProxy proxy) {
            this.proxy = proxy;
            mcNameEditor.setText(medizin.client.ui.view.roo.McProxyRenderer.instance().render(proxy));
        }

        @Override
        public McProxy getValue() {
            return proxy;
        }
    }

    interface Style extends CssResource {

        String editorPanelHidden();

        String editorPanelVisible();

        String viewPanelHidden();

        String viewPanelVisible();
    }

    private class NameLabelSource extends EditorSource<NameLabel> {

        @Override
        public NameLabel create(int index) {
            NameLabel label = new NameLabel();
            addToTable(label.getValue(), index);
            return label;
        }

        @Override
        public void dispose(NameLabel subEditor) {
            subEditor.removeFromParent();
        }

        @Override
        public void setIndex(NameLabel editor, int index) {
            addToTable(editor.getValue(), index);
        }
    }
}
