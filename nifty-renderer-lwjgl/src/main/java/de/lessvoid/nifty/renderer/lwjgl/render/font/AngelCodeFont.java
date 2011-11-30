package de.lessvoid.nifty.renderer.lwjgl.render.font;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Hashtable;

import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * AngelCodeFont loading.
 * @author void
 */
public class AngelCodeFont {

  /**
   * name of font.
   */
  private String name;

  /**
   * Texture HashMap for the font.
   * Key = page index
   * Value = filename of texture
   */
  private HashMap < Integer, String > textures = new HashMap < Integer, String >();

  /**
   * width of single font texture page.
   */
  private int width;

  /**
   * height of single font texture page.
   */
  private int height;

  /**
   * height of single line.
   */
  private int lineHeight;

  /**
   * CharacterInfo for all characters in the font file.
   */
  private Hashtable < Character, CharacterInfo > chars = new Hashtable < Character, CharacterInfo > ();

  private NiftyResourceLoader resourceLoader;

  public AngelCodeFont(final NiftyResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  /**
   * load the font with the given name.
   * @param filename file to load
   * @return true on success and false on any error
   */
  public boolean load(final String filename) {
    InputStream in = resourceLoader.getResourceAsStream(filename);
    if (in == null) {
      return false;
    }

    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      try {
        while (true) {
          String line = reader.readLine();
          if (line == null) {
            break;
          }

          if (line.startsWith("info")) {
            HashMap < String, String > values = getInfoLine(line);
            parseInfo(values);
          } else if (line.startsWith("common")) {
            HashMap < String, Integer > values = splitToInteger(line);
            parseCommon(values);
          } else if (line.startsWith("page")) {
            HashMap < String, String > values = getPageLine(line);
            parsePage(values);
          } else if (line.startsWith("chars")) {
            HashMap < String, Integer > values = splitToInteger(line);
            parseChars(values);
          } else if (line.startsWith("char")) {
            HashMap < String, Integer > values = splitToInteger(line);
            parseChar(values);
          } else if (line.startsWith("kernings")) {
            HashMap < String, Integer > values = splitToInteger(line);
            parseKernings(values);
          } else if (line.startsWith("kerning")) {
            HashMap < String, Integer > values = splitToInteger(line);
            parseKerning(values);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }

      return true;
    } finally {
      try {
        in.close();
      } catch (IOException e) {
      }
    }
  }

  /**
   * parse info.
   * @param line line with info
   */
  private void parseInfo(final HashMap < String, String > line) {
    name = line.get("face");
  }

  private void parseCommon( HashMap<String,Integer> line )
  {
    width= line.get( "scaleW" );
    height= line.get( "scaleH" );
  }
  
  private void parsePage( HashMap<String,String> line )
  {
    Integer id= Integer.parseInt( line.get( "id" ));
    textures.put( id, line.get( "file" ));
  }
  
  private void parseChars( HashMap<String,Integer> line )
  {
  }
  
  private void parseChar( HashMap<String,Integer> line )
  {
    CharacterInfo c= new CharacterInfo();
    c.setId(line.get( "id" ));
    c.setX(line.get( "x" ));
    c.setY(line.get( "y" ));
    c.setWidth(line.get( "width" ));
    c.setHeight(line.get( "height" ));
    c.setXoffset(line.get( "xoffset" ));
    c.setYoffset(line.get( "yoffset" ));
    c.setXadvance(line.get( "xadvance" ));
    c.setPage(line.get( "page" ));
    chars.put( Character.valueOf( (char)c.getId() ), c );
    lineHeight = Math.max(c.getHeight() + c.getYoffset(), lineHeight);
  }
  
  private void parseKernings( HashMap<String,Integer> line )
  {
  }
  
  private void parseKerning( HashMap<String,Integer> line )
  {
    int first= line.get( "first" );
    int second= line.get( "second" );
    int amount= line.get( "amount" );
    CharacterInfo info= chars.get( Character.valueOf( (char)first ));
    info.getKerning().put( Character.valueOf( (char)second ), amount );
  }
  
  private HashMap<String,Integer> splitToInteger( String line )
  {
    HashMap<String,Integer> table= new HashMap<String,Integer>();
    
    String[] values= line.split( " " );
    for( int i=0; i<values.length; i++ )
    {
      String[] current= values[ i ].split( "=" );
      if( current.length > 1 )
      {
        table.put( current[ 0 ], Integer.valueOf( current[ 1 ]));
      }
      else
      {
        table.put( current[ 0 ], null );
      }
    }
    
    return table;
  }
  
  /**
   * 
   * @param line
   * @return
   */
  private HashMap<String,String> getInfoLine( String line )
  {
    HashMap<String,String> table= new HashMap<String,String>();
    
    try
    {
      StreamTokenizer tokenizer = new StreamTokenizer( new StringReader( line ));
      tokenizer.whitespaceChars( ' ', ' ' );
      tokenizer.parseNumbers();
      tokenizer.quoteChar( '"' );
      
      // info face="Times New Roman" size=24 bold=0 italic=0 charset="" unicode=1 stretchH=100 smooth=1 aa=4 padding=1,1,1,1 spacing=1,1

      // info
      tokenizer.nextToken();

      // face
      tokenizer.nextToken();
      tokenizer.nextToken();
      tokenizer.nextToken();
      table.put( "face", tokenizer.sval );
      
      // size
      tokenizer.nextToken();
      tokenizer.nextToken();
      tokenizer.nextToken();
      table.put( "size", Integer.toString( (int)tokenizer.nval ));

      // bold
      tokenizer.nextToken();
      tokenizer.nextToken();
      tokenizer.nextToken();
      table.put( "bold", Integer.toString( (int)tokenizer.nval ));
      
      // italic
      tokenizer.nextToken();
      tokenizer.nextToken();
      tokenizer.nextToken();
      table.put( "italic", Integer.toString( (int)tokenizer.nval ));
      
      // charset
      tokenizer.nextToken();
      tokenizer.nextToken();
      tokenizer.nextToken();
      table.put( "charset", tokenizer.sval );

      // unicode
      tokenizer.nextToken();
      tokenizer.nextToken();
      tokenizer.nextToken();
      table.put( "unicode", Integer.toString( (int)tokenizer.nval ));

      // stretchH
      tokenizer.nextToken();
      tokenizer.nextToken();
      tokenizer.nextToken();
      table.put( "stretchH", Integer.toString( (int)tokenizer.nval ));

      // smooth
      tokenizer.nextToken();
      tokenizer.nextToken();
      tokenizer.nextToken();
      table.put( "smooth", Integer.toString( (int)tokenizer.nval ));

      // aa
      tokenizer.nextToken();
      tokenizer.nextToken();
      tokenizer.nextToken();
      table.put( "aa", Integer.toString( (int)tokenizer.nval ));

      // padding
      tokenizer.nextToken();
      tokenizer.nextToken();
      tokenizer.nextToken();
      String padding= Integer.toString( (int)tokenizer.nval );
      tokenizer.nextToken();
      padding= padding + "," + Integer.toString( (int)tokenizer.nval );
      tokenizer.nextToken();
      padding= padding + "," + Integer.toString( (int)tokenizer.nval );
      tokenizer.nextToken();
      padding= padding + "," + Integer.toString( (int)tokenizer.nval );
      table.put( "padding", padding );
      
      // spacing
      tokenizer.nextToken();
      tokenizer.nextToken();
      tokenizer.nextToken();
      String spacing= Integer.toString( (int)tokenizer.nval );
      tokenizer.nextToken();
      spacing= spacing + "," + Integer.toString( (int)tokenizer.nval );
      table.put( "spacing", spacing );
    }
    catch( IOException e )
    {
      e.printStackTrace();
    }
    
    return table;
  }

  private HashMap<String,String> getPageLine( String line )
  {
    HashMap<String,String> table= new HashMap<String,String>();

    try
    {
      StreamTokenizer tokenizer = new StreamTokenizer( new StringReader( line ));
      tokenizer.whitespaceChars( ' ', ' ' );
      tokenizer.parseNumbers();
      tokenizer.quoteChar( '"' );
      
      // page id=0 file="Times New Roman--24_00.tga"

      // page
      tokenizer.nextToken();

      // id
      tokenizer.nextToken();
      tokenizer.nextToken();
      tokenizer.nextToken();
      table.put( "id", Integer.toString( (int)tokenizer.nval ) );
      
      // file
      tokenizer.nextToken();
      tokenizer.nextToken();
      tokenizer.nextToken();
      table.put( "file", tokenizer.sval );
    }
    catch( IOException e )
    {
      e.printStackTrace();
    }

    return table;
  }

  /**
   * get the font name
   * @return
   */
  public String getName()
  {
    return name;
  }

  /**
   * get the font texture
   * @return
   */
  public String[] getTextures()
  {
    return textures.values().toArray( new String[0] );
  }
  
  /**
   * 
   * @return
   */
  public int getNumChars()
  {
    return chars.size();
  }
  
  /**
   * get character info for the given char
   * @param c
   * @return
   */
  public CharacterInfo getChar( char c )
  {
    return chars.get( Character.valueOf( c ) );
  }

  public Hashtable<Character, CharacterInfo> getChars()
  {
    return chars;
  }

  public int getHeight()
  {
    return height;
  }

  public int getWidth()
  {
    return width;
  }

  public int getLineHeight()
  {
    return lineHeight;
  }
  
}
