package de.lessvoid.nifty.controls.textarea;

public class CursorPosition {
    public final int line;
    public final int posOnLine;

    public CursorPosition(int line, int posOnLine) {
        this.line = line;
        this.posOnLine = posOnLine;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.line;
        hash = 47 * hash + this.posOnLine;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {return true;}
        if (obj == null) {return false;}
        if (getClass() != obj.getClass()) {return false;}
        final CursorPosition other = (CursorPosition) obj;
        if (this.line != other.line) {return false;}
        if (this.posOnLine != other.posOnLine) {return false;}
        return true;
    }

    @Override
    public String toString() {
        return "Cursor{" + "line=" + line + ", posOnLine=" + posOnLine + '}';
    }
    
    
    
}
