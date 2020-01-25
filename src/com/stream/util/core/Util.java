/**
 * 
 */
package com.stream.util.core;

import static com.stream.util.core.UtilConstants.BUFFER_SIZE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.zip.GZIPOutputStream;


import com.stream.util.impl.InputStreamReadWrapper;


public class Util {

	public static int getByteLength(String dataString, String charSet) 
			throws UnsupportedEncodingException {
		int dataLength = dataString.length();
		int index = 0;
		int byteLength = 0;
		int strChunkSize = 10000;
		
		while (index < dataLength) {
			if (index + strChunkSize > dataLength) {
				strChunkSize = dataLength - index;
			}
			byteLength += (dataString.substring(index, index + strChunkSize).getBytes(charSet).length);
			index += strChunkSize;
		}
		return byteLength;
	}
	
	public static int getUTF8ByteLength(String dataString) throws UnsupportedEncodingException {
		
		return getByteLength(dataString, "UTF-8");
	}
	
	
	public static Set<String> splitRemoveDups(String value, String delimeter) {
		int fromIndex = 0;
		int pos = 0;
		Set<String> resultSet = new LinkedHashSet<String>();
		
		while((pos = value.indexOf(delimeter, fromIndex)) > 0) {
			resultSet.add(value.substring(fromIndex, pos));
			fromIndex = pos+1;
		}
		
		if(!value.substring(fromIndex, value.length()).trim().equals("")){
			resultSet.add(value.substring(fromIndex, value.length()).trim());
		}
		
		return resultSet;
	}
	
	public static int byteIndexOf(byte[] srcData, byte[] dataToFind) {
		return byteIndexOf(srcData, dataToFind, srcData.length);
    }
	
	public static int byteIndexOf(byte[] srcData, byte[] dataToFind, int iDataLen) {
        int iDataToFindLen = dataToFind.length;
        int iMatchDataCntr = 0;
        for (int i = 0; i < iDataLen; i++) {
            if (srcData[i] == dataToFind[iMatchDataCntr]) {
                iMatchDataCntr++;
            } else {
                if (srcData[i] == dataToFind[0]) {
                    iMatchDataCntr = 1;
                } else {
                    iMatchDataCntr = 0;
                }
            }
            if (iMatchDataCntr == iDataToFindLen) {
                return i-iDataToFindLen+1;
            } 
        }
        return -1;
    }

	public static int byteLastIndexOf(byte[] srcData, byte[] dataToFind) {
        int iDataLen = srcData.length;
        int iDataToFindLen = dataToFind.length;
        int iMatchDataCntr = iDataToFindLen-1;
        for (int counter = iDataLen-1; counter >= 0; counter--) {
            if (srcData[counter] == dataToFind[iMatchDataCntr]) {
                iMatchDataCntr--;
            } else {
                iMatchDataCntr = iDataToFindLen-1;
            }
            if (iMatchDataCntr == 0) {
                return counter-1;
            } 
        }
        return -1;
    }
	

	public static final void writeTo(Reader in, Writer out) throws IOException {
		int read;
		final char[] data = new char[BUFFER_SIZE];
		while ((read = in.read(data)) != -1) {
			out.write(data, 0, read);
		}
	}

	public static final void writeTo(InputStream in, OutputStream out) throws IOException {
		int read;
		final byte[] data = new byte[BUFFER_SIZE];
		while ((read = in.read(data)) != -1) {
			out.write(data, 0, read);
		}
	}
	
	public static final void writeTo(InputStreamReadWrapper insWrap, OutputStream out) throws IOException {
		while(insWrap.next()) {
			out.write(insWrap.getBytes());
    	}
	}


	
}
