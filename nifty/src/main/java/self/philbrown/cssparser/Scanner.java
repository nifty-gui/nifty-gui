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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Scans the input stream for CSS syntax. Each Object that is a CSS component is recognized as a Token
 * and is handled by the {@link CSSParser parser}.
 * @author Phil Brown
 * @since 2:39:59 PM Dec 18, 2013
 */
public class Scanner implements ParserConstants 
{
	/** Default charset */
	private String charset = "UTF-8";
	/** The current character that has been read by the buffer */
	private char C;
	/** Input stream reader */
	private BufferedReader source;
	
	//note that cursor, charInLine, and line may get thrown off by some at-rules that modify the input stream
	
	/** Used to remember the current index while reading through the input source. */
	private int cursor = 0;
	/** Used to note which on character in the current line an error has occured. */
	private int charInLine = 0;
	/** Keeps track of which line is being parsed. */
	private int line = 1;
	
	/** Keeps track of imports so no redundant calls are made */
	private List<String> imports = new ArrayList<>();
	/** Keeps track of supports statements so no redundant calls are made */
	private List<String> supports = new ArrayList<>();
	
	/** The input stream */
	private InputStream is;
	
	/** character that is used as a line delimeter. this is used for counting lines are characters in lines. */
	private char lineSeparator = Character.LINE_SEPARATOR;
	
	/**
	 * Constructor
	 * @param source
	 * @throws IOException
	 */
	public Scanner(InputStream source) throws IOException
	{
		this.is = source;
		this.source = new BufferedReader(new InputStreamReader(is, charset));
		getChar();
		
	}
	
	/**
	 * Append CSS to the end of the current css input stream.
	 * @param ruleSets
	 * @throws IOException
	 */
	public void include(String ruleSets) throws IOException
	{
		if (supports.contains(ruleSets))
			return;
		supports.add(ruleSets);
		//costly.
		StringBuilder builder = new StringBuilder();
		String read = source.readLine();
		while(read != null) {
		    //System.out.println(read);
			builder.append(read);
			read = source.readLine();
		}
		source.close();
	    String currentStreamContents = builder.toString();
	    
	    this.is = new ByteArrayInputStream(String.format(Locale.US, "%s %s", currentStreamContents,ruleSets).getBytes());
	    this.source = new BufferedReader(new InputStreamReader(is, charset));
		//getChar();
	}
	
	/**
	 * Includes a stylesheet in the inputstream
	 * @param stylesheet
	 * @param name
	 * @throws IOException
	 */
	public void include(InputStream stylesheet, String name) throws IOException
	{
		
		
		if (imports.contains(name))
			return;
		
		imports.add(name);
		
		//costly.
		StringBuilder builder = new StringBuilder();
		String read = source.readLine();
		builder.append(C);//no
		while(read != null) {
		    System.out.println(read);
			builder.append(read);
			read = source.readLine();
		}
		source.close();
	    String currentStreamContents = builder.toString();
	    java.util.Scanner s = new java.util.Scanner(stylesheet).useDelimiter("\\A");
	    String newStreamContents = s.hasNext() ? s.next() : "";
	    
	    this.is = new ByteArrayInputStream(String.format(Locale.US, "%s%s", newStreamContents,currentStreamContents).getBytes());
		this.source = new BufferedReader(new InputStreamReader(is, charset));
		getChar();
		
		
	}
	
	/**
	 * changes the charset
	 * @param charset
	 * @throws IOException
	 */
	public void changeCharset(String charset) throws IOException
	{
		if (this.charset.equalsIgnoreCase(charset))
			return;
		this.charset = charset;
		//change the charset
		
		
		//costly.
		StringBuilder builder = new StringBuilder();
		String read = source.readLine();
		while(read != null) {//This messes up cursor?
			builder.append(read);
			read = source.readLine();
		}
		charInLine = 1;
		source.close();
	    String currentStreamContents = builder.toString();
	    this.source = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(currentStreamContents.getBytes()), charset));
	    cursor++;
	    charInLine = 0;
	    line++;
	}
	
	/**
	 * Read the next character from the input stream
	 * @throws IOException
	 */
	private void getChar() throws IOException
	{
		C = (char) source.read();
		cursor++;
		charInLine++;
		if (C == lineSeparator)
		{
			line++;
			charInLine = 0;
		}
	}
	
	/**
	 * Read the next token from the input stream
	 * @return
	 * @throws IOException
	 */
	public Token nextToken() throws IOException
	{
		return nextToken(true);
	}
	
	/**
	 * Read the next token from the input stream
	 * @param ignoreWhitespace {@code true} to ignore whitespace, otherwise {@code false}
	 * @return
	 * @throws IOException
	 */
	public Token nextToken(boolean ignoreWhitespace) throws IOException
	{
		String attribute = "";
		
		if (ignoreWhitespace)
		{
			//get through whitespace
			while (Character.isWhitespace(C))
			{
				getChar();
			}
		}
		
		//Identifier or reserved word
		if (Character.isLetter(C) || C == '-' || C == '_')
		{
			while (Character.isLetter(C) || C == '-' || Character.isDigit(C) || C == '_')
			{
				attribute += C;
				getChar();
			}
			return new Token(lookup(attribute), attribute);
		}
		if (Character.isDigit(C))
		{
			while (Character.isDigit(C))
			{
				attribute += C;
				getChar();
			}
			return new Token(NUMBER, attribute);
		}
		switch (C)
		{
			case '/' : {//Slash or Comment
				getChar();
				if (C == '*')
				{
					boolean inComment = true;
					while (inComment)
					{
						getChar();
						if (C == '*')
						{
							getChar();
							if (C == '/')
							{
								inComment = false;
							}
							else
							{
								attribute += '*';
								attribute += C;
							}
						}
						else
						{
							attribute += C;
						}
					}
					getChar();
					return nextToken(ignoreWhitespace);//new Token(COMMENT, attribute);
				}
				return new Token(SLASH, null);
			}
			case '.' : {
				getChar();
				return new Token(DOT, null);
			}
			case '#' : {
				getChar();
				return new Token(HASH, null);
			}
			case '*' : {
				getChar();
				if (C == '=')
				{
					getChar();
					return new Token(TIMES_EQUAL, null);
				}
				return new Token(TIMES, null);
			}
			case ',' : {
				getChar();
				return new Token(COMMA, null);
			}
			case '>' : {
				getChar();
				return new Token(GT, null);
			}
			case '+' : {
				getChar();
				return new Token(PLUS, null);
			}
			case '[' : {
				getChar();
				return new Token(LEFTSQ, attribute);
			}
			case ']' : {
				getChar();
				return new Token(RIGHTSQ, attribute);
			}
			case '{' : {
				getChar();
				return new Token(LEFT_CURLY_BRACKET, attribute);
			}
			case '}' : {
				getChar();
				return new Token(RIGHT_CURLY_BRACKET, attribute);
			}
			case ':' : {
				getChar();
				if (C == ':')
				{
					getChar();
					return new Token(DOUBLE_COLON, null);
				}
				return new Token(COLON, null);
			}
			case '=' : {
				getChar();
				return new Token(EQUAL, null);
			}
			case '(' : {
				getChar();
				return new Token(LEFTPAREN, null);
			}
			case ')' : {
				getChar();
				return new Token(RIGHTPAREN, null);
			}
			case '~' : {
				getChar();
				if (C == '=')
				{
					getChar();
					return new Token(NOT_EQUAL, null);
				}
				return new Token(NOT,null);
			}
			case '|' : {
				getChar();
				if (C == '=')
				{
					getChar();
					return new Token(OR_EQUAL, null);
				}
				return new Token(OR,null);
			}
			case '^' : {
				getChar();
				if (C == '=')
				{
					getChar();
					return new Token(CARET_EQUAL, null);
				}
				return new Token(CARET,null);
			}
			case '$' : {
				getChar();
				if (C == '=')
				{
					getChar();
					return new Token(DOLLAR_EQUAL, null);
				}
				return new Token(DOLLAR,null);
			}
			case '@' : {
				getChar();
				if (Character.isLetter(C))
				{
					while (Character.isLetter(C) || C == '-')
					{
						attribute += C;
						getChar();
					}
					return new Token(AT_RULE, attribute);
				}
				return new Token(AT, null);
			}
			case ';' : {
				getChar();
				return new Token(SEMICOLON, null);
			}
			case '%' : {
				getChar();
				return new Token(PERCENT, null);
			}
			case ' ' : {
				getChar();
				return new Token(SPACE, null);
			}
			case '"' : {
				getChar();
				return new Token(DOUBLE_QUOTE, null);
			}
			case EOFCHAR : {
				return new Token(EOF, null);
			}
			default : {
				System.out.println(String.format(Locale.US, "Unexpected Token '%s'", String.valueOf(C)));
				getChar();
				return nextToken();
			}
		}
	}
	
	/**
	 * Get the Token for reserved word or identifier
	 * @param word
	 * @return
	 */
	private int lookup(String word)
	{
		for (int i = 1; i < RESERVEDWORD.length; i++) 
		{
			if (word.equalsIgnoreCase(RESERVEDWORD[i]))
				return i;
		}
		return IDENTIFIER;
	}
	
	/**
	 * Get the current line number of the scanner
	 * @return
	 */
	public int getLineNumber()
	{
		return line;
	}
	
	/**
	 * Get the current character number of the scanner
	 * @return
	 */
	public int getCharacterNumber()
	{
		return cursor+1;
	}
	
	/**
	 * Get the current character number for the current line that is being scanned
	 * @return
	 */
	public int getCharacterNumberForCurrentLine()
	{
		return charInLine+1;
	}

	/**
	 * Get the line delimiter character
	 * @return
	 */
	public char getLineSeparator() {
		return lineSeparator;
	}
	
	/**
	 * Set the line delimiter character
	 * @param lineSeparator
	 */
	public void setLineSeparator(char lineSeparator) {
		this.lineSeparator = lineSeparator;
	}
	
}
