package de.lessvoid.nifty.controls.textarea;

import org.junit.Test;

import static de.lessvoid.nifty.controls.textarea.TextAreaControl.convertCursorPositionWithWrappingToWithout;
import static de.lessvoid.nifty.controls.textarea.TextAreaControl.convertCursorPositionWithoutWrappingToWith;
import static org.junit.Assert.*;

public class TextAreaControlTest {

    @Test
    public void convertCursorPositionWithWrappingToCursorPositionWithoutWrapping_emptyString(){
        assertEquals(0, convertCursorPositionWithWrappingToWithout(0, "", ""));
    }
    @Test
    public void convertCursorPositionWithWrappingToCursorPositionWithoutWrapping_noWrapping(){
        String text = "hi\nthere\nisGood";
        String textWithWrapping = text;

        assertEquals(0, convertCursorPositionWithWrappingToWithout(0, textWithWrapping, text));
        assertEquals(3, convertCursorPositionWithWrappingToWithout(3, textWithWrapping, text));
        assertEquals(15, convertCursorPositionWithWrappingToWithout(15, textWithWrapping, text));

    }

    @Test
    public void convertCursorPositionWithWrappingToCursorPositionWithoutWrapping_withWrapping(){
        String text = "abcdef\n\nabcdef";
        String textWithWrapping = "abc\ndef\n\nabc\ndef";

        assertEquals(0, convertCursorPositionWithWrappingToWithout(0, textWithWrapping, text));
        assertEquals(3, convertCursorPositionWithWrappingToWithout(3, textWithWrapping, text));
        assertEquals(3, convertCursorPositionWithWrappingToWithout(4, textWithWrapping, text));
        assertEquals(4, convertCursorPositionWithWrappingToWithout(5, textWithWrapping, text));

        //after the first real new lines
        assertEquals(7, convertCursorPositionWithWrappingToWithout(8, textWithWrapping, text));

        //after the two real new lines
        assertEquals(8, convertCursorPositionWithWrappingToWithout(9, textWithWrapping, text));

        //very end of text
        assertEquals(14, convertCursorPositionWithWrappingToWithout(16, textWithWrapping, text));

    }

    @Test
    public void convertCursorPositionWithoutWrappingToWith_emptyString(){
        assertEquals(0, convertCursorPositionWithoutWrappingToWith(0, "", ""));
    }
    @Test
    public void convertCursorPositionWithoutWrappingToWith_singleChar(){
        String text = "a";
        String textWithWrapping = text;
        assertEquals(0, convertCursorPositionWithoutWrappingToWith(0, textWithWrapping, text));
        assertEquals(1, convertCursorPositionWithoutWrappingToWith(1, textWithWrapping, text));
    }

    @Test
    public void convertCursorPositionWithoutWrappingToWith_wrapOnSpace(){ //when the text renderer wraps on a space it ommits the space in the output
        String text =             "hi there";
        String textWithWrapping = "hi\nthere";
        assertEquals(8, convertCursorPositionWithoutWrappingToWith(8, textWithWrapping, text));

    }


    @Test
    public void convertCursorPositionWithoutWrappingToWith_withSimpleWrapping(){
        String text = "abcdefghij";
        String textWithWrapping = "ab\ncd\nef\ngh\nij";

        assertEquals(0, convertCursorPositionWithoutWrappingToWith(0, textWithWrapping, text));
        assertEquals(1, convertCursorPositionWithoutWrappingToWith(1, textWithWrapping, text));
        assertEquals(3, convertCursorPositionWithoutWrappingToWith(2, textWithWrapping, text));
        assertEquals(4, convertCursorPositionWithoutWrappingToWith(3, textWithWrapping, text));
        assertEquals(6, convertCursorPositionWithoutWrappingToWith(4, textWithWrapping, text));
        assertEquals(7, convertCursorPositionWithoutWrappingToWith(5, textWithWrapping, text));
        assertEquals(9, convertCursorPositionWithoutWrappingToWith(6, textWithWrapping, text));
        assertEquals(10, convertCursorPositionWithoutWrappingToWith(7, textWithWrapping, text));
        assertEquals(12, convertCursorPositionWithoutWrappingToWith(8, textWithWrapping, text));
        assertEquals(13, convertCursorPositionWithoutWrappingToWith(9, textWithWrapping, text));
        assertEquals(14, convertCursorPositionWithoutWrappingToWith(10, textWithWrapping, text));
    }

    @Test
    public void convertCursorPositionWithoutWrappingToWith_withWrapping(){
        String text = "abcdef\n\nabcdef";
        String textWithWrapping = "abc\ndef\n\nabc\ndef";

        //note certain without wrappings can't be uniquely mapped to with wrappings. Where two with wrapping indexes
        //pair with one with wrapping then the one that is a end of the line wins

        assertEquals(0, convertCursorPositionWithoutWrappingToWith(0, textWithWrapping, text));

        //this is at abc| there has been a soft wrap between abc and d
        assertEquals(4, convertCursorPositionWithoutWrappingToWith(3, textWithWrapping, text));
        //this is at abcd|
        assertEquals(5, convertCursorPositionWithoutWrappingToWith(4, textWithWrapping, text));

        //this is at abcdef|
        assertEquals(7, convertCursorPositionWithoutWrappingToWith(6, textWithWrapping, text));

        //this is at abcdef\n|
        assertEquals(8, convertCursorPositionWithoutWrappingToWith(7, textWithWrapping, text));
        //this is at abcdef\n\n|
        assertEquals(9, convertCursorPositionWithoutWrappingToWith(8, textWithWrapping, text));

        //this is at the end of the string
        assertEquals(16, convertCursorPositionWithoutWrappingToWith(14, textWithWrapping, text));

    }

}
