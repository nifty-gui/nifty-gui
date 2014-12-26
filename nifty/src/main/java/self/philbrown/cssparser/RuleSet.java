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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A rule set consists of the selector and declaration block.
 * @author Phil Brown
 * @since 1:40:02 PM Dec 19, 2013
 *
 */
public class RuleSet
{
	/** Selector */
	private TokenSequence selector;
	/** List of Property-Value pairings */
	private List<Declaration> declarationBlock;
	
	/**
	 * Constructor
	 */
	public RuleSet()
	{
		declarationBlock = new ArrayList<Declaration>();
	}
	
	/**
	 * Constructor
	 * @param selector
	 */
	public RuleSet(TokenSequence selector)
	{
		this();
		this.selector = selector;
	}
	
	/**
	 * Constructor
	 * @param selector
	 * @param declarationBlock
	 */
	public RuleSet(TokenSequence selector, List<Declaration> declarationBlock)
	{
		this.selector = selector;
		this.declarationBlock = declarationBlock;
	}

	public TokenSequence getSelector() {
		return selector;
	}

	public void setSelector(TokenSequence selector) {
		this.selector = selector;
	}

	public List<Declaration> getDeclarationBlock() {
		return declarationBlock;
	}

	public void setDeclarationBlock(List<Declaration> declarationBlock) {
		this.declarationBlock = declarationBlock;
	}
	
	@Override
	public String toString()
	{
		StringBuilder block = new StringBuilder(" {\n");
		for (int i = 0; i < declarationBlock.size(); i++)
		{
			block.append(" ").append(declarationBlock.get(i)).append(";\n");
		}
		block.append("}");
		
		return String.format(Locale.US, "%s %s", selector.toString(), block.toString());
	}
	
	public String toDebugString()
	{
		StringBuilder block = new StringBuilder(" {\n");
		for (int i = 0; i < declarationBlock.size(); i++)
		{
			block.append(" [DECLARATION]: ").append(declarationBlock.get(i)).append(";\n");
		}
		block.append("}");
		
		return String.format(Locale.US, "[SELECTOR]: %s %s", selector.toString(), block.toString());
	}
	
}
