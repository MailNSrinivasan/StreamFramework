package com.stream.util.impl;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.zip.GZIPOutputStream;



public class GZippedStream extends InputStreamReadWrapper {
	
	
	public GZippedStream(InputStream is) throws IOException {
		
		super();
		PipedOutputStream pout = new PipedOutputStream();
		PipedInputStream pin = new PipedInputStream(pout);
		GZIPOutputStream out = new GZIPOutputStream(pout);
		setInputStream(pin);
		new GzipThreadOut(out, is).start();
	}

	
	public void getGzippedSize() {
		
	}


	public void getInputStreamSize() {
		// TODO Auto-generated method stub
	}
	
}

class GzipThreadOut extends Thread {
    
    OutputStream outStream = null;
    InputStream inStream = null;

    
    public GzipThreadOut(OutputStream outStream, InputStream inStream) {
        this.outStream = outStream;
        this.inStream = inStream;
    }
    
    public void run() {
        if (outStream != null && inStream != null) {
            try {
            	
            	InputStreamReadWrapper insWrap = new InputStreamReadWrapper(inStream);
            	byte[] bytes = null;
            	while(insWrap.next()){
            		bytes = insWrap.getBytes();
            		outStream.write(bytes, 0, insWrap.getReadByteLeng());
            	}
            	insWrap.close();
            	
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
            	try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
    }


	
    
}


