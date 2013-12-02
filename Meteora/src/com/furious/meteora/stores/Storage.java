package com.furious.meteora.stores;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public interface Storage {

	Boolean add(String fileName, byte[] content);
	
	InputStream get(String fileName);
}
