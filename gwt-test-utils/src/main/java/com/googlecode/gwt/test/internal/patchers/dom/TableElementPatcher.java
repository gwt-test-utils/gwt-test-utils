package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.dom.client.*;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;
import com.googlecode.gwt.test.utils.JavaScriptObjects;

import java.util.ArrayList;
import java.util.List;

@PatchClass(TableElement.class)
class TableElementPatcher {

    private static final String TCAPTION = "TCAPTION";
    private static final String TFOOT = "tFoot";
    private static final String THEAD = "tHead";

    @PatchMethod
    static TableCaptionElement createCaption(TableElement e) {
        TableCaptionElement caption = JavaScriptObjects.getObject(e, TCAPTION);
        if (caption == null) {
            caption = Document.get().createCaptionElement();
            JavaScriptObjects.setProperty(e, TCAPTION, caption);
            e.insertFirst(caption);
        }
        return caption;
    }

    @PatchMethod
    static TableSectionElement createTFoot(TableElement e) {
        TableSectionElement tfoot = JavaScriptObjects.getObject(e, TFOOT);
        if (tfoot == null) {
            tfoot = Document.get().createTFootElement();

            TableSectionElement thead = e.getTHead();
            if (thead != null) {
                e.insertAfter(tfoot, thead);
            } else {
                TableCaptionElement caption = e.getCaption();
                if (caption == null) {
                    e.insertFirst(tfoot);
                } else {
                    e.insertAfter(tfoot, caption);
                }
            }
            JavaScriptObjects.setProperty(e, TFOOT, tfoot);
        }

        return tfoot;
    }

    @PatchMethod
    static TableSectionElement createTHead(TableElement e) {
        TableSectionElement thead = JavaScriptObjects.getObject(e, THEAD);
        if (thead == null) {
            thead = Document.get().createTHeadElement();
            TableCaptionElement caption = e.getCaption();
            if (caption == null) {
                e.insertFirst(thead);
            } else {
                e.insertAfter(thead, caption);
            }
            JavaScriptObjects.setProperty(e, THEAD, thead);
        }

        return thead;
    }

    @PatchMethod
    static void deleteCaption(TableElement e) {
        TableCaptionElement caption = JavaScriptObjects.getObject(e, TCAPTION);
        if (caption != null) {
            JavaScriptObjects.remove(e, TCAPTION);
            e.removeChild(caption);
        }
    }

    @PatchMethod
    static void deleteRow(TableElement e, int index) {
        NodeList<TableRowElement> rows = e.getRows();

        if (rows.getLength() < 1) {
            return;
        }

        if (index == -1) {
            index = rows.getLength() - 1;
        }

        TableRowElement rowToDelete = rows.getItem(index);
        e.removeChild(rowToDelete);
    }

    @PatchMethod
    static void deleteTFoot(TableElement e) {
        TableSectionElement tfoot = JavaScriptObjects.getObject(e, TFOOT);
        if (tfoot != null) {
            JavaScriptObjects.remove(e, TFOOT);
            e.removeChild(tfoot);
        }
    }

    @PatchMethod
    static void deleteTHead(TableElement e) {
        TableSectionElement thead = JavaScriptObjects.getObject(e, THEAD);
        if (thead != null) {
            JavaScriptObjects.remove(e, THEAD);
            e.removeChild(thead);
        }
    }

    @PatchMethod
    static TableCaptionElement getCaption(TableElement e) {
        return JavaScriptObjects.getObject(e, TCAPTION);

    }

    @PatchMethod
    static NodeList<TableRowElement> getRows(TableElement e) {
        // deep search
        return e.getElementsByTagName("tr").cast();
    }

    @PatchMethod
    static NodeList<TableSectionElement> getTBodies(TableElement e) {
        return getElementByTagName(e, "tbody").cast();
    }

    @PatchMethod
    static TableSectionElement getTFoot(TableElement e) {
        return JavaScriptObjects.getObject(e, TFOOT);
    }

    @PatchMethod
    static TableSectionElement getTHead(TableElement e) {
        return JavaScriptObjects.getObject(e, THEAD);
    }

    @PatchMethod
    static TableRowElement insertRow(TableElement e, int index) {
        NodeList<TableRowElement> rows = e.getRows();
        TableRowElement newRow = Document.get().createTRElement();
        if (rows.getLength() < 1) {
            TableSectionElement tbody = Document.get().createTBodyElement();
            e.appendChild(tbody);
            tbody.appendChild(newRow);
        } else {

            if (index == -1 || index >= rows.getLength()) {
                TableRowElement after = rows.getItem(rows.getLength() - 1);
                after.getParentElement().insertAfter(newRow, after);
            } else {
                TableRowElement before = rows.getItem(index);
                before.getParentElement().insertBefore(newRow, before);
            }
        }

        return newRow;
    }

    @PatchMethod
    static void setTFoot(TableElement e, TableSectionElement tFoot) {
        TableSectionElement old = JavaScriptObjects.getObject(e, TFOOT);

        if (old != null && tFoot != null) {
            e.replaceChild(tFoot, old);
        } else if (tFoot != null) {
            e.appendChild(tFoot);
        } else {
            e.removeChild(old);
        }

        JavaScriptObjects.setProperty(e, TFOOT, tFoot);

    }

    @PatchMethod
    static void setTHead(TableElement e, TableSectionElement tHead) {
        TableSectionElement old = JavaScriptObjects.getObject(e, THEAD);

        if (old != null && tHead != null) {
            e.replaceChild(tHead, old);
        } else if (tHead != null) {
            e.appendChild(tHead);
        } else {
            e.removeChild(old);
        }

        JavaScriptObjects.setProperty(e, THEAD, tHead);
    }

    /**
     * Specific function which does not inspect deep.
     *
     * @param tag
     * @return
     */
    private static NodeList<Element> getElementByTagName(TableElement e, String tagName) {

        NodeList<Node> childs = e.getChildNodes();
        List<Element> list = new ArrayList<>();

        for (int i = 0; i < childs.getLength(); i++) {
            Node n = childs.getItem(i);
            if (Element.is(n)) {
                Element childElement = n.cast();
                if (tagName.equalsIgnoreCase(childElement.getTagName())) {
                    list.add(childElement);
                }
            }
        }

        return JsoUtils.newNodeList(list);
    }
}
