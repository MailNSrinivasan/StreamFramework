
package com.stream.util.samplecode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.stream.util.DataLoader;
import com.stream.util.core.Util;
import com.stream.util.impl.InputStreamReadWrapper;


public class DataToStreamConvertorSample {

	public static void main(String[] args) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream("C:\\tmp\\Gzip\\data111.txt");
		
		
		//InputStreamReadWrapper insWrap = new InputStreamReadWrapper(gen2);
		// Just Read and Print 
		/* while(ins.next()) {
			//System.out.println(new String(insWrap.getBytes(), 0, insWrap.getReadByteLeng()));
			System.out.println(new String(ins.getBytes()));
		}*/
		
		
		/// Write to a File
		DataLoader dataLoader = new DataLoaderImpl();
		InputStreamReadWrapper ins = new InputStreamReadWrapper(dataLoader);
		Util.writeTo(ins, fileOutputStream);
		
		fileOutputStream.close();
		
		
		ins.close();
		
		
	}
}


class DataLoaderImpl implements DataLoader{

	public void load(OutputStream out) throws IOException {

		for (int i = 0; i < 1000; i++) {
			out.write(("Test data " + i + "\n").getBytes("UTF-8"));
		}
		out.close();

	}

}
