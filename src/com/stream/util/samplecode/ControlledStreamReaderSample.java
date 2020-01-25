
package com.stream.util.samplecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.stream.util.core.Util;
import com.stream.util.impl.InputStreamReadWrapper;


public class ControlledStreamReaderSample {

	
	public static void main(String[] args) throws IOException {
		
		
		File inputFile = new File("C:\\tmp\\Gzip\\4d7b90bba1c5fa0204fb7851c6c3af0500daf207e437");
		FileInputStream fileInputStream = new FileInputStream(inputFile);
		FileOutputStream file1OutStream = new FileOutputStream("C:\\tmp\\Gzip\\fileOutStream1.txt");
		FileOutputStream file2OutStream = new FileOutputStream("C:\\tmp\\Gzip\\fileOutStream2.txt");

		InputStreamReadWrapper insWrap = new InputStreamReadWrapper(fileInputStream, 1000L);
		Util.writeTo(insWrap, file1OutStream);
		file1OutStream.close();
		
		insWrap.setReadLimit(-1);
		Util.writeTo(insWrap, file2OutStream);
		file2OutStream.close();
		
		insWrap.close();
		file1OutStream.close();
		file2OutStream.close();
		
	}
	
}
