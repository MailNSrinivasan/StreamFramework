package com.stream.util.samplecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PrefTest2 {
	public static void main(String[] args) throws IOException, InterruptedException {
		File inputFile = new File("C:\\tmp\\4d7b90bba2c5fa0304fb77d6623e6a05087769405f9b");
		System.out.println("Start2 ..");
		
		
		//Thread.sleep(7000);
		
		long start = System.currentTimeMillis();
		FileInputStream fileInputStream = new FileInputStream(inputFile);
		int read;
		final byte[] data = new byte[1024*128];
		while ((read = fileInputStream.read(data)) != -1) {

		}
		
		System.out.println(" Time Taken to Read : "+(System.currentTimeMillis()-start));
		fileInputStream.close();
	}
}
