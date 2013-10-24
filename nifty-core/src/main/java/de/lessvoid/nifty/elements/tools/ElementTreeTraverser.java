/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lessvoid.nifty.elements.tools;

import de.lessvoid.nifty.elements.Element;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author telamon
 */
public class ElementTreeTraverser implements Iterator<Element>{
    private ArrayList<Iterator<Element>> iterators =  new ArrayList<Iterator<Element>>();
    private Iterator<Element> current;
    public ElementTreeTraverser(Element e){
        current = e.getChildren().listIterator();
    }

    @Override
    public boolean hasNext() {
        return current.hasNext() || !iterators.isEmpty();
    }

    @Override
    public Element next() {
        if(current.hasNext()){
            Element e = current.next();
            if(!e.getChildren().isEmpty())
                iterators.add(e.getChildren().listIterator());

            return e;
        }else if(!iterators.isEmpty()){
            current = iterators.remove(0);
            return next();
        }
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
