
package com.stream.util.samplecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.stream.util.DataReader;
import com.stream.util.impl.InputStreamGzipReadWrapper;


public class InputStreamReadAndGzipSample {

	public static void main(String[] args) throws IOException {

		long StartTime = System.currentTimeMillis();

		File inputFile = new File("C:\\tmp\\Gzip\\4d7b90bba1c5fa0204fb7851c6c3af0500daf207e437");
		FileInputStream ffInputStream = new FileInputStream(inputFile);

		DataReader gzipDataReader = new GzipDataReaderImpl();
		InputStreamGzipReadWrapper dataInputStream = new InputStreamGzipReadWrapper(ffInputStream, gzipDataReader);

		while (dataInputStream.next()) {
			dataInputStream.getBytes();
		}

		dataInputStream.close();
		ffInputStream.close();

		System.out.println("Total Time : " + (System.currentTimeMillis() - StartTime));
	}

}


class GzipDataReaderImpl implements DataReader {

	
	
	
	public void readBytes(byte[] bytes, int off, int length) {
		try {
			ffOutStream.write(bytes, off, length);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
FileOutputStream ffOutStream;
	
	public GzipDataReaderImpl() {
		try {
			ffOutStream = new FileOutputStream("C:\\tmp\\Gzip\\ZippedNewwwwww.zip");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	

}