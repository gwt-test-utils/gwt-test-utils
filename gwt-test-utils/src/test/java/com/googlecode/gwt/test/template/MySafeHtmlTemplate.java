package com.googlecode.gwt.test.template;

import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface MySafeHtmlTemplate extends SafeHtmlTemplates {

    @Template("<div style=\"outline:none;\">{0}</div>")
    SafeHtml div(SafeHtml contents);

    @Template("<div style=\"outline:none;\" tabindex=\"{0}\" accessKey=\"{1}\">{2}</div>")
    SafeHtml divFocusableWithKey(int tabIndex, char accessKey, SafeHtml contents);

    @Template("<td class=\"{0}\">{1}</td>")
    SafeHtml td(String classes, SafeHtml contents);

    @Template("<td class=\"{0}\" align=\"{1}\" valign=\"{2}\">{3}</td>")
    SafeHtml tdBothAlign(String classes, String hAlign, String vAlign, SafeHtml contents);

    @Template("<td class=\"{0}\" align=\"{1}\">{2}</td>")
    SafeHtml tdHorizontalAlign(String classes, String hAlign, SafeHtml contents);

    @Template("<td class=\"{0}\" valign=\"{1}\">{2}</td>")
    SafeHtml tdVerticalAlign(String classes, String vAlign, SafeHtml contents);

    @Template("<table><tfoot>{0}</tfoot></table>")
    SafeHtml tfoot(SafeHtml rowHtml);

    @Template("<th colspan=\"{0}\" class=\"{1}\">{2}</th>")
    SafeHtml th(int colspan, String classes, SafeHtml contents);
}
