package com.furious.meteora.stores;

import java.io.InputStream;

public interface Storage {

	Boolean add(String fileName, byte[] content);
	
	InputStream get(String fileName);
}
