/*
 * Copyright 2013 Phil Brown
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package self.philbrown.cssparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <h1>Main Parser</h1> 
 * This class is used to parse CSS Tokens from an Input Stream.
 * @author Phil Brown
 * @since 2:39:59 PM Dec 18, 2013
 */
public class CSSParser implements ParserConstants
{
	/** Used to parse the next {@link Token}. */
	private Scanner s;
	/** The current {@code Token}. */
	private Token t;
	/** The previous {@code Token}. */
	private Token previous = new Token(NULL);
	
	/** Used to respond to stream parsing events. */
	private CSSHandler handler;
	
	/** Used for debugging, to print the correct number of indents in order to keep output pretty. */
	private int indent = 0;
	
	/** debug flag */
	private boolean debug = false;
	
	/**
	 * Constructor
	 * @param source the input stream
	 * @param handler the handler that listens for stream parsing events
	 * @throws IOException if the parser does not recognize the syntax, or the CSS file is not valid.
	 */
	public CSSParser(InputStream source, CSSHandler handler) throws IOException
	{
		s = new Scanner(source);
		this.handler = handler;
	}
	
	/**
	 * Asserts that the next token is the token expected. If the assertion is false, an exception is created
	 * @param matcher the token code to match against what the current token code actually is
	 * @throws IOException if the given {@code matcher} is not the token code of the current {@link Token}.
	 * @see ParserConstants
	 */
	private void match(int matcher) throws IOException
	{
		if (t.tokenCode == matcher)
		{
			if (debug)
			{
				for (int i = 0; i < indent; i++) {
					System.out.print(" ");
				}
				System.out.print(t.toDebugString());
			}
			previous = t;
			t = s.nextToken();
		}
		else {
			handler.handleError(String.format(Locale.US, "Match Error at line %d and character %d. Need: %s Found: %s. Previous Token is %s.", s.getLineNumber(), s.getCharacterNumberForCurrentLine(), new Token(matcher, null).toDebugString(), t.toString(), previous.toDebugString()), new Exception("Matcher Error"));
			System.exit(0);
		}
	}
	
	/**
	 * Parse the CSS Input Stream
	 * @throws IOException if the given {@code matcher} is not the token code of the current {@link Token}.
	 */
	public void parse() throws IOException
	{
		t = s.nextToken();
		if (debug)
			System.out.println("[" + t.toDebugString() + "]");
		
		while (t.tokenCode != EOF)
		{
			if (t.tokenCode == COMMENT)
			{
				match(COMMENT);
			}
			else if (t.tokenCode == AT_RULE)
			{
				String identifier = t.attribute;
				if (identifier.equalsIgnoreCase("charset"))
				{
					match(AT_RULE);
					String charset = t.attribute;
					match(IDENTIFIER);
					match(SEMICOLON);
					s.changeCharset(charset);
					//t = s.nextToken();
					handler.handleNewCharset(charset);
				}
				else if (identifier.equalsIgnoreCase("import"))
				{
					match(AT_RULE);
					TokenSequence.Builder importSequence = new TokenSequence.Builder();
					while (t.tokenCode != SEMICOLON)
					{
						importSequence.append(t);
						t = s.nextToken();
					}
					String name = importSequence.toString();
					InputStream is = handler.handleImport(importSequence.create());
					if (is != null)
					{
						s.include(is, name);
					}
					match(SEMICOLON);
				}
				else if (identifier.equalsIgnoreCase("namespace"))
				{
					match(AT_RULE);
					StringBuilder namespace = new StringBuilder();
					while (t.tokenCode != SEMICOLON)
					{
						namespace.append(t.toString());
						t = s.nextToken();
					}
					handler.handleNamespace(namespace.toString());
					//this does not do anything in scanner yet (what would it do?)
				}
				else if (identifier.equalsIgnoreCase("supports"))
				{
					match(AT_RULE);
					TokenSequence.Builder logic = new TokenSequence.Builder();
					while (t.tokenCode != LEFT_CURLY_BRACKET)
					{
						logic.append(t);
						t = s.nextToken();
					}
					match(LEFT_CURLY_BRACKET);
					boolean supports = handler.supports(logic.create());
					if (supports)
					{
						StringBuilder newLogic = new StringBuilder();
						int curlies = 1;
						while (curlies > 0)
						{
							if (t.tokenCode == LEFT_CURLY_BRACKET)
							{
								curlies++;
							}
							else if (t.tokenCode == RIGHT_CURLY_BRACKET)
							{
								curlies--;
							}
							
							if (t.tokenCode == AT_RULE)
							{
								newLogic.append(t.toString()).append(" ");
							}
							else
							{
								newLogic.append(t.toString());
							}
							
							t = s.nextToken();
						}
						s.include(newLogic.toString());//include the supports css. 
						//Note that this appends the support logic to the end of the stylesheet
					}
					else
					{
						int curlies = 1;
						while (curlies > 0)
						{
							if (t.tokenCode == LEFT_CURLY_BRACKET)
							{
								curlies++;
							}
							else if (t.tokenCode == RIGHT_CURLY_BRACKET)
							{
								curlies--;
							}
							t = s.nextToken();
						}
					}

				}
				else if (identifier.equalsIgnoreCase("keyframes"))
				{
					match(AT_RULE);
					String ident = t.attribute;
					match(IDENTIFIER);
					match(LEFT_CURLY_BRACKET);
					int curlies = 1;
					List<KeyFrame> keyFrames = new ArrayList<KeyFrame>();
					while (curlies > 0)
					{
						if (t.tokenCode == LEFT_CURLY_BRACKET)
						{
							curlies++;
							t = s.nextToken();
						}
						else if (t.tokenCode == RIGHT_CURLY_BRACKET)
						{
							curlies--;
							t = s.nextToken();
						}
						else
						{
							double percent = -1;
							if (t.tokenCode == NUMBER)
							{
								percent = Double.parseDouble(t.attribute);
								match(NUMBER);
								match(PERCENT);
								
							}
							else
							{
								String _percent = t.attribute;
								match(IDENTIFIER);
								
								if (_percent.equalsIgnoreCase("to"))
									percent = 0;
								else if (_percent.equalsIgnoreCase("from"))
									percent = 100;
								else
									System.out.println(String.format(Locale.US, "Not a valid percentage: '%s'.", _percent));
							}
							match(LEFT_CURLY_BRACKET);
							
							List<Declaration> declarations = new ArrayList<Declaration>();
							while (t.tokenCode != RIGHT_CURLY_BRACKET)
							{
								TokenSequence.Builder property = new TokenSequence.Builder();
								property.append(t);
								match(IDENTIFIER);
								match(COLON);
								TokenSequence.Builder value = new TokenSequence.Builder();
								while (t.tokenCode != RIGHT_CURLY_BRACKET && t.tokenCode != SEMICOLON)
								{
									value.append(t);
									t = s.nextToken(false);
								}
								declarations.add(new Declaration(property.create(), value.create(), false));
								t = s.nextToken();
							}
							if (percent != -1)
							{
								keyFrames.add(new KeyFrame(percent, declarations));
							}
							
							t = s.nextToken();
						}
						
					}
					handler.handleKeyframes(ident, keyFrames);
				}
				else if (identifier.equals("font-face"))
				{
					//get Typeface from handler
					match(AT_RULE);
					match(LEFT_CURLY_BRACKET);
					List<Declaration> declarations = new ArrayList<Declaration>();
					while(t.tokenCode != RIGHT_CURLY_BRACKET)
					{
						TokenSequence.Builder property = new TokenSequence.Builder();
						property.append(t);
						
						match(IDENTIFIER);
						match(COLON);
						TokenSequence.Builder value = new TokenSequence.Builder();
						while (t.tokenCode != RIGHT_CURLY_BRACKET && t.tokenCode != SEMICOLON)
						{
							value.append(t);
							t = s.nextToken(false);
						}
						declarations.add(new Declaration(property.create(), value.create(), false));
						t = s.nextToken();
					}
					match (RIGHT_CURLY_BRACKET);
					try {
						FontFace f = new FontFace(declarations, s);
						handler.handleFontFace(f);
					}
					catch (Throwable t)
					{
						handler.handleError("Could not parse @font-face", t);
						System.exit(0);
					}
					
				}
				else if (identifier.equalsIgnoreCase("media"))
				{
					match(AT_RULE);
					TokenSequence.Builder mediaTypes = new TokenSequence.Builder();
					while (t.tokenCode != LEFT_CURLY_BRACKET)
					{
						mediaTypes.append(t);
						t = s.nextToken();
					}
					match(LEFT_CURLY_BRACKET);
					boolean supportsMedia = handler.queryMedia(mediaTypes.create().split(new Token(COMMA)));
					if (supportsMedia)
					{
						StringBuilder newLogic = new StringBuilder();
						int curlies = 1;
						while (curlies > 0)
						{
							if (t.tokenCode == LEFT_CURLY_BRACKET)
							{
								curlies++;
							}
							else if (t.tokenCode == RIGHT_CURLY_BRACKET)
							{
								curlies--;
							}
							
							if (t.tokenCode == AT_RULE)
							{
								newLogic.append(t.toString()).append(" ");
							}
							else
							{
								newLogic.append(t.toString());
							}
							
							t = s.nextToken();
						}
						s.include(newLogic.toString());//include the supports css. 
						//Note that this appends the support logic to the end of the stylesheet
					}
					else
					{
						int curlies = 1;
						while (curlies > 0)
						{
							if (t.tokenCode == LEFT_CURLY_BRACKET)
							{
								curlies++;
							}
							else if (t.tokenCode == RIGHT_CURLY_BRACKET)
							{
								curlies--;
							}
							t = s.nextToken();
						}
					}
					
				}
				else if (identifier.equalsIgnoreCase("page"))
				{
					match(AT_RULE);
					TokenSequence.Builder pseudoClass = new TokenSequence.Builder();
					while (t.tokenCode != LEFT_CURLY_BRACKET)
					{
						pseudoClass.append(t);
						t = s.nextToken();
					}
					match(LEFT_CURLY_BRACKET);
					
					List<Declaration> declarations = new ArrayList<Declaration>();
					while (t.tokenCode != RIGHT_CURLY_BRACKET)
					{
						TokenSequence.Builder property = new TokenSequence.Builder();
						property.append(t);
						match(IDENTIFIER);
						match(COLON);
						TokenSequence.Builder value = new TokenSequence.Builder();
						while (t.tokenCode != RIGHT_CURLY_BRACKET && t.tokenCode != SEMICOLON)
						{
							value.append(t);
							t = s.nextToken(false);
						}
						declarations.add(new Declaration(property.create(), value.create(), false));
						t = s.nextToken();
					}
					
					handler.handlePage(pseudoClass.create(), declarations);

				}
				else if (identifier.equalsIgnoreCase("document"))
				{
					match(AT_RULE);
					TokenSequence.Builder importSequence = new TokenSequence.Builder();
					while (t.tokenCode != LEFT_CURLY_BRACKET)
					{
						importSequence.append(t);
						t = s.nextToken();
					}
					match(LEFT_CURLY_BRACKET);
					boolean include = handler.queryDocument(importSequence.create().split(new Token(COMMA)));
					if (include)
					{
						StringBuilder newLogic = new StringBuilder();
						int curlies = 1;
						while (curlies > 0)
						{
							if (t.tokenCode == LEFT_CURLY_BRACKET)
							{
								curlies++;
							}
							else if (t.tokenCode == RIGHT_CURLY_BRACKET)
							{
								curlies--;
							}
							
							if (t.tokenCode == AT_RULE)
							{
								newLogic.append(t.toString()).append(" ");
							}
							else
							{
								newLogic.append(t.toString());
							}
							
							t = s.nextToken();
						}
						s.include(newLogic.toString());//include the supports css. 
						//Note that this appends the support logic to the end of the stylesheet
					}
					else
					{
						int curlies = 1;
						while (curlies > 0)
						{
							if (t.tokenCode == LEFT_CURLY_BRACKET)
							{
								curlies++;
							}
							else if (t.tokenCode == RIGHT_CURLY_BRACKET)
							{
								curlies--;
							}
							t = s.nextToken();
						}
					}
					
				}
//----DEVELOPERS CAN ADD SUPPORT FOR ADDITIONAL AT-RULES HERE!
				else
				{
					
					handler.handleError(String.format(Locale.US, "This implementation does not support the at-rule %s.", t.toString()), new Exception("At-Rule not supported"));
					System.exit(0);
				}
			}
			else
			{
				//expect a selector
				TokenSequence.Builder selector = new TokenSequence.Builder();
				while (t.tokenCode != LEFT_CURLY_BRACKET && t.tokenCode != EOF)
				{
					selector.append(t);
					t = s.nextToken();
				}
				if (t.tokenCode == EOF)
				{
					match(EOF);
					return;
				}
				match(LEFT_CURLY_BRACKET);
				List<Declaration> declarations = new ArrayList<Declaration>();
				while(t.tokenCode != RIGHT_CURLY_BRACKET)
				{
					TokenSequence.Builder property = new TokenSequence.Builder();
					property.append(t);
					
					match(IDENTIFIER);
					match(COLON);
					TokenSequence.Builder value = new TokenSequence.Builder();
					while (t.tokenCode != RIGHT_CURLY_BRACKET && t.tokenCode != SEMICOLON)
					{
						value.append(t);
						t = s.nextToken(false);
					}
					declarations.add(new Declaration(property.create(), value.create(), false));
					if (t.tokenCode == SEMICOLON)
						match(SEMICOLON);
				}
				match (RIGHT_CURLY_BRACKET);
				handler.handleRuleSet(new RuleSet(selector.create(), declarations));
			}
		}
		match(EOF);
		
	}
	
	/**
	 * Get the character used as line separator by the scanner
	 * @return the character used by the scanner for counting lines
	 */
	public char getLineSeparator() {
		return s.getLineSeparator();
	}

	/**
	 * Set the character used by the scanner to denote a new line
	 * @param lineSeparator the character that the scanner should recognize as a new line
	 */
	public void setLineSeparator(char lineSeparator) {
		s.setLineSeparator(lineSeparator);
	}
	
	/**
	 * Parses and prints debug info
	 * @throws IOException 
	 */
	public void debug() throws IOException
	{
		debug = true;
		parse();
	}
	
}
