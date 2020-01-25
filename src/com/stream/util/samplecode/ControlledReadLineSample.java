/**
 * 
 */
package com.stream.util.samplecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.stream.util.impl.InputStreamReadWrapper;


public class ControlledReadLineSample {

	
	public static void main(String[] args) throws IOException, NumberFormatException, NoSuchMethodException {
		File inputFF = new File("C:\\tmp\\Gzip\\test.txt");
		FileInputStream fileInputStream = new FileInputStream(inputFF);

		InputStreamReadWrapper insWrap = new InputStreamReadWrapper(fileInputStream);

		while (insWrap.readLine(50)) {
			String lineStr = new String(insWrap.getBytes(), "UTF-8");
			System.out.print(lineStr);
			// Check for desired data
			int contentLngIndex = lineStr.indexOf("Content-Length:");
			if (contentLngIndex > -1) {
				String lenStr = lineStr.substring(contentLngIndex + ("Content-Length:".length())).trim();
				int leng = Integer.parseInt(lenStr);
				// Set for the length to read
				insWrap.setReadLimit(leng);
				while (insWrap.next()) {
					System.out.print(new String(insWrap.getBytes(), "UTF-8"));
				}
				// Set the read limit to -1 to proceed further there
				insWrap.setReadLimit(-1);
			}
		}

		
	}
}
