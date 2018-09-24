package com.googlecode.gwt.test.finder;

import org.antlr.runtime.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {

    static class TokenException extends RuntimeException {

        private static final long serialVersionUID = 7666850302524423170L;

    }

    public static Node parse(String s) {
        try {
            CharStream is = new ANTLRStringStream(s);
            XPathLexer lexer = new XPathLexer(is) {

                public void recover(RecognitionException re) {
                    throw new TokenException();
                }

            };
            CommonTokenStream stream = new CommonTokenStream(lexer);
            XPathParser parser = new XPathParser(stream) {

                protected void mismatch(IntStream input, int ttype, BitSet follow)
                        throws RecognitionException {
                    throw new MismatchedTokenException(ttype, input);
                }

            };
            parser.expr();
            return parser.root;
        } catch (RecognitionException e) {
            return null;
        } catch (TokenException e) {
            return null;
        }

    }

    private String label;

    private String map;

    private Node map_eq;

    private Node next;

    private List<String> paramList;

    public Node() {
    }

    public Node(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getMap() {
        return map;
    }

    public Node getMapEq() {
        return map_eq;
    }

    public Node getNext() {
        return next;
    }

    public List<String> getParams() {
        return paramList == null ? null : Collections.unmodifiableList(paramList);
    }

    public void insertParam(String param) {
        if (this.paramList == null) {
            this.paramList = new ArrayList<>();
        }
        this.paramList.add(0, param);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public void setMapEq(Node n) {
        this.map_eq = n;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public String toString() {
        String s = "/" + label;
        if (paramList != null) {
            s += "(";
            for (String p : paramList) {
                s += p + ",";
            }
            s += ")";
        }
        if (map_eq == null && map != null) {
            s += "{" + map + "}";
        }
        if (map_eq != null && map != null) {
            s += "[" + map_eq.toString() + "=" + map + "]";
        }

        if (next != null) {
            s += next.toString();
        }
        return s;
    }
}
