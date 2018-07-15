package de.lessvoid.nifty.controls.textarea;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.*;
import de.lessvoid.nifty.controls.textfield.*;
import de.lessvoid.nifty.controls.textfield.filter.delete.TextFieldDeleteFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptDigits;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptFloat;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptLetters;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptLowerCase;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptNegativeDigits;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptRegex;
import de.lessvoid.nifty.controls.textfield.filter.input.FilterAcceptUpperCase;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputCharSequenceFilter;
import de.lessvoid.nifty.controls.textfield.filter.input.TextFieldInputFilter;
import de.lessvoid.nifty.controls.textfield.format.FormatPassword;
import de.lessvoid.nifty.controls.textfield.format.TextFieldDisplayFormat;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyStandardInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class TextAreaControl extends AbstractController implements TextArea, TextFieldView {

    private int linesOfScroll = 0;
    private int currentYOffset = 0; //the current offset in pixels of the rendered text

    private static String ZERO_WIDTH_CHARACTER = "\u200E";
    private static final Logger log = Logger.getLogger(TextFieldControl.class.getName());

    protected TextFieldLogic textField;
    private FocusHandler focusHandler;
    private Screen screen;
    protected Element textElement;
    private Element fieldElement;
    private Element cursorElement;
    private Nifty nifty;

    private int fromClickCursorPos;

    private boolean needsUpdateCursor = false;

    @Override
    public void bind(Nifty nifty, Screen screen, Element elmnt, Parameters properties) {
        bind(elmnt);
        this.screen = screen;
        textElement = elmnt.findElementById("#text");
        fieldElement = elmnt.findElementById("#field");
        cursorElement = elmnt.findElementById("#cursor");
        this.nifty = nifty;

        String initText = properties.get("text");
        if ((initText == null) || initText.isEmpty()) {
            textField = new TextFieldLogic(nifty.getClipboard(), this);
            textField.toFirstPosition();
        } else {
            textField = new TextFieldLogic(initText, nifty.getClipboard(), this);
        }


        if (textElement == null) {
            throw new RuntimeException("Locating the text element of the text field failed. Looked for: #text");
        }
        if (fieldElement == null) {
            throw new RuntimeException("Locating the field element of the text field failed. Looked for: #field");
        }
        if (cursorElement == null) {
            throw new RuntimeException("Locating the cursor element of the text field failed. Looked for: #cursor");
        }

        setMaxLength(properties.getAsInteger("maxLength", UNLIMITED_LENGTH));
        activateFilter(properties.getWithDefault("filter", "all"));
    }

    /**
     * Apply a filter in regards to the filter property that was set for this text field control.
     *
     * @param filter the value of the filter property
     */
    private void activateFilter(@Nonnull final String filter) {
        if ("all".equals(filter)) { //NON-NLS
            disableInputFilter();
        } else if ("digits".equals(filter)) { //NON-NLS
            enableInputFilter(new FilterAcceptDigits());
        } else if ("negative digits".equals(filter)) { //NON-NLS
            enableInputFilter(new FilterAcceptNegativeDigits());
        } else if ("float".equals(filter)) { //NON-NLS
            enableInputFilter(new FilterAcceptFloat());
        } else if ("letters".equals(filter)) { //NON-NLS
            enableInputFilter(new FilterAcceptLetters());
        } else if ("upper case".equals(filter)) { //NON-NLS
            enableInputFilter(new FilterAcceptUpperCase());
        } else if ("lower case".equals(filter)) { //NON-NLS
            enableInputFilter(new FilterAcceptLowerCase());
        } else {
            enableInputFilter((TextFieldInputCharSequenceFilter)(new FilterAcceptRegex(filter)));
        }
    }

    @Override
    public void enableInputFilter(@Nullable final TextFieldInputCharFilter filter) {
        if (filter == null) {
            disableInputFilter();
        } else {

            textField.setInputFilterSingle(filter);
            textField.setInputFilterSequence(new InputCharFilterWrapper(filter));

        }
    }

    @Override
    public void enableInputFilter(@Nullable final TextFieldInputCharSequenceFilter filter) {
        if (filter == null) {
            disableInputFilter();
        } else {
            textField.setInputFilterSingle(new InputCharSequenceFilterWrapper(filter));
            textField.setInputFilterSequence(filter);

        }
    }

    @Override
    public void enableInputFilter(@Nullable final TextFieldInputFilter filter) {
        textField.setInputFilterSingle(filter);
        textField.setInputFilterSequence(filter);
    }

    @Override
    public void disableInputFilter() {
        textField.setInputFilterSingle(null);
        textField.setInputFilterSequence(null);
    }


    @Override
    public void init(Parameters prmtrs) {
        super.init(prmtrs);

        focusHandler = screen.getFocusHandler();

        layoutCallback();

        updateTextAndCursor();
    }

    @Override
    public void onFocus(final boolean getFocus) {
        if (cursorElement != null) {
            super.onFocus(getFocus);
            if (getFocus) {
                cursorElement.startEffect(EffectEventId.onCustom);
            } else {
                cursorElement.stopEffect(EffectEventId.onCustom);
            }
            updateTextAndCursor();
        }
    }

    @Override
    public boolean inputEvent(NiftyInputEvent inputEvent) {
        if (inputEvent instanceof NiftyStandardInputEvent) {
            final NiftyStandardInputEvent standardInputEvent = (NiftyStandardInputEvent) inputEvent;
            if (textField != null) {
                switch (standardInputEvent) {
                    case MoveCursorDown:
                        this.moveLines(1);
                        break;
                    case MoveCursorUp:
                        this.moveLines(-1);
                        break;
                    case MoveCursorLeft:
                        textField.cursorLeft();
                        break;
                    case MoveCursorRight:
                        textField.cursorRight();
                        break;
                    case Delete:
                        textField.delete();
                        break;
                    case Backspace:
                        textField.backspace();
                        break;
                    case MoveCursorToLastPosition:
                        textField.toLastPosition();
                        break;
                    case MoveCursorToFirstPosition:
                        textField.toFirstPosition();
                        break;
                    case SelectionStart:
                        textField.startSelecting();
                        break;
                    case SelectionEnd:
                        textField.endSelecting();
                        break;
                    case Cut:
                        textField.cut();
                        break;
                    case Copy:
                        textField.copy();
                        break;
                    case Paste:
                        textField.put();
                        break;
                    case SelectAll:
                        textField.selectAll();
                        break;
                    case Character:
                        textField.insert(standardInputEvent.getCharacter());
                        break;
                    case NextInputElement:
                        textField.insert("    "); //todo change to real tabs if possible
                        break;
                    case PrevInputElement:
                        if (focusHandler != null && fieldElement != null) {
                            focusHandler.getPrev(fieldElement).setFocus();
                        }
                        break;
                    case SubmitText:
                        textField.insert("\n");
                        break;
                    default:
                        updateTextAndCursor();
                        return false;
                }
            }
        }
        this.updateTextAndCursor();
        return true;

    }

    public void onClick(final int mouseX, final int mouseY) {
        fromClickCursorPos = getCursorPosFromMouse(mouseX, mouseY);
        textField.setCursorPosition(fromClickCursorPos);

        updateTextAndCursor();

    }

    public void onClickMouseMove(final int mouseX, final int mouseY) {

        if (textField != null) {
            textField.setCursorPosition(fromClickCursorPos); //the position from the last click
            textField.startSelecting();
            textField.setCursorPosition(getCursorPosFromMouse(mouseX, mouseY));
            textField.endSelecting();
        }
        updateTextAndCursor();
    }

    @Override
    public String getText() {
        return getRealText();
    }

    @Override
    public String getRealText() {
        if (textField == null) {
            return "";
        } else {
            return textField.getRealText().toString();
        }
    }

    @Override
    public void setMaxLength(final int maxLength) {
        textField.setMaxLength(maxLength);
        updateTextAndCursor();
    }

    private int getCursorPosFromMouse(final int mouseX, final int mouseY) {
        TextRenderer renderer = textElement.getRenderer(TextRenderer.class);
        String text = renderer.getWrappedText();

        int mouseY_withinElement = mouseY - fieldElement.getY()- currentYOffset;
        int mouseX_withinElement = mouseX - fieldElement.getX();
        int row = mouseY_withinElement / renderer.getFont().getHeight();

        final SplitLinesDetails splitLineDetails = new SplitLinesDetails(text);

        if(row>=splitLineDetails.getNumberOfLines()){ //clamp to actually available lines
            row = splitLineDetails.getNumberOfLines()-1;
        }

        LineDetails lineDetails = splitLineDetails.getLineDetail(row);
        String textOnLine = lineDetails.realTextOnLine;
        //find the character position that best matches the mouse click
        int bestIndex = 0;
        int error = Integer.MAX_VALUE;
        for(int i=0;i<=textOnLine.length();i++){
            int newError = Math.abs(renderer.getFont().getWidth(textOnLine.substring(0, i)) - mouseX_withinElement);
            if(newError<error){
                error = newError;
                bestIndex = i;
            }else{
                break; //we'd expect the error to get smaller and smaller then larger as we go past the optimum
            }
        }

        CursorPosition cursorPosition = new CursorPosition(row, bestIndex);
        return convertCursorPositionWithWrappingToWithout(splitLineDetails.convertLineCursorToAbsolute(cursorPosition));
    }

    /**
     * Wrapping adds bonus new lines not in the real string, this converts indexes including those new lines to an
     * index not including them
     */
    private int convertCursorPositionWithWrappingToWithout(int cursorPositionWithWrapping){
        return convertCursorPositionWithWrappingToWithout(cursorPositionWithWrapping, textElement.getRenderer(TextRenderer.class).getWrappedText(), (String)textField.getDisplayedText() );
    }

    protected static int convertCursorPositionWithWrappingToWithout(int cursorPositionWithWrapping, String textWithWrapping, String textWithoutWrapping){
        if(cursorPositionWithWrapping == textWithWrapping.length()){
            return textWithoutWrapping.length();
        }

        textWithWrapping = textWithWrapping+"/n";
        String wrappedTextBeforeCursor = textWithWrapping.substring(0, cursorPositionWithWrapping);
        List<String> wrappedLines = new ArrayList<String>(Arrays.asList(wrappedTextBeforeCursor.split("\n", -1))); //split on new lines, some may be real, some wrapped

        boolean cursorStraightAfterNewLine = wrappedTextBeforeCursor.endsWith("\n");

        //if the very list line is "" then remove it as the new line is already considered at the end of the line before

        if(wrappedLines.get(wrappedLines.size()-1).equals("")){
            wrappedLines.remove(wrappedLines.size()-1);
        }

        //use each wrapped line to track forward in the real text. If a wrappedLines line end alignes with a real line end add a bonus 1
        int absoluteCursorPosition = 0;
        for(String wrappedLine: wrappedLines){
            absoluteCursorPosition+=wrappedLine.length();

            boolean lastLine = wrappedLine.equals(wrappedLines.get(wrappedLines.size()-1));

            if(textWithoutWrapping.length()>absoluteCursorPosition+1){
                char charWithoutWrapping = textWithoutWrapping.charAt(absoluteCursorPosition);
                boolean realNewLine = charWithoutWrapping == '\n';
                boolean wrappedOnSpace =charWithoutWrapping == ' ';

                if(realNewLine || wrappedOnSpace) {
                    if(!lastLine || cursorStraightAfterNewLine) {
                        absoluteCursorPosition++;
                    }
                }
            }
        }

        return absoluteCursorPosition;
    }

    private int convertCursorPositionWithoutWrappingToWith(int cursorPositionWithoutWrapping){
        return convertCursorPositionWithoutWrappingToWith(cursorPositionWithoutWrapping, textElement.getRenderer(TextRenderer.class).getWrappedText(), (String)textField.getDisplayedText() );
    }

    protected static int convertCursorPositionWithoutWrappingToWith(int cursorPositionWithoutWrapping, String textWithWrapping, String textWithoutWrapping){
        //track forward, adding an extra index every time we encounter a new line not in the without wrapping string

        List<Character> wrapEquivalentChars = new ArrayList<Character>(); //characters in the original string which when appearing alongside a new line do not offer a bonus new line
        wrapEquivalentChars.add('\n');
        wrapEquivalentChars.add(' ');

        int extraNewLines = 0;

        for(int i=0;i<cursorPositionWithoutWrapping; i++){
            
            if( !wrapEquivalentChars.contains(textWithoutWrapping.charAt(i)) && textWithWrapping.charAt(i+extraNewLines) =='\n' ){
                extraNewLines++;
            }
        }

        //because of soft wraps the end of one line and the beginning of the next are the same cursorPosition, we choose
        //the beginning of the next line as that feels more normal
        if(cursorPositionWithoutWrapping+1<textWithoutWrapping.length()){
            char nextCharInWrapped = textWithWrapping.charAt(cursorPositionWithoutWrapping+extraNewLines);
            char nextCharInNonWrapped = textWithoutWrapping.charAt(cursorPositionWithoutWrapping);

            if(nextCharInWrapped=='\n' && nextCharInNonWrapped!='\n'){
                extraNewLines++;
            }
        }

        return cursorPositionWithoutWrapping + extraNewLines;
    }

    private void updateTextAndCursor() {
        if (cursorElement == null || textElement == null || textField == null) {
            return;
        }

        final TextRenderer textRenderer = textElement.getRenderer(TextRenderer.class);
        textRenderer.setText(textField.getDisplayedText().toString());

        final String textWrapped = textRenderer.getWrappedText();

        textRenderer.setSelection(textField.getSelectionStart(), textField.getSelectionEnd());

        final Element element = getElement();
        if (element == null) {
            throw new NullPointerException("Due to a binding problem the element couldn't be found so the cursor cannot be updated");
        }

        element.getParent().layoutElements(); //need an early layout elements here so the text wrapping is correct

        final int cursorPosAfterWrap = this.convertCursorPositionWithoutWrappingToWith(textField.getCursorPosition());

        final SplitLinesDetails splitLineDetails = new SplitLinesDetails(textWrapped);
        final CursorPosition cursorPosOnLine = splitLineDetails.convertAbsoluteCursorPositionToLineAware(cursorPosAfterWrap);


        String textOnLine_beforeCursor = splitLineDetails.getLineDetail(cursorPosOnLine.line).realTextOnLine.substring(0, cursorPosOnLine.posOnLine);
        final int textWidth = textRenderer.getFont().getWidth(textOnLine_beforeCursor);
        final int lineHeight = textRenderer.getFont().getHeight();

        final int maximumRenderableLines = this.getElement().getHeight()/lineHeight;

        //adjust the scroll so the cursor is onscreen
        if(maximumRenderableLines > 1) { //unfortunately on the first render maximumRenderableLines is zero which would kick the scroll to 1 automatically
            linesOfScroll = clamp(linesOfScroll, cursorPosOnLine.line - maximumRenderableLines + 1, cursorPosOnLine.line);
        }else{
            linesOfScroll = cursorPosOnLine.line;
        }
        currentYOffset = -linesOfScroll*lineHeight;

        textRenderer.setyOffset(currentYOffset);

        cursorElement.setConstraintX(SizeValue.px(textWidth));
        //cursorElement.setConstraintY(SizeValue.px((getElement().getHeight() - cursorElement.getHeight()) / 2));
        cursorElement.setConstraintY(SizeValue.px((lineHeight- cursorElement.getHeight())/2 + lineHeight * cursorPosOnLine.line + currentYOffset)); //high numbers are at bottom
        cursorElement.startEffect(EffectEventId.onActive, null);

        element.getParent().layoutElements();
    }

    /**
     * Moves up (negative) or down (positive) the lines
     * @param numberOfLines
     */
    protected void moveLines(int numberOfLines){
        TextRenderer renderer = textElement.getRenderer(TextRenderer.class);
        String text = renderer.getWrappedText();

        SplitLinesDetails splitLineDetails = new SplitLinesDetails(text);
        CursorPosition cursorPosOnLine = splitLineDetails.convertAbsoluteCursorPositionToLineAware(convertCursorPositionWithoutWrappingToWith(textField.getCursorPosition()));

        int proposedLine = clamp(cursorPosOnLine.line + numberOfLines, 0, splitLineDetails.getNumberOfLines()-1);

        LineDetails detailsOftargetLine = splitLineDetails.getLineDetail(proposedLine);

        int positionOnNewLine = clamp(cursorPosOnLine.posOnLine, 0, detailsOftargetLine.numberOfRealChars);

        int newAbsolutePositionIncludingWraps = detailsOftargetLine.startIndex + positionOnNewLine;

        textField.setCursorPosition(convertCursorPositionWithWrappingToWithout(newAbsolutePositionIncludingWraps));
    }

    @Override
    public void onStartScreen() {}


    @Override
    public void disableDeleteFilter() {
        textField.setDeleteFilter(null);
    }



    @Override
    public void textChangeEvent(String newText) {
        if (nifty == null) {
            log.warning("Binding not done yet. Can't publish events without reference to Nifty.");
        } else {
            final Element element = getElement();
            if (element != null) {
                String id = getElement().getId();
                if (id != null) {
                    nifty.publishEvent(id, new TextAreaChangedEvent(this, newText));
                }
            }
        }
    }

    @Override
    public void enableDeleteFilter(TextFieldDeleteFilter filter) {
        textField.setDeleteFilter(filter);
    }

    @Override
    public String getDisplayedText() {
        return textField.getDisplayedText().toString();
    }

    @Override
    public void setCursorPosition(int position) {
        textField.setCursorPosition(position);
        updateTextAndCursor();
    }

    @Override
    public void setFormat(TextFieldDisplayFormat format) {
        textField.setFormat(format);
    }

    @Override
    public void setText(CharSequence text) {
        final CharSequence realText;
        if (nifty == null) {
            log.warning("Nifty instance is not set, binding did not run yet. Special value replacing skipped.");
            realText = text;
        } else {
            realText = nifty.specialValuesReplace(text.toString());
        }

        textField.setText(realText);

        updateTextAndCursor();
    }

    private int clamp(int value, int min, int max){
        return Math.max(min, Math.min(max, value));
    }


}
