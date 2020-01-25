/**
 * 
 */
package com.stream.util.samplecode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.stream.util.core.MessageDigestAlgorithm;
import com.stream.util.core.Util;
import com.stream.util.impl.InputStreamReadWrapper;


public class CalculateMd5 {

	public static void main(String[] args) throws IOException {
		
		
		String fileName = "C:\\tmp\\Gzip\\TestData22222222222222.txt";
		FileInputStream fileInStream = new FileInputStream(fileName);
		
		String fileOut = "C:\\tmp\\Gzip\\TestDataWriterrr.txt";
		FileOutputStream fileOutStream = new FileOutputStream(fileOut);
		
		
		InputStreamReadWrapper insReadWrap = new InputStreamReadWrapper(fileInStream, MessageDigestAlgorithm.MD5);
		/*while(insReadWrap.next()) {
			insReadWrap.getBytes();
		}*/
		Util.writeTo(insReadWrap, fileOutStream);
		fileOutStream.close();
		
		System.out.println(insReadWrap.getMessageDigestValue());
	}
}
