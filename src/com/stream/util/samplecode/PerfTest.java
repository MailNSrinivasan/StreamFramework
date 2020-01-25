

package com.stream.util.samplecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.stream.util.impl.InputStreamReadWrapper;


public class PerfTest {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		// Read 500 Mb of file using general Stream Process 
		// Read the same 500 Mb of file using InputStreamReadWrapper  
		
		//Thread.sleep(7000);
		
		File inputFile = new File("C:\\tmp\\4d7b90bba2c5fa0304fb77d6623e6a05087769405f9b");
		FileInputStream fileInputStream = new FileInputStream(inputFile);
		
		System.out.println("Start ..");
		long start = System.currentTimeMillis();
		
		InputStreamReadWrapper insWrap = new InputStreamReadWrapper(fileInputStream);
		
		while(insWrap.next()) {
			insWrap.getBytes();
		}
		
		System.out.println(" Time Taken to Read : "+(System.currentTimeMillis()-start));
		
		insWrap.close();
		fileInputStream.close();
		
		
		
	}
}
