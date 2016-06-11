package com.googlecode.gwt.test.uibinder.widget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IsWidget;
import com.googlecode.gwt.test.uibinder.UiBinderXmlUtils;
import com.googlecode.gwt.test.uibinder.UiObjectTag;
import com.googlecode.gwt.test.uibinder.UiObjectTagFactory;

import java.util.List;
import java.util.Map;

/**
 * Handles subclasses of {@link Grid} (which declare &lt;g:row>, &lt;g:cell> and &lt;g:customCell>
 * tags).
 *
 * @author Gael Lazzari
 */
public class UiGridTagFactory implements UiObjectTagFactory<Grid> {

    private static class GridTag extends UiObjectTag<Grid> {

        private int currentRowIndex = 0;
        private int numColumns;

        @Override
        protected void appendElement(Grid wrapped, Element element, String namespaceURI,
                                     List<IsWidget> childWidgets) {
            if (!ROW_TAG.equals(element.getTagName())
                    || !UiBinderXmlUtils.CLIENTUI_NSURI.equals(namespaceURI)) {
                super.appendElement(wrapped, element, namespaceURI, childWidgets);
            } else {
                // handle <g:row> tag
                handleRow(wrapped, element);
                currentRowIndex++;
            }
        }

        @Override
        protected void finalizeObject(Grid uiObject) {
            // nothing to do
        }

        @Override
        protected void initializeObject(Grid wrapped, Map<String, Object> attributes, Object owner) {
            // nothing to do
        }

        private void checkGridSize(Grid wrapped, int columnIndex) {
            if (numColumns <= columnIndex) {
                numColumns = columnIndex + 1;
                wrapped.resize(currentRowIndex + 1, numColumns);
            }
        }

        private void handleCell(Grid wrapped, Element element, int columnIndex) {
            checkGridSize(wrapped, columnIndex);
            wrapped.setHTML(currentRowIndex, columnIndex, element.getInnerHTML());

            handleCellStyle(wrapped, element, columnIndex);
        }

        private void handleCellStyle(Grid wrapped, Element element, int columnIndex) {
            String styleName = element.getAttribute(STYLE_ATTR);
            if (styleName.length() > 0) {
                wrapped.getCellFormatter().setStyleName(currentRowIndex, columnIndex, styleName);
            }
        }

        private void handleCustomCell(Grid wrapped, Element element, List<IsWidget> childWidgets,
                                      int columnIndex) {
            checkGridSize(wrapped, columnIndex);
            // should only contains one widget per <g:customCell> tag
            IsWidget w = (childWidgets.size() > 0) ? childWidgets.get(0) : null;
            wrapped.setWidget(currentRowIndex, columnIndex, w);

            handleCellStyle(wrapped, element, columnIndex);
        }

        private void handleRow(Grid wrapped, Element element) {
            // 1. insert
            wrapped.insertRow(currentRowIndex);

            // 2. handle row style
            String styleName = element.getAttribute(STYLE_ATTR);
            if (styleName.length() > 0) {
                wrapped.getRowFormatter().setStyleName(currentRowIndex, styleName);
            }

            // 3. handle child cells
            handleRowCells(wrapped, element);
        }

        private void handleRowCells(Grid wrapped, Element element) {
            int columnIndex = 0;
            NodeList<Node> childs = element.getChildNodes();
            for (int i = 0; i < childs.getLength(); i++) {
                Element e = childs.getItem(i).cast();
                if (CELL_TAG.equals(e.getTagName())) {
                    handleCell(wrapped, e, columnIndex++);
                } else if (CUSTOMCELL_TAG.equals(e.getTagName())) {
                    handleCustomCell(wrapped, e, UiBinderXmlUtils.getChildWidgets(e), columnIndex++);
                }
            }
        }

    }

    private static final String CELL_TAG = "cell";
    private static final String CUSTOMCELL_TAG = "customCell";
    private static final String ROW_TAG = "row";
    private static final String STYLE_ATTR = "styleName";

    /*
     * (non-Javadoc)
     *
     * @see com.googlecode.gwt.test.uibinder.UiObjectTagFactory#createUiObjectTag( java.lang .Class,
     * java.util.Map)
     */
    public UiObjectTag<Grid> createUiObjectTag(Class<?> clazz, Map<String, Object> attributes) {

        if (!Grid.class.isAssignableFrom(clazz)) {
            return null;
        }

        return new GridTag();
    }

}
