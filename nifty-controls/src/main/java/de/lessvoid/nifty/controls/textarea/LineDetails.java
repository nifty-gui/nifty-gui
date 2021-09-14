package de.lessvoid.nifty.controls.textarea;

public class LineDetails {
    public final int startIndex;
    /**
     * The last character of the lines index, does include the \n
     */
    public final int endIndex;
    /**
     * The number of characters on the line, excluding \n
     */
    public final int numberOfRealChars;

    public final String realTextOnLine;
    
    public LineDetails(int startIndex, int endIndex, String realTextOnLine) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.realTextOnLine = realTextOnLine;
        this.numberOfRealChars = realTextOnLine.length();
    }

    @Override
    public int hashCode() {
        int hash = 7+realTextOnLine.hashCode();hash = 97 * hash + this.startIndex; hash = 97 * hash + this.endIndex;return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true;}
        if (obj == null) {return false;}
        if (getClass() != obj.getClass()) {return false;}
        final LineDetails other = (LineDetails) obj;
        if (this.startIndex != other.startIndex) {return false;}
        if (this.endIndex != other.endIndex) {return false;}
        if (this.numberOfRealChars != other.numberOfRealChars) {return false;}
        if (!this.realTextOnLine.equals(other.realTextOnLine)) {return false;}
        return true;
    }

    @Override
    public String toString() {
        return "LineDetails{" + "s:=" + startIndex + ", e:" + endIndex + ", text:" + realTextOnLine + '}';
    }
        
}
