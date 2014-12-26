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

import java.io.InputStream;
import java.util.List;

/**
 * Interface for receiving parse events from the CSS Parser.
 * @author Phil Brown
 * @since 2:39:59 PM Dec 18, 2013
 *
 */
public interface CSSHandler 
{

	/**
	 * Handle a parse error. This is called before the parser exits.
	 * @param error the error message
	 * @param t the error. This may be {@code null}.
	 */
	public void handleError(String error, Throwable t);
	
	//at-rules
	
	/**
	 * Handle the {@literal @}import At-Rule
	 * @param importString the name of the file to input. This can be interpreted any way.
	 * @return an {@code InputStream} associated with the given name, or {@code null}.
	 */
	public InputStream handleImport(TokenSequence importSequence);
	
	/**
	 * Alerts the listener that a {@literal @}charset At-Rule has been parsed and handled by the
	 * {@link Scanner}.
	 * @param charset the charset String.
	 * FIXME: this needs further tests.
	 */
	public void handleNewCharset(String charset);
	
	/**
	 * Handle the {@literal @}namespace At-Rule.
	 * @param namespace the namespace
	 */
	public void handleNamespace(String namespace);
	
	/**
	 * Gets whether or not the logic is {@code true} for the given {@literal @}support query
	 * @param logic the supports logic to query
	 * @return {@code true} if the support block should be included (at the end of the CSS Input Stream).
	 * Otherwise {@code false}.
	 */
	public boolean supports(TokenSequence logic);
	
	/**
	 * Handle {@literal @}Keyframes At-Rule
	 * @param identifier the name of the keyframes animation
	 * @param keyframes a list of the keyframes
	 */
	public void handleKeyframes(String identifier, List<KeyFrame> keyframes);
	
	/**
	 * Handle the {@literal @}Font-Face At-Rule
	 * @param font the FontFace that has been parsed.
	 */
	public void handleFontFace(FontFace font);
	
	/**
	 * Query to see if the given media is supported. If so, the logic within will be appended
	 * to the end of the css input stream
	 * @param types a list of media types used for the media query
	 * @return true if any of the the given media is supported
	 */
	public boolean queryMedia(TokenSequence[] types);
	
	/**
	 * Handle the {@literal @}page At-Rule
	 * @param pseudoClass the pseudo-class
	 * @param properties the list of properties
	 */
	public void handlePage(TokenSequence pseudoClass, List<Declaration> properties);
	
	public boolean queryDocument(TokenSequence[] functions);
	
	/**
	 * Called once the parser has completed.
	 * @param ruleSet contains all the parsed rules
	 */
	public void handleRuleSet(RuleSet ruleSet);
	
	
}
