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

/**
 * Contains Tokens and Reserved words that are used during parsing
 * @author Phil Brown
 * @since 2:39:59 PM Dec 18, 2013
 */
public interface ParserConstants 
{
	public static final int LINK = 1,
			                VISITED = 2,
			                ACTIVE = 3,
			                HOVER = 4,
			                FOCUS = 5,
			                FIRST_LETTER = 6,
			                FIRST_LINE = 7,
			                FIRST_CHILD = 8,
			                BEFORE = 9,
			                AFTER = 10,
			                LANG = 11,
			                FIRST_OF_TYPE = 12,
			                LAST_OF_TYPE = 13,
			                ONLY_OF_TYPE = 14,
			                ONLY_CHILD = 15,
			                NTH_CHILD = 16,
			                NTH_LAST_CHILD = 17,
			                NTH_OF_TYPE = 18,
			                NTH_LAST_OF_TYPE = 19,
			                LAST_CHILD = 20,
			                ROOT = 21,
			                EMPTY = 22,
			                TARGET = 23,
			                ENABLED = 24,
			                DISABLED = 25,
			                CHECKED = 26,
			                NOT = 27,
			                SELECTION = 28;
	
	//Room here for more keywords
	
	public static final int DOT = 101,
			                HASH = 102,
			                COMMA = 103,
			                COLON = 104,
			                SEMICOLON = 105,
			                LEFTSQ = 106,
			                RIGHTSQ = 107,
			                MINUS = 108,
			                LEFTPAREN = 109,
			                RIGHTPAREN = 110,
			                TIMES = 111,
			                GT = 112,
			                LT = 113,
			                OR = 114,
			                PLUS = 115,
			                NOT_EQUAL = 116,
			                EQUAL = 117,
			                OR_EQUAL = 118,
			                TIMES_EQUAL = 119,
			                CARET_EQUAL = 120,
			                DOLLAR_EQUAL = 121,
			                AT = 123,
			                CARET = 124,
			                DOLLAR = 125,
			                NUMBER = 126,
			                IDENTIFIER = 127,
			                LEFT_CURLY_BRACKET = 128,
			                RIGHT_CURLY_BRACKET = 129,
			                SLASH_STAR = 130,
			                STAR_SLASH = 131,
			                SLASH = 132,
			                COMMENT = 133,
			                DOUBLE_COLON = 134,
			                SINGLE_QUOTE = 135,
			                DOUBLE_QUOTE = 136,
			                AT_RULE = 137,
			                PERCENT = 138,
			                SPACE = 139,
			                NULL = 140;

	public static final char EOF = 1000;
	public static final char EOFCHAR = (char)(-1);
	
	/**
	 * CSS Strings associated with variables above (same indices)
	 */
	public static final String[] RESERVEDWORD = {"","link","visited","active","hover",
        "focus","first-letter","first-line","first-child","before","after","lang","first-of-type","last-of-type","only-of-type",
        "only-child","nth-child","nth-last-child","nth-of-type","nth-last-of-type","last-child","root","empty","target",
        "enabled","disabled","checked","not","selection"};

	public static final boolean DEBUG = false;
}
