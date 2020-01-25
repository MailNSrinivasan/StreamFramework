package com.stream.util;

import java.io.IOException;
import java.io.OutputStream;

public interface DataLoader {
	
	public void load(OutputStream out) throws IOException;
	//public boolean hasDataToLoad();
	
}
