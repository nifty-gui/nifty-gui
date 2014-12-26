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

import java.util.Locale;

/**
 * Includes a property and value. May be marked as important.
 * @author Phil Brown
 * @since 1:43:16 PM Dec 19, 2013
 *
 */
public class Declaration 
{
	/** The property of this declaration. This is the sequence to the left of the {@link ParserConstants#COLON}. */
	private TokenSequence property;
	/** The value of this declaration. This is the sequence to the right of the {@link ParserConstants#COLON}. */
	private TokenSequence value;
	/** 
	 * Denotes that this declaration is important. This is {@code false} by default, but is set to 
	 * {@code true} if the <em>important</em> flag is set on a declaration using the {@code !important}
	 * syntax. 
	 */
	private boolean important;
	
	/**
	 * Default constructor
	 */
	public Declaration()
	{
	}
	
	/**
	 * Constructor
	 * @param property The property of this declaration. This is the sequence to the left of the {@link ParserConstants#COLON}.
	 * @param value The value of this declaration. This is the sequence to the right of the {@link ParserConstants#COLON}.
	 * @param important whether or not this declaration is flagged as important, for example via {@code !important}.
	 */
	public Declaration(TokenSequence property, TokenSequence value, boolean important)
	{
		this.property = property;
		this.value = value;
		this.important = important;
	}

	/**
	 * Get the Property of this Declaration
	 * @return The property of this declaration. This is the sequence to the left of the {@link ParserConstants#COLON}.
	 */
	public TokenSequence getProperty() {
		return property;
	}

	/**
	 * Set the Property of this Declaration
	 * @param property The property of this declaration. This is the sequence to the left of the {@link ParserConstants#COLON}.
	 */
	public void setProperty(TokenSequence property) {
		this.property = property;
	}

	/**
	 * Get the Value of this Declaration
	 * @return The value of this declaration. This is the sequence to the right of the {@link ParserConstants#COLON}.
	 */
	public TokenSequence getValue() {
		return value;
	}

	/**
	 * Set the Value of this Declaration
	 * @param value The value of this declaration. This is the sequence to the right of the {@link ParserConstants#COLON}.
	 */
	public void setValue(TokenSequence value) {
		this.value = value;
	}

	/**
	 * Get whether or not this Declaration has been flagged as important
	 * @return {@code true} if this Declaration is important, otherwise {@code false} (default).
	 */
	public boolean isImportant() {
		return important;
	}

	/**
	 * Set whether or not this Declaration has been flagged as important
	 * @param important
	 */
	public void setImportant(boolean important) {
		this.important = important;
	}
	
	@Override
	public String toString()
	{
		return String.format(Locale.US, "%s:%s%s", property.toString(), value.toString(), (important ? " !important" : ""));
	}
	
}
