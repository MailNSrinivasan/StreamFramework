/**
 * 
 */
package com.stream.util.impl;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.zip.GZIPOutputStream;

import com.stream.util.DataReader;


public class InputStreamGzipReadWrapper extends InputStreamReadWrapper {


	InputStream gzipInStream;
	
	PipedOutputStream pTout;
	
	public InputStreamGzipReadWrapper(InputStream is, DataReader gzipStreamProcessor) throws IOException {
		
		super(is);
		
		// 
		PipedOutputStream pout = new PipedOutputStream();
		PipedInputStream gzipInStream = new PipedInputStream(pout);
		GZIPOutputStream out = new GZIPOutputStream(pout);
		new GzipReadThread(gzipInStream, gzipStreamProcessor).start();
		
		// Written to the below out Stream while read by the client 
		pTout = new PipedOutputStream();
		PipedInputStream datain = new PipedInputStream(pTout);
		InputStreamReadWrapper dataInWrap = new InputStreamReadWrapper(datain);
		new GzipThread(out, dataInWrap).start();
	}

	

	public InputStream getGzipInStream(){
		return gzipInStream;
	}
	
	void writeData(byte[] buf, int off, int len) {
		if (readByteLeng > 0) {
			try {
				pTout.write(buf, off, len);
				pTout.flush();
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				pTout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public byte[] readBytes() throws IOException {
		super.readBytes();
		writeData(bytes, 0, readByteLeng);
		return bytes;
	}
	
	@Override
	public int read(byte[] buf, int off, int len) throws IOException {
		super.read(buf, off, len);
		
		writeData(buf, off, readByteLeng);
		return readByteLeng;
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		super.read(b, 0, b.length);
		writeData(b, 0, readByteLeng);
		return readByteLeng;
		//return read(b, 0, b.length);
	}

	@Override
	public int read() throws IOException {
		byte[] buff = new byte[1];
		super.read(buff, 0, 1);
		writeData(buff, 0, readByteLeng);
		return readByteLeng;
	}
	

}

class GzipReadThread extends Thread {

	InputStream pin;
	FileOutputStream ffOutStream;
	DataReader gzipStreamProcessor;
	
	public GzipReadThread(PipedInputStream gzipInStream, DataReader gzipStreamProcessor) {
		pin = gzipInStream;
		this.gzipStreamProcessor = gzipStreamProcessor;
		
	}
	
	@Override
	public void run() {
		
		InputStreamReadWrapper insWrap = new InputStreamReadWrapper(pin);
		//gzipStreamProcessor.readBytes(insWrap);
		
		try {
			while(insWrap.next()){
				gzipStreamProcessor.readBytes(insWrap.getBytes(), 0, insWrap.getReadByteLeng());
			}
			insWrap.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
}

class GzipThread extends Thread {
    
    OutputStream outStream = null;
    InputStreamReadWrapper inStream = null;

    
    public GzipThread(OutputStream outStream, InputStreamReadWrapper inStream) {
        this.outStream = outStream;
        this.inStream = inStream;
    }
    
    public void run() {
        if (outStream != null && inStream != null) {
            try {
            	while(inStream.next()) {
            		outStream.write(inStream.getBytes());
            	}
            	System.out.println("Stream Close :::: ");
            	outStream.close();
            	inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
