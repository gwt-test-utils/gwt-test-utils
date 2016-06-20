package com.googlecode.gwt.test.csv.runner;

import com.googlecode.gwt.test.finder.Node;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class XPathTest {

    @Test
    public void testA() {
        assertThat(processString("/toto").toString()).isEqualTo("/toto");
        assertThat(processString("/t").toString()).isEqualTo("/t");
    }

    @Test
    public void testB() {
        assertThat(processString("/toto/tata/tete/titi").toString()).isEqualTo("/toto/tata/tete/titi");
        assertThat(processString("/t(a)").toString()).isEqualTo("/t(a,)");
    }

    @Test
    public void testC() {
        assertThat(processString("/a(b )").toString()).isEqualTo("/a(b ,)");
    }

    @Test
    public void testD() {
        assertThat(processString("/toto(aa bb,zz)/titi/toto").toString()).isEqualTo("/toto(aa bb,zz,)/titi/toto");
    }

    @Test
    public void testE() {
        assertThat(processString("/toto/tata[zyy]").toString()).isEqualTo("/toto/tata{zyy}");
    }

    @Test
    public void testErrorA() {
        assertThat(processString("/toto(")).isNull();
    }

    @Test
    public void testErrorB() {
        assertThat(processString("/toto(a,)")).isNull();
    }

    @Test
    public void testErrorC() {
        assertThat(processString("/toto/")).isNull();
    }

    @Test
    public void testErrorD() {
        assertThat(processString("/toto//")).isNull();
    }

    @Test
    public void testF() {
        assertThat(processString("/toto/tata[zz yy]").toString()).isEqualTo("/toto/tata{zz yy}");
    }

    @Test
    public void testG() {
        assertThat(processString("/toto/tata[aa=zz yy]").toString()).isEqualTo("/toto/tata[/aa=zz yy]");
    }

    @Test
    public void testH() {
        assertThat(processString("/toto/tata[aa/zz(b)/toto=zz yy]").toString()).isEqualTo("/toto/tata[/aa/zz(b,)/toto=zz yy]");
    }

    @Test
    public void testI() {
        assertThat(processString("/toto/tata[aa/zz[b=2]/toto=zz yy]").toString()).isEqualTo("/toto/tata[/aa/zz[/b=2]/toto=zz yy]");
    }

    @Test
    public void testInteg() {
        assertThat(processString("/view/paymentView/nextValidationButton")).isNotNull();
        assertThat(processString("/view/contractChooserPanel/stackPanel/widget(0)/contractTypesAnchors[OC00000002048]")).isNotNull();
        assertThat(processString("/view/configuratorStackPanel/widget[title=TV - 1 erreurs]")).isNotNull();
        assertThat(processString("/view/configuratorStackPanel/widget[title=TV - 1 erreur(s)]")).isNotNull();
        assertThat(processString("/view/configuratorStackPanel/widget[title=TV - 1 erreur(s)]/widget[widget(0)/text=Décodeur HauteDef Enregistreur]/widget(1)")).isNotNull();
        assertThat(processString("/view/configurationGrid/parametersGrid/widgetMap/widgetList[text=portal.contrats.OC00000002048]")).isNotNull();
    }

    @Test
    public void testJ() {
        assertThat(processString("/_toto")).isNull();
        assertThat(processString("/toto")).isNotNull();
    }

    @Test
    public void testK() {
        assertThat(processString("/à")).isNull();
        assertThat(processString("/toto(à)")).isNotNull();
        assertThat(processString("/toto(àéèê)").toString()).isEqualTo("/toto(àéèê,)");
    }

    private Node processString(String s) {
        Node res = Node.parse(s);
        return res;
    }

}
