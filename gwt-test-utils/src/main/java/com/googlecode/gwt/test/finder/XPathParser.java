// $ANTLR 3.1.1
// D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g
// 2011-03-17 10:41:29
package com.googlecode.gwt.test.finder;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;

import java.util.Stack;

class XPathParser extends Parser {
    public static class condition_return extends ParserRuleReturnScope {
        Object tree;

        public Object getTree() {
            return tree;
        }
    }

    public static class element_return extends ParserRuleReturnScope {
        Object tree;

        public Object getTree() {
            return tree;
        }
    }

    public static class eq_expr_return extends ParserRuleReturnScope {
        Object tree;

        public Object getTree() {
            return tree;
        }
    }

    public static class expr_return extends ParserRuleReturnScope {
        Object tree;

        public Object getTree() {
            return tree;
        }
    }

    public static class internal_condition_return extends ParserRuleReturnScope {
        Object tree;

        public Object getTree() {
            return tree;
        }
    }

    public static class params_return extends ParserRuleReturnScope {
        Object tree;

        public Object getTree() {
            return tree;
        }
    }

    public static class parenthesis_return extends ParserRuleReturnScope {
        Object tree;

        public Object getTree() {
            return tree;
        }
    }

    public static class simple_expr_return extends ParserRuleReturnScope {
        Object tree;

        public Object getTree() {
            return tree;
        }
    }

    public static class sub_return extends ParserRuleReturnScope {
        Object tree;

        public Object getTree() {
            return tree;
        }
    }

    public static class token_return extends ParserRuleReturnScope {
        public Node result;
        Object tree;

        public Object getTree() {
            return tree;
        }
    }

    public static class value_expr_parenthesis_return extends ParserRuleReturnScope {
        Object tree;

        public Object getTree() {
            return tree;
        }
    }

    // delegates
    // delegators

    public static class value_expr_return extends ParserRuleReturnScope {
        Object tree;

        public Object getTree() {
            return tree;
        }
    }

    protected static class token_scope {
        Node currentNode;
    }

    public static final int EOF = -1;

    public static final int EQ = 12;

    public static final BitSet FOLLOW_condition_in_sub117 = new BitSet(
            new long[]{0x0000000000000002L});

    public static final BitSet FOLLOW_element_in_token77 = new BitSet(
            new long[]{0x0000000000000012L});

    public static final BitSet FOLLOW_eq_expr_in_internal_condition175 = new BitSet(
            new long[]{0x0000000000000002L});

    public static final BitSet FOLLOW_EQ_in_eq_expr247 = new BitSet(new long[]{0x00000000000008E0L});

    public static final BitSet FOLLOW_IN_COND_in_condition158 = new BitSet(
            new long[]{0x0000000000000820L});
    ;

    public static final BitSet FOLLOW_IN_in_parenthesis126 = new BitSet(
            new long[]{0x0000000000000820L});

    // $ANTLR end "expr"

    public static final BitSet FOLLOW_IN_in_value_expr_parenthesis215 = new BitSet(
            new long[]{0x00000000000008E2L});

    public static final BitSet FOLLOW_IN_in_value_expr_parenthesis228 = new BitSet(
            new long[]{0x00000000000008E2L});

    public static final BitSet FOLLOW_internal_condition_in_condition160 = new BitSet(
            new long[]{0x0000000000000400L});
    ;

    public static final BitSet FOLLOW_LABEL_in_element99 = new BitSet(
            new long[]{0x0000000000000242L});

    // $ANTLR end "token"

    public static final BitSet FOLLOW_OUT_COND_in_condition162 = new BitSet(
            new long[]{0x0000000000000002L});
    ;

    public static final BitSet FOLLOW_OUT_in_parenthesis130 = new BitSet(
            new long[]{0x0000000000000002L});

    // $ANTLR end "element"

    public static final BitSet FOLLOW_OUT_in_value_expr_parenthesis219 = new BitSet(
            new long[]{0x00000000000008E2L});
    ;

    public static final BitSet FOLLOW_OUT_in_value_expr_parenthesis232 = new BitSet(
            new long[]{0x00000000000008E2L});

    // $ANTLR end "sub"

    public static final BitSet FOLLOW_params_in_params145 = new BitSet(
            new long[]{0x0000000000000002L});
    ;

    public static final BitSet FOLLOW_params_in_parenthesis128 = new BitSet(
            new long[]{0x0000000000000080L});

    // $ANTLR end "parenthesis"

    public static final BitSet FOLLOW_parenthesis_in_sub113 = new BitSet(
            new long[]{0x0000000000000002L});
    ;

    public static final BitSet FOLLOW_set_in_value_expr196 = new BitSet(
            new long[]{0x0000000000000002L});

    // $ANTLR end "params"

    public static final BitSet FOLLOW_simple_expr_in_internal_condition171 = new BitSet(
            new long[]{0x0000000000000002L});
    ;

    public static final BitSet FOLLOW_SLASH_in_expr50 = new BitSet(new long[]{0x0000000000000020L});

    // $ANTLR end "condition"

    public static final BitSet FOLLOW_SLASH_in_token80 = new BitSet(new long[]{0x0000000000000020L});
    ;

    public static final BitSet FOLLOW_sub_in_element101 = new BitSet(new long[]{0x0000000000000002L});

    // $ANTLR end "internal_condition"

    public static final BitSet FOLLOW_token_in_eq_expr245 = new BitSet(
            new long[]{0x0000000000001000L});
    ;

    public static final BitSet FOLLOW_token_in_expr54 = new BitSet(new long[]{0x0000000000000002L});

    // $ANTLR end "simple_expr"

    public static final BitSet FOLLOW_token_in_token84 = new BitSet(new long[]{0x0000000000000002L});
    ;

    public static final BitSet FOLLOW_value_expr_in_params140 = new BitSet(
            new long[]{0x0000000000000102L});

    // $ANTLR end "value_expr"

    public static final BitSet FOLLOW_value_expr_in_simple_expr186 = new BitSet(
            new long[]{0x0000000000000002L});
    ;

    public static final BitSet FOLLOW_value_expr_in_value_expr_parenthesis211 = new BitSet(
            new long[]{0x00000000000008E2L});

    // $ANTLR end "value_expr_parenthesis"

    public static final BitSet FOLLOW_value_expr_in_value_expr_parenthesis224 = new BitSet(
            new long[]{0x00000000000008E2L});
    ;

    public static final BitSet FOLLOW_value_expr_parenthesis_in_eq_expr251 = new BitSet(
            new long[]{0x0000000000000002L});

    // $ANTLR end "eq_expr"

    // Delegated rules

    public static final BitSet FOLLOW_VIRG_in_params143 = new BitSet(new long[]{0x0000000000000820L});
    public static final int IN = 6;
    public static final int IN_COND = 9;
    public static final int LABEL = 5;
    public static final int OUT = 7;
    public static final int OUT_COND = 10;
    public static final int SLASH = 4;
    public static final String[] tokenNames = new String[]{
            "<invalid>", "<EOR>", "<DOWN>", "<UP>", "SLASH", "LABEL", "IN", "OUT", "VIRG",
            "IN_COND", "OUT_COND", "VALUE", "EQ"};
    public static final int VALUE = 11;
    public static final int VIRG = 8;
    public Node root;
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();
    protected Stack<token_scope> token_stack = new Stack<token_scope>();

    public XPathParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }

    public XPathParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);

    }

    // $ANTLR start "condition"
    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:50:1:
    // condition : IN_COND internal_condition OUT_COND ;
    public final XPathParser.condition_return condition() throws RecognitionException {
        XPathParser.condition_return retval = new XPathParser.condition_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token IN_COND12 = null;
        Token OUT_COND14 = null;
        XPathParser.internal_condition_return internal_condition13 = null;

        Object IN_COND12_tree = null;
        Object OUT_COND14_tree = null;

        try {
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:50:11:
            // ( IN_COND internal_condition OUT_COND )
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:50:13:
            // IN_COND internal_condition OUT_COND
            {
                root_0 = adaptor.nil();

                IN_COND12 = (Token) match(input, IN_COND, FOLLOW_IN_COND_in_condition158);
                IN_COND12_tree = adaptor.create(IN_COND12);
                adaptor.addChild(root_0, IN_COND12_tree);

                pushFollow(FOLLOW_internal_condition_in_condition160);
                internal_condition13 = internal_condition();

                state._fsp--;

                adaptor.addChild(root_0, internal_condition13.getTree());
                OUT_COND14 = (Token) match(input, OUT_COND, FOLLOW_OUT_COND_in_condition162);
                OUT_COND14_tree = adaptor.create(OUT_COND14);
                adaptor.addChild(root_0, OUT_COND14_tree);

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    // $ANTLR start "element"
    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:38:1:
    // element : label= LABEL ( sub )? ;
    public final XPathParser.element_return element() throws RecognitionException {
        XPathParser.element_return retval = new XPathParser.element_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token label = null;
        XPathParser.sub_return sub4 = null;

        Object label_tree = null;

        try {
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:38:9:
            // (label= LABEL ( sub )? )
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:38:11:
            // label= LABEL ( sub )?
            {
                root_0 = adaptor.nil();

                label = (Token) match(input, LABEL, FOLLOW_LABEL_in_element99);
                label_tree = adaptor.create(label);
                adaptor.addChild(root_0, label_tree);

                // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:38:23:
                // ( sub )?
                int alt2 = 2;
                int LA2_0 = input.LA(1);

                if ((LA2_0 == IN || LA2_0 == IN_COND)) {
                    alt2 = 1;
                }
                switch (alt2) {
                    case 1:
                        // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:38:23:
                        // sub
                    {
                        pushFollow(FOLLOW_sub_in_element101);
                        sub4 = sub();

                        state._fsp--;

                        adaptor.addChild(root_0, sub4.getTree());

                    }
                    break;

                }

                (token_stack.peek()).currentNode.setLabel((label != null ? label.getText() : null));

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    // $ANTLR start "eq_expr"
    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:62:1:
    // eq_expr : t= token EQ v= value_expr_parenthesis ;
    public final XPathParser.eq_expr_return eq_expr() throws RecognitionException {
        XPathParser.eq_expr_return retval = new XPathParser.eq_expr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token EQ24 = null;
        XPathParser.token_return t = null;

        XPathParser.value_expr_parenthesis_return v = null;

        Object EQ24_tree = null;

        try {
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:62:9:
            // (t= token EQ v= value_expr_parenthesis )
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:62:11:
            // t= token EQ v= value_expr_parenthesis
            {
                root_0 = adaptor.nil();

                pushFollow(FOLLOW_token_in_eq_expr245);
                t = token();

                state._fsp--;

                adaptor.addChild(root_0, t.getTree());
                EQ24 = (Token) match(input, EQ, FOLLOW_EQ_in_eq_expr247);
                EQ24_tree = adaptor.create(EQ24);
                adaptor.addChild(root_0, EQ24_tree);

                pushFollow(FOLLOW_value_expr_parenthesis_in_eq_expr251);
                v = value_expr_parenthesis();

                state._fsp--;

                adaptor.addChild(root_0, v.getTree());

                (token_stack.peek()).currentNode.setMap((v != null ? input.toString(v.start, v.stop)
                        : null));
                (token_stack.peek()).currentNode.setMapEq((t != null ? t.result : null));

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    // $ANTLR start "expr"
    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:21:1:
    // expr : SLASH t= token ;
    public final XPathParser.expr_return expr() throws RecognitionException {
        XPathParser.expr_return retval = new XPathParser.expr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SLASH1 = null;
        XPathParser.token_return t = null;

        Object SLASH1_tree = null;

        try {
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:21:6:
            // ( SLASH t= token )
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:21:8:
            // SLASH t= token
            {
                root_0 = adaptor.nil();

                SLASH1 = (Token) match(input, SLASH, FOLLOW_SLASH_in_expr50);
                SLASH1_tree = adaptor.create(SLASH1);
                adaptor.addChild(root_0, SLASH1_tree);

                pushFollow(FOLLOW_token_in_expr54);
                t = token();

                state._fsp--;

                adaptor.addChild(root_0, t.getTree());

                root = (t != null ? t.result : null);

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    public String getGrammarFileName() {
        return "D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g";
    }

    public String[] getTokenNames() {
        return XPathParser.tokenNames;
    }

    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    // $ANTLR start "internal_condition"
    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:52:1:
    // internal_condition : ( simple_expr | eq_expr ) ;
    public final XPathParser.internal_condition_return internal_condition()
            throws RecognitionException {
        XPathParser.internal_condition_return retval = new XPathParser.internal_condition_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        XPathParser.simple_expr_return simple_expr15 = null;

        XPathParser.eq_expr_return eq_expr16 = null;

        try {
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:52:20:
            // ( ( simple_expr | eq_expr ) )
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:52:22:
            // ( simple_expr | eq_expr )
            {
                root_0 = adaptor.nil();

                // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:52:22:
                // ( simple_expr | eq_expr )
                int alt5 = 2;
                int LA5_0 = input.LA(1);

                if ((LA5_0 == LABEL)) {
                    int LA5_1 = input.LA(2);

                    if ((LA5_1 == OUT_COND)) {
                        alt5 = 1;
                    } else if ((LA5_1 == SLASH || LA5_1 == IN || LA5_1 == IN_COND || LA5_1 == EQ)) {
                        alt5 = 2;
                    } else {
                        NoViableAltException nvae = new NoViableAltException("", 5, 1, input);

                        throw nvae;
                    }
                } else if ((LA5_0 == VALUE)) {
                    alt5 = 1;
                } else {
                    NoViableAltException nvae = new NoViableAltException("", 5, 0, input);

                    throw nvae;
                }
                switch (alt5) {
                    case 1:
                        // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:52:23:
                        // simple_expr
                    {
                        pushFollow(FOLLOW_simple_expr_in_internal_condition171);
                        simple_expr15 = simple_expr();

                        state._fsp--;

                        adaptor.addChild(root_0, simple_expr15.getTree());

                    }
                    break;
                    case 2:
                        // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:52:37:
                        // eq_expr
                    {
                        pushFollow(FOLLOW_eq_expr_in_internal_condition175);
                        eq_expr16 = eq_expr();

                        state._fsp--;

                        adaptor.addChild(root_0, eq_expr16.getTree());

                    }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    // $ANTLR start "params"
    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:46:1:
    // params : v= value_expr ( VIRG params )? ;
    public final XPathParser.params_return params() throws RecognitionException {
        XPathParser.params_return retval = new XPathParser.params_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token VIRG10 = null;
        XPathParser.value_expr_return v = null;

        XPathParser.params_return params11 = null;

        Object VIRG10_tree = null;

        try {
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:46:8:
            // (v= value_expr ( VIRG params )? )
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:46:10:
            // v= value_expr ( VIRG params )?
            {
                root_0 = adaptor.nil();

                pushFollow(FOLLOW_value_expr_in_params140);
                v = value_expr();

                state._fsp--;

                adaptor.addChild(root_0, v.getTree());
                // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:46:23:
                // ( VIRG params )?
                int alt4 = 2;
                int LA4_0 = input.LA(1);

                if ((LA4_0 == VIRG)) {
                    alt4 = 1;
                }
                switch (alt4) {
                    case 1:
                        // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:46:24:
                        // VIRG params
                    {
                        VIRG10 = (Token) match(input, VIRG, FOLLOW_VIRG_in_params143);
                        VIRG10_tree = adaptor.create(VIRG10);
                        adaptor.addChild(root_0, VIRG10_tree);

                        pushFollow(FOLLOW_params_in_params145);
                        params11 = params();

                        state._fsp--;

                        adaptor.addChild(root_0, params11.getTree());

                    }
                    break;

                }

                (token_stack.peek()).currentNode.insertParam((v != null ? input.toString(v.start,
                        v.stop) : null));

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    // $ANTLR start "parenthesis"
    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:44:1:
    // parenthesis : IN params OUT ;
    public final XPathParser.parenthesis_return parenthesis() throws RecognitionException {
        XPathParser.parenthesis_return retval = new XPathParser.parenthesis_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token IN7 = null;
        Token OUT9 = null;
        XPathParser.params_return params8 = null;

        Object IN7_tree = null;
        Object OUT9_tree = null;

        try {
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:44:13:
            // ( IN params OUT )
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:44:15:
            // IN params OUT
            {
                root_0 = adaptor.nil();

                IN7 = (Token) match(input, IN, FOLLOW_IN_in_parenthesis126);
                IN7_tree = adaptor.create(IN7);
                adaptor.addChild(root_0, IN7_tree);

                pushFollow(FOLLOW_params_in_parenthesis128);
                params8 = params();

                state._fsp--;

                adaptor.addChild(root_0, params8.getTree());
                OUT9 = (Token) match(input, OUT, FOLLOW_OUT_in_parenthesis130);
                OUT9_tree = adaptor.create(OUT9);
                adaptor.addChild(root_0, OUT9_tree);

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    // $ANTLR start "simple_expr"
    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:54:1:
    // simple_expr : s= value_expr ;
    public final XPathParser.simple_expr_return simple_expr() throws RecognitionException {
        XPathParser.simple_expr_return retval = new XPathParser.simple_expr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        XPathParser.value_expr_return s = null;

        try {
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:54:13:
            // (s= value_expr )
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:54:15:
            // s= value_expr
            {
                root_0 = adaptor.nil();

                pushFollow(FOLLOW_value_expr_in_simple_expr186);
                s = value_expr();

                state._fsp--;

                adaptor.addChild(root_0, s.getTree());

                (token_stack.peek()).currentNode.setMap((s != null ? input.toString(s.start, s.stop)
                        : null));

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    // $ANTLR start "sub"
    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:42:1:
    // sub : ( parenthesis | condition ) ;
    public final XPathParser.sub_return sub() throws RecognitionException {
        XPathParser.sub_return retval = new XPathParser.sub_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        XPathParser.parenthesis_return parenthesis5 = null;

        XPathParser.condition_return condition6 = null;

        try {
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:42:5:
            // ( ( parenthesis | condition ) )
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:42:7:
            // ( parenthesis | condition )
            {
                root_0 = adaptor.nil();

                // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:42:7:
                // ( parenthesis | condition )
                int alt3 = 2;
                int LA3_0 = input.LA(1);

                if ((LA3_0 == IN)) {
                    alt3 = 1;
                } else if ((LA3_0 == IN_COND)) {
                    alt3 = 2;
                } else {
                    NoViableAltException nvae = new NoViableAltException("", 3, 0, input);

                    throw nvae;
                }
                switch (alt3) {
                    case 1:
                        // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:42:8:
                        // parenthesis
                    {
                        pushFollow(FOLLOW_parenthesis_in_sub113);
                        parenthesis5 = parenthesis();

                        state._fsp--;

                        adaptor.addChild(root_0, parenthesis5.getTree());

                    }
                    break;
                    case 2:
                        // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:42:22:
                        // condition
                    {
                        pushFollow(FOLLOW_condition_in_sub117);
                        condition6 = condition();

                        state._fsp--;

                        adaptor.addChild(root_0, condition6.getTree());

                    }
                    break;

                }

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    // $ANTLR start "token"
    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:25:1:
    // token returns [Node result] : element ( SLASH t= token )? ;
    public final XPathParser.token_return token() throws RecognitionException {
        token_stack.push(new token_scope());
        XPathParser.token_return retval = new XPathParser.token_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SLASH3 = null;
        XPathParser.token_return t = null;

        XPathParser.element_return element2 = null;

        Object SLASH3_tree = null;

        (token_stack.peek()).currentNode = new Node();

        try {
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:30:1:
            // ( element ( SLASH t= token )? )
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:30:3:
            // element ( SLASH t= token )?
            {
                root_0 = adaptor.nil();

                pushFollow(FOLLOW_element_in_token77);
                element2 = element();

                state._fsp--;

                adaptor.addChild(root_0, element2.getTree());
                // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:30:11:
                // ( SLASH t= token )?
                int alt1 = 2;
                int LA1_0 = input.LA(1);

                if ((LA1_0 == SLASH)) {
                    alt1 = 1;
                }
                switch (alt1) {
                    case 1:
                        // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:30:12:
                        // SLASH t= token
                    {
                        SLASH3 = (Token) match(input, SLASH, FOLLOW_SLASH_in_token80);
                        SLASH3_tree = adaptor.create(SLASH3);
                        adaptor.addChild(root_0, SLASH3_tree);

                        pushFollow(FOLLOW_token_in_token84);
                        t = token();

                        state._fsp--;

                        adaptor.addChild(root_0, t.getTree());

                    }
                    break;

                }

                if (t != null) {
                    (token_stack.peek()).currentNode.setNext((t != null ? t.result : null));
                }
                retval.result = (token_stack.peek()).currentNode;

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException e) {
            throw e;
        } finally {
            token_stack.pop();
        }
        return retval;
    }

    // $ANTLR start "value_expr"
    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:58:1:
    // value_expr : ( VALUE | LABEL ) ;
    public final XPathParser.value_expr_return value_expr() throws RecognitionException {
        XPathParser.value_expr_return retval = new XPathParser.value_expr_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set17 = null;

        try {
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:58:12:
            // ( ( VALUE | LABEL ) )
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:58:14:
            // ( VALUE | LABEL )
            {
                root_0 = adaptor.nil();

                set17 = input.LT(1);
                if (input.LA(1) == LABEL || input.LA(1) == VALUE) {
                    input.consume();
                    adaptor.addChild(root_0, adaptor.create(set17));
                    state.errorRecovery = false;
                } else {
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    throw mse;
                }

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

    // $ANTLR start "value_expr_parenthesis"
    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:60:1:
    // value_expr_parenthesis : ( value_expr | IN | OUT ) ( ( value_expr | IN |
    // OUT ) )* ;
    public final XPathParser.value_expr_parenthesis_return value_expr_parenthesis()
            throws RecognitionException {
        XPathParser.value_expr_parenthesis_return retval = new XPathParser.value_expr_parenthesis_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token IN19 = null;
        Token OUT20 = null;
        Token IN22 = null;
        Token OUT23 = null;
        XPathParser.value_expr_return value_expr18 = null;

        XPathParser.value_expr_return value_expr21 = null;

        Object IN19_tree = null;
        Object OUT20_tree = null;
        Object IN22_tree = null;
        Object OUT23_tree = null;

        try {
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:60:24:
            // ( ( value_expr | IN | OUT ) ( ( value_expr | IN | OUT ) )* )
            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:60:26:
            // ( value_expr | IN | OUT ) ( ( value_expr | IN | OUT ) )*
            {
                root_0 = adaptor.nil();

                // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:60:26:
                // ( value_expr | IN | OUT )
                int alt6 = 3;
                switch (input.LA(1)) {
                    case LABEL:
                    case VALUE: {
                        alt6 = 1;
                    }
                    break;
                    case IN: {
                        alt6 = 2;
                    }
                    break;
                    case OUT: {
                        alt6 = 3;
                    }
                    break;
                    default:
                        NoViableAltException nvae = new NoViableAltException("", 6, 0, input);

                        throw nvae;
                }

                switch (alt6) {
                    case 1:
                        // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:60:27:
                        // value_expr
                    {
                        pushFollow(FOLLOW_value_expr_in_value_expr_parenthesis211);
                        value_expr18 = value_expr();

                        state._fsp--;

                        adaptor.addChild(root_0, value_expr18.getTree());

                    }
                    break;
                    case 2:
                        // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:60:40:
                        // IN
                    {
                        IN19 = (Token) match(input, IN, FOLLOW_IN_in_value_expr_parenthesis215);
                        IN19_tree = adaptor.create(IN19);
                        adaptor.addChild(root_0, IN19_tree);

                    }
                    break;
                    case 3:
                        // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:60:45:
                        // OUT
                    {
                        OUT20 = (Token) match(input, OUT, FOLLOW_OUT_in_value_expr_parenthesis219);
                        OUT20_tree = adaptor.create(OUT20);
                        adaptor.addChild(root_0, OUT20_tree);

                    }
                    break;

                }

                // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:60:50:
                // ( ( value_expr | IN | OUT ) )*
                loop8:
                do {
                    int alt8 = 2;
                    int LA8_0 = input.LA(1);

                    if (((LA8_0 >= LABEL && LA8_0 <= OUT) || LA8_0 == VALUE)) {
                        alt8 = 1;
                    }

                    switch (alt8) {
                        case 1:
                            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:60:51:
                            // ( value_expr | IN | OUT )
                        {
                            // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:60:51:
                            // ( value_expr | IN | OUT )
                            int alt7 = 3;
                            switch (input.LA(1)) {
                                case LABEL:
                                case VALUE: {
                                    alt7 = 1;
                                }
                                break;
                                case IN: {
                                    alt7 = 2;
                                }
                                break;
                                case OUT: {
                                    alt7 = 3;
                                }
                                break;
                                default:
                                    NoViableAltException nvae = new NoViableAltException("", 7, 0, input);

                                    throw nvae;
                            }

                            switch (alt7) {
                                case 1:
                                    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:60:52:
                                    // value_expr
                                {
                                    pushFollow(FOLLOW_value_expr_in_value_expr_parenthesis224);
                                    value_expr21 = value_expr();

                                    state._fsp--;

                                    adaptor.addChild(root_0, value_expr21.getTree());

                                }
                                break;
                                case 2:
                                    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:60:65:
                                    // IN
                                {
                                    IN22 = (Token) match(input, IN, FOLLOW_IN_in_value_expr_parenthesis228);
                                    IN22_tree = adaptor.create(IN22);
                                    adaptor.addChild(root_0, IN22_tree);

                                }
                                break;
                                case 3:
                                    // D:\\gwt-test-utils\\gwt-test-utils-csv\\src\\main\\resources\\com\\googlecode\\gwt\\test\\csv\\runner\\XPath.g:60:70:
                                    // OUT
                                {
                                    OUT23 = (Token) match(input, OUT,
                                            FOLLOW_OUT_in_value_expr_parenthesis232);
                                    OUT23_tree = adaptor.create(OUT23);
                                    adaptor.addChild(root_0, OUT23_tree);

                                }
                                break;

                            }

                        }
                        break;

                        default:
                            break loop8;
                    }
                } while (true);

            }

            retval.stop = input.LT(-1);

            retval.tree = adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        } catch (RecognitionException e) {
            throw e;
        } finally {
        }
        return retval;
    }

}