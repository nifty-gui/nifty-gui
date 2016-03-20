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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Simple organization of a sequence of tokens.
 * @author Phil Brown
 * @since 1:35:08 PM Dec 19, 2013
 *
 */
public class TokenSequence implements Iterable<Token>
{
	/** The tokens in the sequence */
	private List<Token> tokens;
	/** The sequence as a string */
	private String string;
	
	/**
	 * Constructor
	 * @param tokens
	 * @param string
	 */
	public TokenSequence(List<Token> tokens, String string)
	{
		this.tokens = tokens;
		this.string = string;
	}
	
	/**
	 * Constructor
	 * @param token
	 * @param string
	 */
	public TokenSequence(Token token, String string)
	{
		this.tokens = new ArrayList<>();
		this.tokens.add(token);
		this.string = string;
	}
	
	/**
	 * Create a TokenSequence from a String
	 * @param s
	 * @return
	 * @throws IOException
	 */
	public static TokenSequence parse(String s) throws IOException
	{
		Scanner scanner = new Scanner(new ByteArrayInputStream(s.getBytes()));
		Builder builder = new Builder();
		Token t = scanner.nextToken();
		while (t.tokenCode != ParserConstants.EOF)
		{
			builder.append(t);
			t = scanner.nextToken();
		}
		return builder.create();
	}
	
	/**
	 * Gets the length of the TokenSequence
	 * @return
	 */
	public int length()
	{
		return tokens.size();
	}
	
	/**
	 * Gets the tokens in the sequence
	 * @return
	 */
	public List<Token> getTokens()
	{
		return tokens;
	}
	
	/**
	 * Get the tokens as an enumeration
	 * @return
	 */
	public Enumeration<Token> enumerate()
	{
		Vector<Token> vector = new Vector<>();
		for (int i = 0; i < tokens.size(); i++)
		{
			vector.add(tokens.get(i));
		}
		return vector.elements();
	}
	
	/**
	 * Get a subsequence of the TokenSequence
	 * @param start
	 * @return
	 */
	public TokenSequence subSequence(int start)
	{
		Builder b = new Builder();
		Enumeration<Token> t = enumerate();
		int index = 0;
		Token token = t.nextElement();
		do {
			if (index >= start)
				b.append(token);
			index++;
		}
		while (t.hasMoreElements());
		return b.create();
	}
	
	/**
	 * Get a subSequence of the TokenSequence
	 * @param start
	 * @param end
	 * @return
	 */
	public TokenSequence subSequence(int start, int end)
	{
		Builder b = new Builder();
		Enumeration<Token> t = enumerate();
		int index = 0;
		Token token = t.nextElement();
		do {
			if (index >= end)
				break;
			if (index >= start)
				b.append(token);
			index++;
		}
		while (t.hasMoreElements());
		return b.create();
	}
	
	@Override
	public String toString()
	{
		return string;
	}
	
	public String toDebugString()
	{
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < tokens.size(); i++)
		{
			builder.append(tokens.get(i).toDebugString());
		}
		return builder.toString();
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (object instanceof TokenSequence)
		{
			TokenSequence seq = (TokenSequence) object;
			if (tokens.size() == seq.tokens.size())
			{
				if (tokens.isEmpty())
					return true;
				Enumeration<Token> A = enumerate();
				Enumeration<Token> B = seq.enumerate();
				Token t1 = A.nextElement();
				Token t2 = B.nextElement();
				do
				{
					if (t1.nequals(t2))
						return false;
					t1 = A.nextElement();
					t2 = B.nextElement();
				}
				while (A.hasMoreElements());
				return true;
			}
			
			
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = this.tokens != null ? this.tokens.hashCode() : 0;
		result = 31 * result + (this.string != null ? this.string.hashCode() : 0);
		return result;
	}

	/**
	 * Not Equals
	 * @param o
	 * @return
	 */
	public boolean nequals(Object o)
	{
		return !equals(o);
	}
	
	public boolean endsWith(Token t)
	{
		return tokens.get(tokens.size()-1).equals(t);
	}
	
	public boolean startsWith(Token t)
	{
		return tokens.get(0).equals(t);
	}
	
	public boolean startsWith(TokenSequence seq)
	{
		TokenSequence sub = subSequence(0, seq.length());
System.out.println("subSequence(0," + seq.length() + " = " + sub);//TODO remove debug line
		return sub.equals(seq);
	}
	
	public boolean startsWith(String s)
	{
		try
		{
			return startsWith(parse(s));
		}
		catch (IOException io)
		{
			io.printStackTrace(System.out);
		}
		return false;
	}
	
	/**
	 * Splits the string on any of the given Tokens. This is useful, for example, when splitting 
	 * on either {@link ParserConstants#COLON} or {@link ParserConstants#DOUBLE_COLON}.
	 * @param t
	 * @return
	 */
	public TokenSequence[] splitOnAny(Token[] t)
	{
		List<TokenSequence> list = new ArrayList<>();
		Builder b = new Builder();
		for (int i = 0; i < length(); i++)
		{
			Token token = tokens.get(i);
			for (Token _t : t)
			{
				if (_t.equals(token))
				{
					list.add(b.create());
					b.clear();
					break;
				}
				else
				{
					b.append(token);
				}
			}
			
		}
		TokenSequence lastSequence = b.create();
		if (lastSequence.length() > 0)
			list.add(lastSequence);
		TokenSequence[] array = new TokenSequence[list.size()];
		array = list.toArray(array);
		return array;
	}
	
	/**
	 * Split the sequence on a given token
	 * @param t
	 * @return
	 */
	public TokenSequence[] split(Token t)
	{
		List<TokenSequence> list = new ArrayList<>();
		Builder b = new Builder();
		for (int i = 0; i < length(); i++)
		{
			Token token = tokens.get(i);
			if (t.equals(token))
			{
				list.add(b.create());
				b.clear();
				
			}
			else
			{
				b.append(token);
			}
		}
		TokenSequence lastSequence = b.create();
		if (lastSequence.length() > 0)
			list.add(lastSequence);
		TokenSequence[] array = new TokenSequence[list.size()];
		array = list.toArray(array);
		return array;
	}
	
	public boolean contains(Token t)
	{
		for (int i = 0; i < length(); i++)
		{
			if (t.equals(tokens.get(i)))
				return true;
		}
		return false;
	}
	
	public boolean contains(TokenSequence t)
	{
		for (int i = 0; i < tokens.size(); i++)
		{
			Token token = tokens.get(i);
			if (token.equals(t.tokens.get(0)))
			{
				if (i + t.length() < length())
				{
					if (subSequence(i, t.length()-1).equals(t))
					return true;
				}
			}
		}
		return false;
	}
	
	public int indexOf(Token t)
	{
		for (int i = 0; i < length(); i++)
		{
			if (t.equals(tokens.get(i)))
				return i;
		}
		return -1;
	}

	@Override
	public Iterator<Token> iterator() {
		return tokens.iterator();
	}
	
	/**
	 * Helper class for creating a TokenSequence
	 * @author Phil Brown
	 * @since 1:37:05 PM Jan 6, 2014
	 *
	 */
	public static class Builder
	{
		List<Token> tokens;
		StringBuilder builder;
		
		/**
		 * Constructor
		 */
		public Builder()
		{
			tokens = new ArrayList<>();
			builder = new StringBuilder();
		}
		
		/**
		 * Append a token
		 * @param t
		 * @return
		 */
		public Builder append(Token t)
		{
			tokens.add(t);
			builder.append(t.toString());
			return this;
		}
		
		/**
		 * Append a TokenSequence
		 * @param t
		 * @return
		 */
		public Builder append(TokenSequence t)
		{
			for (int i = 0; i < t.length(); i++)
			{
				Token token = t.tokens.get(i);
				tokens.add(token);
				builder.append(token.toString());
			}
			
			return this;
		}
		
		/**
		 * Create the sequence
		 * @return
		 */
		public TokenSequence create()
		{
			return new TokenSequence(tokens, builder.toString());
		}
		
		/**
		 * Reset
		 */
		public void clear()
		{
			tokens.clear();
			builder = new StringBuilder();
		}
	}
}
