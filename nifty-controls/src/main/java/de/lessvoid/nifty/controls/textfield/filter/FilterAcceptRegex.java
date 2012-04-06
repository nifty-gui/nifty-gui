package de.lessvoid.nifty.controls.textfield.filter;

import java.util.regex.Pattern;

import de.lessvoid.nifty.controls.textfield.SingleCharSequence;

/**
 * This filter applies a regular expression to the characters insert into the text field. Only if the characters meet
 * the regular expression, they are accepted.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class FilterAcceptRegex implements TextFieldInputFilter {
  /**
   * The regular expression pattern all inputs are compared against.
   */
  private final Pattern pattern;

  /**
   * Create a new instance of this class and set the pattern that is used for the test.
   *
   * @param expression the used pattern
   */
  public FilterAcceptRegex(final Pattern expression) {
    pattern = expression;
  }

  /**
   * Create a new instance and set the regular expression used for the test. The pattern is compiled without special
   * flags.
   *
   * @param expression the regular expression
   */
  public FilterAcceptRegex(final String expression) {
    this(expression, 0);
  }

  /**
   * Create a new instance and set the regular expression that needs to be checked.
   *
   * @param expression the regular expression
   * @param flags Match flags, a bit mask that may include {@link Pattern#CASE_INSENSITIVE}, {@link Pattern#MULTILINE},
   * {@link Pattern#DOTALL}, {@link Pattern#UNICODE_CASE}, {@link Pattern#CANON_EQ}, {@link Pattern#UNIX_LINES}, {@link
   * Pattern#LITERAL} and {@link Pattern#COMMENTS}
   */
  public FilterAcceptRegex(final String expression, final int flags) {
    this(Pattern.compile(expression, flags));
  }

  @Override
  public boolean acceptInput(final CharSequence oldText, final int index, final char newChar) {
    return pattern.matcher(new SingleCharSequence(newChar, 1)).matches();
  }

  @Override
  public boolean acceptInput(final CharSequence oldText, final int index, final CharSequence newChars) {
    return pattern.matcher(newChars).matches();
  }

  @Override
  public boolean acceptDelete(final CharSequence oldSequence, final int deleteStart, final int deleteEnd) {
    return true;
  }
}
