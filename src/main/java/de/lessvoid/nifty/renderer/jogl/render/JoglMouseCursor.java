package de.lessvoid.nifty.renderer.jogl.render;

import java.awt.Cursor;

import de.lessvoid.nifty.spi.render.MouseCursor;

public class JoglMouseCursor implements MouseCursor {

    private Cursor cursor;
    
    public JoglMouseCursor(final Cursor cursor){
        this.cursor = cursor;
    }
    
    @Override
    public void dispose() {      
    }

    Cursor getCursor(){
        return cursor;
    }
}
