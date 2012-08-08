package com.googlecode.gwt.test.csv.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.googlecode.gwt.test.finder.Node;

public class XPathTest {

   @Test
   public void testA() {
      assertEquals("/toto", processString("/toto").toString());
      assertEquals("/t", processString("/t").toString());
   }

   @Test
   public void testB() {
      assertEquals("/toto/tata/tete/titi", processString("/toto/tata/tete/titi").toString());
      assertEquals("/t(a,)", processString("/t(a)").toString());
   }

   @Test
   public void testC() {
      assertEquals("/a(b ,)", processString("/a(b )").toString());
   }

   @Test
   public void testD() {
      assertEquals("/toto(aa bb,zz,)/titi/toto",
               processString("/toto(aa bb,zz)/titi/toto").toString());
   }

   @Test
   public void testE() {
      assertEquals("/toto/tata{zyy}", processString("/toto/tata[zyy]").toString());
   }

   @Test
   public void testErrorA() {
      assertNull(processString("/toto("));
   }

   @Test
   public void testErrorB() {
      assertNull(processString("/toto(a,)"));
   }

   @Test
   public void testErrorC() {
      assertNull(processString("/toto/"));
   }

   @Test
   public void testErrorD() {
      assertNull(processString("/toto//"));
   }

   @Test
   public void testF() {
      assertEquals("/toto/tata{zz yy}", processString("/toto/tata[zz yy]").toString());
   }

   @Test
   public void testG() {
      assertEquals("/toto/tata[/aa=zz yy]", processString("/toto/tata[aa=zz yy]").toString());
   }

   @Test
   public void testH() {
      assertEquals("/toto/tata[/aa/zz(b,)/toto=zz yy]",
               processString("/toto/tata[aa/zz(b)/toto=zz yy]").toString());
   }

   // @Test
   // public void testErrorA() {
   // assertNull(processString("toto"));
   // }

   @Test
   public void testI() {
      assertEquals("/toto/tata[/aa/zz[/b=2]/toto=zz yy]",
               processString("/toto/tata[aa/zz[b=2]/toto=zz yy]").toString());
   }

   @Test
   public void testInteg() {
      assertNotNull(processString("/view/paymentView/nextValidationButton"));
      assertNotNull(processString("/view/contractChooserPanel/stackPanel/widget(0)/contractTypesAnchors[OC00000002048]"));
      assertNotNull(processString("/view/configuratorStackPanel/widget[title=TV - 1 erreurs]"));
      assertNotNull(processString("/view/configuratorStackPanel/widget[title=TV - 1 erreur(s)]"));
      assertNotNull(processString("/view/configuratorStackPanel/widget[title=TV - 1 erreur(s)]/widget[widget(0)/text=Décodeur HauteDef Enregistreur]/widget(1)"));
      assertNotNull(processString("/view/configurationGrid/parametersGrid/widgetMap/widgetList[text=portal.contrats.OC00000002048]"));
   }

   @Test
   public void testJ() {
      assertNull(processString("/_toto"));
      assertNotNull(processString("/toto"));
   }

   @Test
   public void testK() {
      assertNull(processString("/à"));
      assertNotNull(processString("/toto(à)"));
      assertEquals("/toto(àéèê,)", processString("/toto(àéèê)").toString());
   }

   private Node processString(String s) {
      Node res = Node.parse(s);
      return res;
   }

}
