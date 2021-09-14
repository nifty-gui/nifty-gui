package de.lessvoid.nifty.controls.textarea;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;


public class SplitLinesDetailsTest {
    @Test
    public void calculateLineStarts_empty() {

        String rawString = "";
        List<LineDetails> lineStarts = SplitLinesDetails.calculateLineStarts(rawString);

        assertEquals(1, lineStarts.size());
        assertEquals(new LineDetails(0,0,""), lineStarts.get(0));

    }
    @Test
    public void calculateLineStarts_singleLine() {

        String rawString = "hey";
        List<LineDetails> lineStarts = SplitLinesDetails.calculateLineStarts(rawString);

        assertEquals(1, lineStarts.size());
        assertEquals(new LineDetails(0,2, "hey"),lineStarts.get(0));

    }
    @Test
    public void calculateLineStarts_twoLines() {

        String rawString = "hello\nWorld";
        List<LineDetails> lineStarts = SplitLinesDetails.calculateLineStarts(rawString);

        assertEquals(2, lineStarts.size());
        assertEquals(new LineDetails(0,5, "hello"), lineStarts.get(0));
        assertEquals(new LineDetails(6,10, "World"), lineStarts.get(1));
    }

    @Test
    public void calculateLineStarts_trailingNewLine() {

        String rawString = "hello\n";
        List<LineDetails> lineStarts = SplitLinesDetails.calculateLineStarts(rawString);

        assertEquals(2, lineStarts.size());
        assertEquals(new LineDetails(0,5,"hello"), lineStarts.get(0));
        assertEquals(new LineDetails(6,6,""), lineStarts.get(1));
    }

    @Test
    public void calculateLineStarts_multipleRrailingNewLine() {

        String rawString = "hello\n\n\n";
        List<LineDetails> lineStarts = SplitLinesDetails.calculateLineStarts(rawString);

        assertEquals(4, lineStarts.size());
        assertEquals(new LineDetails(0,5,"hello"), lineStarts.get(0));
        assertEquals(new LineDetails(6,6,""), lineStarts.get(1));
        assertEquals(new LineDetails(7,7,""), lineStarts.get(2));
        assertEquals(new LineDetails(8,8,""), lineStarts.get(3));
    }
    @Test
    public void calculateLineStarts_multipleNewLines() {

        String rawString = "hello\n"
                + "\n"
                + "World";
        List<LineDetails> lineStarts = SplitLinesDetails.calculateLineStarts(rawString);

        assertEquals(3, lineStarts.size());
        assertEquals(new LineDetails(0,5, "hello"), lineStarts.get(0));
        assertEquals(new LineDetails(6,6, ""), lineStarts.get(1));
        assertEquals(new LineDetails(7,11, "World"), lineStarts.get(2));
    }

    @Test
    public void convertAbsoluteCursorPositionToLineAware_withBlankLine(){
        String rawString = "hello\n"
                + "\n"
                + "World";

        SplitLinesDetails splitLine = new SplitLinesDetails(rawString);

        assertEquals(new CursorPosition(0,0), splitLine.convertAbsoluteCursorPositionToLineAware(0));
        assertEquals(new CursorPosition(0,5), splitLine.convertAbsoluteCursorPositionToLineAware(5));
        assertEquals(new CursorPosition(1,0), splitLine.convertAbsoluteCursorPositionToLineAware(6));
        assertEquals(new CursorPosition(2,0), splitLine.convertAbsoluteCursorPositionToLineAware(7));
        assertEquals(new CursorPosition(2,1), splitLine.convertAbsoluteCursorPositionToLineAware(8));
        assertEquals(new CursorPosition(2,5), splitLine.convertAbsoluteCursorPositionToLineAware(12));

    }

    @Test
    public void convertAbsoluteCursorPositionToLineAware_withtrailingNewline(){
        String rawString = "hi\n"
                + "\n";

        SplitLinesDetails splitLine = new SplitLinesDetails(rawString);

        assertEquals(new CursorPosition(0,0), splitLine.convertAbsoluteCursorPositionToLineAware(0));
        assertEquals(new CursorPosition(0,1), splitLine.convertAbsoluteCursorPositionToLineAware(1));
        assertEquals(new CursorPosition(0,2), splitLine.convertAbsoluteCursorPositionToLineAware(2));
        assertEquals(new CursorPosition(1,0), splitLine.convertAbsoluteCursorPositionToLineAware(3));

    }
    @Test
    public void convertAbsoluteCursorPositionToLineAware_emptyString(){
        String rawString = "";

        SplitLinesDetails splitLine = new SplitLinesDetails(rawString);

        assertEquals(new CursorPosition(0,0), splitLine.convertAbsoluteCursorPositionToLineAware(0));

    }

}