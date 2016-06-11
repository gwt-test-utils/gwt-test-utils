package com.googlecode.gwt.test.internal.patchers.dom;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.googlecode.gwt.test.internal.utils.JsoUtils;
import com.googlecode.gwt.test.patchers.PatchClass;
import com.googlecode.gwt.test.patchers.PatchMethod;

import java.util.List;

@PatchClass(NodeList.class)
class NodeListPatcher {

    @PatchMethod
    static <T extends Node> T getItem(NodeList<T> nodeList, int index) {
        List<T> innerList = JsoUtils.getChildNodeInnerList(nodeList);
        if (innerList.size() <= index) {
            return null;
        } else {
            return innerList.get(index);
        }
    }

    @PatchMethod
    static <T extends Node> int getLength(NodeList<T> nodeList) {
        List<T> innerList = JsoUtils.getChildNodeInnerList(nodeList);
        return innerList.size();
    }

}
