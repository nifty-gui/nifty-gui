package de.lessvoid.nifty.controls.textarea;


import java.util.ArrayList;
import java.util.List;

public class SplitLinesDetails {
    public final List<LineDetails> lineDetails;

    public SplitLinesDetails(String textToSplit){
        this(calculateLineStarts(textToSplit));
    }
    
    public SplitLinesDetails(List<LineDetails> lineDetails) {
        this.lineDetails = lineDetails;
    }
    
    public CursorPosition convertAbsoluteCursorPositionToLineAware(int cursorPosition){
        int lineNumber = 0;
        
        //find correct line number
        while(lineNumber<lineDetails.size()-1 && lineDetails.get(lineNumber+1).startIndex<=cursorPosition){
            lineNumber++;
        }
        
        int onLineIndex = cursorPosition - lineDetails.get(lineNumber).startIndex;
        
        return new CursorPosition(lineNumber, onLineIndex);
    }
    
    public int getNumberOfLines() {
        return lineDetails.size();
    }
    
    public LineDetails getLineDetail(int lineNumber){
        return lineDetails.get(lineNumber);
    }
    
    static List<LineDetails> calculateLineStarts(String text){
        List<LineDetails> lineDetails = new ArrayList<LineDetails>();
        
        int lastStart = 0;
        int charIndex = 0;
        int stringSize = text.length();
        StringBuilder lineTextBuilder = new StringBuilder();
        
        while(charIndex<stringSize){
            char charAt = text.charAt(charIndex);
            
            if (charAt == '\n'){
                String lineText = lineTextBuilder.toString();
                lineDetails.add(new LineDetails(lastStart, charIndex, lineText));
                lastStart=charIndex+1;
                lineTextBuilder = new StringBuilder();
            }else{
                lineTextBuilder.append(charAt);
            }
            
            charIndex++;
        }
        boolean trailingNewline = !text.isEmpty() && text.charAt(text.length()-1) == '\n';
        
        lineDetails.add(new LineDetails(lastStart, Math.max(0, trailingNewline?charIndex:charIndex-1), lineTextBuilder.toString()));
        
        return lineDetails;
    }

    public int convertLineCursorToAbsolute(CursorPosition cursorPosition) {
        return this.lineDetails.get(cursorPosition.line).startIndex + cursorPosition.posOnLine;
    }
    /**
     * As convertLineCursorToAbsolute but treats each new line as being zero 
     * characters (even though in the string they are a \n
     */
    public int convertLineCursorToAbsoluteWithoutNewLines(CursorPosition cursorPosition) {
        return convertLineCursorToAbsolute(cursorPosition) - cursorPosition.line;
    }
    
    
}
