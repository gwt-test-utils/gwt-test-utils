grammar XPath;

options {
	output = AST;
}

@lexer::header { package com.googlecode.gwt.test.csv.runner; }

@header { package com.octo.googlecode.test.csv.runner; }

@members {
 public Node root; 
}

@rulecatch {
 catch (RecognitionException e) {
  throw e;
 }
}

expr : SLASH t=token {
 root = $t.result;
}; 

token returns [Node result]
scope{ Node currentNode; }
@init {
 $token::currentNode = new Node();
}
: element (SLASH t=token)? {
 if (t != null) {
  $token::currentNode.setNext($t.result);
 }
 $result = $token::currentNode;
 }
;

element : label=LABEL sub ?{
 $token::currentNode.setLabel($label.text);
};

sub : (parenthesis | condition);

parenthesis : IN params OUT;

params : v=value_expr (VIRG params) ? {
 $token::currentNode.insertParam($v.text); 
};

condition : IN_COND internal_condition OUT_COND;

internal_condition : (simple_expr | eq_expr);

simple_expr : s=value_expr {
 $token::currentNode.setMap($s.text);
};

value_expr : (VALUE | LABEL);

value_expr_parenthesis : (value_expr | IN | OUT) ((value_expr | IN | OUT))*;

eq_expr : t=token EQ v=value_expr_parenthesis {
 $token::currentNode.setMap($v.text);
 $token::currentNode.setMapEq($t.result);
};

SLASH : '/';
IN : '(';
OUT : ')';
IN_COND : '[';
OUT_COND : ']';
VIRG : ',';
EQ : '=';
LABEL : ('a'..'z' | 'A'..'Z' | '0'..'9' ) (('a'..'z' | 'A'..'Z' | '0'..'9' | '_' | ' '))*;
VALUE : ('a'..'z' | 'A'..'Z' | '0'..'9' | '\u00E0' | '\u00E8' | '\u00E9' | '\u00EA' | '.' ) (('a'..'z' | 'A'..'Z' | '0'..'9' | '\u00E0' | '\u00E8' | '\u00E9' | '\u00EA' | '.' | '?' | '-' | ' ' | '_' ))*;
