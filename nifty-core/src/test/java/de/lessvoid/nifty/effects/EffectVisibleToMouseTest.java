package de.lessvoid.nifty.effects;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.EffectType;
import de.lessvoid.nifty.loaderv2.types.RegisterEffectType;
import de.lessvoid.xml.xpp3.Attributes;
import org.junit.Test;

import java.util.ArrayList;

import static org.easymock.EasyMock.*;

public class EffectVisibleToMouseTest {

    @Test
    public void testThatOnClickEffectsSetVisibleToMouse() {
        Nifty nifty = createNiceMock(Nifty.class);
        expect(nifty.resolveRegisteredEffect("nop")).andReturn(new RegisterEffectType("nop", "de.lessvoid.nifty.effects.impl.Nop"));
        Element element = createNiceMock(Element.class);
        element.setVisibleToMouseEvents(true);
        replay(nifty, element);
        new EffectType().materialize(nifty, element, EffectEventId.onClick, new Attributes("name", "nop"), new ArrayList<Object>());
        verify(nifty, element);
    }
}