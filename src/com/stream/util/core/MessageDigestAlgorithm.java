/**
 * 
 */
package com.stream.util.core;

public enum MessageDigestAlgorithm {

	
	MD5;

	public static void main(String[] args) {
		
		
		System.out.println(MessageDigestAlgorithm.valueOf("MD5") == MessageDigestAlgorithm.MD5);
	}
}


