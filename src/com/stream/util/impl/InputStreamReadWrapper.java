
package com.stream.util.impl;

import static com.stream.util.core.UtilConstants.BUFFER_SIZE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.SequenceInputStream;

import com.stream.util.DataLoader;
import com.stream.util.core.MD5;
import com.stream.util.core.MessageDigestAlgorithm;
import com.stream.util.core.Util;



public class InputStreamReadWrapper extends InputStream {

	private InputStream is;
	
	private int bytesSize = BUFFER_SIZE;
	
	byte[] bytes = new byte[bytesSize];
	
	private boolean hasBytes = true;
			
	int readByteLeng = 0;
	
	private long readLimit = -1;
	
	private long totalBytesread = 0;
	
	private MessageDigestAlgorithm mda;

	// dataLoader out Stream
	PipedOutputStream pout;

	private MD5 algorithm;
	
	
	public long getReadLimit() {
		return readLimit;
	}

	public void setReadLimit(long readLimit) {
		if(readLimit == -1) {
			this.readLimit = readLimit;
		}else{
			this.readLimit= totalBytesread + readLimit ;
		}
		
		hasBytes = true;
		//bytes = new byte[bytesSize];
	}

	// Default Constructor
	InputStreamReadWrapper(){
	}
	
	// Used for converting data to inputStream
	public InputStreamReadWrapper(DataLoader dataLoader) throws IOException {
		
		PipedInputStream pin = new PipedInputStream();
		this.pout = new PipedOutputStream(pin);
		setInputStream(pin);

		new LoaderThread(pout, dataLoader).start();
		
	}
	
	void setInputStream(InputStream is) {
		this.is = is;
		this.bytesSize = BUFFER_SIZE;
	}
	
	public InputStreamReadWrapper(InputStream is, MessageDigestAlgorithm mda) {
		this.is = is;
		this.bytesSize = BUFFER_SIZE;
		this.mda = mda;
		algorithm = new MD5();
	}
	
	public InputStreamReadWrapper(InputStream is) {
		this.is = is;
		this.bytesSize = BUFFER_SIZE;
	}
	/*
	public InputStreamReadWrapper(InputStream is, int bytesSize) {
		this.is = is;
		this.bytesSize = bytesSize;
	}*/
	
	public InputStreamReadWrapper(InputStream is, long readLimit) {
		this.is = is;
		this.bytesSize = 1024*128;
		this.readLimit = readLimit;
		/*if(this.bytesSize > this.readLimit){
			this.bytesSize = (int) readLimit;
		}*/
	}

/*	public InputStreamReadWrapper(InputStream is, int bytesSize, long readLimit) {
		this.is = is;
		this.bytesSize = bytesSize;
		this.readLimit = readLimit;
	}
*/	

	public boolean next() throws IOException {
		if(hasBytes == true){
			readBytes();
		}
		return hasBytes;
	}
	
	public boolean readLine(int readLineLimit) throws IOException, NoSuchMethodException {
		
		if(mda != null){
			throw new NoSuchMethodException();
		}
		
		if(readLineLimit < 1){
			return false;
		}
		
		setReadLimit(readLineLimit);
		
		boolean isNewLineAvailable = false;

		int destOffset = 0;
		byte[] bytesTemp = new byte[readLineLimit];
		while(next()) {
			System.arraycopy(getBytes(), 0, bytesTemp, destOffset, getReadByteLeng());
			destOffset += getReadByteLeng();
		}
		
		setReadLimit(-1);
		
		int NLCR = Util.byteIndexOf(bytesTemp, "\n\r".getBytes(), bytesTemp.length);
		int NL = Util.byteIndexOf(bytesTemp, "\n".getBytes(), bytesTemp.length);

		int CRNL = Util.byteIndexOf(bytesTemp, "\r\n".getBytes(), bytesTemp.length);
		int CR = Util.byteIndexOf(bytesTemp, "\r".getBytes(), bytesTemp.length);
		
		
		int lowIndex = getLowest(NLCR, NL, CRNL, CR);
		
		if(lowIndex != -1) {
			isNewLineAvailable = true;
			int newLineBytes = getNewLineBytes(NLCR, NL, CRNL, CR, lowIndex);
			
			System.arraycopy(bytesTemp, 0, bytes, 0, lowIndex+newLineBytes);
			
			// Put back the remaining bytes in the stream.
			readByteLeng = lowIndex+newLineBytes;
			totalBytesread -= (bytesTemp.length - (lowIndex+newLineBytes));
			
			is = new SequenceInputStream(new ByteArrayInputStream (bytesTemp, lowIndex+newLineBytes, bytesTemp.length-(lowIndex+newLineBytes)), is);
			
		} else {
			// Put back the remaining bytes in the stream.
			readByteLeng = 0;
			totalBytesread -= bytesTemp.length;
			is = new SequenceInputStream(new ByteArrayInputStream(bytesTemp), is);
			
		}
		
		return isNewLineAvailable;
		
	}
	
	
	/**
	 * @param nLCR
	 * @param nL
	 * @param cRNL
	 * @param cR
	 * @return
	 */
	private int getNewLineBytes(int NLCR, int NL, int CRNL, int CR, int lowIndex) {
		int newLineBytes = 0;
		if (NLCR == NL && lowIndex == NLCR) {
			newLineBytes = 2;
		}else if (CRNL == CR && lowIndex == CR) {
			newLineBytes = 2;
		}else {
			
			if(lowIndex == NLCR){
				newLineBytes = 2;
			}else if(lowIndex == NL){
				newLineBytes = 1;
			}else if(lowIndex == CRNL){
				newLineBytes = 2;
			}else if(lowIndex == CR){
				newLineBytes = 1;
			}
		}
		return newLineBytes;
	}

	private int getLowest(int NLCR, int NL, int CRNL, int CR) {
		
		int low = -1;
		if(NLCR !=-1){
			low = NLCR;
		}else if (NL != -1){
			low = NL;
		}else if(CRNL != -1){
			low = CRNL;
		}else if (CR != -1){
			low = CR;
		}
		
		if (low == -1){
			return low;
		}
		
		if(NL !=-1 && NLCR>=NL){
			low = NL;
		}
		if(CRNL !=-1 && low>CRNL){
			low = CRNL;
		}
		if(CR !=-1 && low>CR){
			low = CR;
		}
		return low;
	}

	byte[] readBytes() throws IOException {
		try {
			int toRead = bytes.length;
			//int prevReadByteLeng =readByteLeng;
			if(readLimit != -1) {
				if(readLimit-totalBytesread < bytes.length){
					toRead = (int) (readLimit - totalBytesread); 
					//bytes = new byte[toRead];
				}
			}
			
			readByteLeng = is.read(bytes, 0, toRead);
			
			if(readLimit != -1) {
				if(totalBytesread == readLimit || (  //prevReadByteLeng == -1 && 
						readByteLeng == -1)) {
					hasBytes = false;
				}else{
					hasBytes = true;
				}
			}else{
				if(readByteLeng != -1) {
					hasBytes = true;
				}else {
					hasBytes = false;
				}
				/*if(readLimit != -1) {
					
				}*/
			}
			totalBytesread += readByteLeng;
			
		} catch (IOException e) {
			readByteLeng = -1;
			hasBytes = false;
			e.printStackTrace();
		}
		return bytes;
	}
	
	public int read(byte[] buf, int off, int len) throws IOException {
		byte[] temp = bytes;
		bytes = buf;
		readBytes();
		
		calculateMessageDigest();
		bytes = temp;
		return readByteLeng;
		
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}
	
	public int read() throws IOException {
		if(readLimit-totalBytesread <= 0) {
    		return -1;
    	}
		byte[] buff = new byte[1];
		return read(buff, 0, 1);
		
	}
	
	@Override
	public void close() throws IOException {
		super.close();
	}

	public int available() throws IOException {

		if (readLimit != -1) {
			if (readLimit - totalBytesread <= 0) {
				return -1;
			} else if (readLimit - totalBytesread > bytes.length) {
				return bytes.length;
			} else {
				return (int) (readLimit - totalBytesread);
			}
		}
		return is.available();
	}
	
	
	public int getReadByteLeng() {
		return readByteLeng;
	}

	public String getMessageDigestValue() {
		
		if(mda != null){
			return algorithm.getMd5();
		}
		return null;
	}
	
	private void calculateMessageDigest() {
		if(mda == MessageDigestAlgorithm.MD5 && readByteLeng > 0) {
			//System.out.println("readByteLeng : "+readByteLeng +" bytes leng : "+bytes.length+" totalBytesread : "+totalBytesread);
			algorithm.update(bytes, 0, readByteLeng);
		}

	}
	
	public byte[] getBytes() {
		//System.out.println(readByteLeng);
		byte[] temp = new byte[readByteLeng]; 
		System.arraycopy(bytes, 0, temp, 0, temp.length);
		
		calculateMessageDigest();
		
		return temp;
	}
	

}

class LoaderThread extends Thread {
    
    OutputStream outStream = null;
    DataLoader dataLoader;
    
    public LoaderThread(OutputStream outStream, DataLoader dataLoader) {
        this.outStream = outStream;
        this.dataLoader = dataLoader;
    }
    
    @Override
	public void run() {
		try {
			dataLoader.load(outStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
