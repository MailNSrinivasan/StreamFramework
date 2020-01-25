
package com.stream.util.samplecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.stream.util.core.Util;
import com.stream.util.impl.GZippedStream;


public class InputStreamToGzipStreamConvertorSample {

	public static void main(String[] args) throws IOException {
		
		File inputFile = new File("C:\\tmp\\Gzip\\4d7b90bba1c5fa0204fb7851c6c3af0500daf207e437");
		FileInputStream fileInputStream = new FileInputStream(inputFile);
		FileOutputStream fileOutStream = new FileOutputStream("C:\\tmp\\Gzip\\Zipped_neww.zip");

		GZippedStream zs = new GZippedStream(fileInputStream);
		Util.writeTo(zs, fileOutStream);
		
		zs.close();
		fileInputStream.close();
//		
//		InputStreamReadWrapper gzipStream = new InputStreamReadWrapper(fileInputStream, true);
//		Util.writeTo(gzipStream, fileOutStream);
//		gzipStream.close();
		
		/*InputStreamReadWrapper insWrap = new InputStreamReadWrapper(zs.getGzippedStream());
    	while(insWrap.next()) {
    		fileOutStream.write(insWrap.getBytes());
    	}
    	insWrap.close();*/
    	
    	fileOutStream.close();
	}
	
	
}
