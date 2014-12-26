package self.philbrown.cssparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Modeled after <a href="http://grepcode.com/file_/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/io/SequenceInputStream.java/?v=source">SequenceInputStream</a>,
 * this InputStream can continue to accept new streams.
 * @author Phil Brown
 * @since 11:08:58 AM Dec 27, 2013
 * @deprecated not used, and did not work as needed.
 */
public class SequentialInputStream extends InputStream
{
    private List<InputStream> streams;
    private InputStream in;
    private Iterator<InputStream> iterator;
    private int index = -1;

    public SequentialInputStream(InputStream... streams) throws IOException
    {
        this.streams = new ArrayList<InputStream>();
        for (InputStream stream : streams)
        {
        	this.streams.add(stream);
        }
        iterator = this.streams.iterator();
        nextStream();
    }
    
    /**
     * Add a stream to the end
     * @param stream
     */
    public void append(InputStream stream)
    {
    	this.streams.add(stream);
    }
    
    /**
     * Add a stream here.
     * @param stream
     */
    public void insert(InputStream stream)
    {
    	insert(stream, index);
    }
    
    /**
     * Add a stream at the given index.
     * @param stream
     */
    public void insert(InputStream stream, int index)
    {
    	System.out.println("BEFORE: in=" + in);
    	streams.add(index, stream);
    	if (index == this.index)
    	{
    		System.out.println("SPECIAL CASE. INDEX=" + index);
    		//special case
    		iterator = streams.iterator();
    		in = stream;
//    		for (int i = 0; i <= index; i++)
//    		{
//    			try {nextStream();}catch(Throwable t){t.printStackTrace();}
//    			//in = iterator.next();
//    		}
    		//in = stream;
    		
        	//cursor = 0;
    	}
    	System.out.println("AFTER: in=" + in);
    }

    /**
     *  Continues reading in the next stream if an EOF is reached.
     */
    private void nextStream() throws IOException {
    	System.out.println("NEXT STREAM");
        if (in != null) {
            in.close();
        }
        
        if (iterator.hasNext())
        {
        	in = iterator.next();
        	index++;
        	if (in == null)
                throw new NullPointerException();
        }
        else 
        {
        	in = null;
        }

    }
    
    /**
     * Method taken from <a href="http://grepcode.com/file_/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/io/SequenceInputStream.java/?v=source">SequenceInputStream</a>.
     */
    @Override
    public int read() throws IOException {
        if (in == null) {
            return -1;
        }
        int c = in.read();
        if (c == -1) {
            nextStream();
            return read();
        }
        return c;
    }
    
    @Override
    public int read(byte b[], int off, int len) throws IOException 
    {
        if (in == null) {
            return -1;
        } else if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int n = in.read(b, off, len);
        if (n <= 0) {
            nextStream();
            return read(b, off, len);
        }
        return n;
    }

    /**
     * Method taken from <a href="http://grepcode.com/file_/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/io/SequenceInputStream.java/?v=source">SequenceInputStream</a>.
     */
    public void close() throws IOException {
        do {
            nextStream();
        } while (in != null);
    }
}