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
 * Simple CSS Stream Parser Handler that prints what it finds to the console.
 * @author Phil Brown
 * @since 2:39:59 PM Dec 18, 2013
 */
public class DefaultCSSHandler implements CSSHandler
{

	@Override
	public void handleError(String error, Throwable t) {
		System.err.println(error);
		t.printStackTrace(System.out);
	}

	@Override
	public InputStream handleImport(TokenSequence importString) {
		System.out.println("Found @import " + importString.toString());
		return null;
	}
	
	@Override
	public void handleNewCharset(String charset) {
		System.out.println("New Charset: " + charset);
	}

	@Override
	public void handleNamespace(String namespace) {
		System.out.println("Found Namespace " + namespace);
	}

	@Override
	public boolean supports(TokenSequence logic) {
		System.out.println("Skipping support check for logic: " + logic.toString());
		return false;
	}

	@Override
	public void handleKeyframes(String identifier, List<KeyFrame> keyframes) {
		StringBuilder builder = new StringBuilder(" {\n");
		for (int i = 0; i < keyframes.size(); i++)
		{
			builder.append("  ").append(keyframes.get(i)).append(";\n");
		}
		builder.append("}");
		System.out.println(" Found @keyframes " + identifier + builder.toString());
	}

	@Override
	public void handleFontFace(FontFace font) {
		System.out.println("Found New Font: " + font.toString());
	}

	@Override
	public void handleRuleSet(RuleSet ruleSet) {
		System.out.println("\n" + ruleSet.toString() + "\n");
	}

	@Override
	public boolean queryMedia(TokenSequence[] types) {
		StringBuilder builder = new StringBuilder("Skipping media query for media types: [ ");
		for (int i = 0; i < types.length; i++) 
		{
			builder.append(types[i].toString());
			if (i != types.length-1)
				builder.append(", ");
			
		}
		builder.append(" ]");
		System.out.println(builder.toString());
		return false;
	}

	@Override
	public void handlePage(TokenSequence pseudoClass, List<Declaration> properties) {
		StringBuilder builder = new StringBuilder(" {\n");
		for (int i = 0; i < properties.size(); i++)
		{
			builder.append("  ").append(properties.get(i)).append(";\n");
		}
		builder.append("}");
		System.out.println(" Found @page " + pseudoClass.toString() + builder.toString());
	}

	@Override
	public boolean queryDocument(TokenSequence[] functions) {
		StringBuilder builder = new StringBuilder("Skipping document query for document functions: [ ");
		for (int i = 0; i < functions.length; i++) 
		{
			builder.append(functions[i].toString());
			if (i != functions.length-1)
				builder.append(", ");
			
		}
		builder.append(" ]");
		System.out.println(builder.toString());
		return false;
	}

}
