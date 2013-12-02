package com.furious.meteora.generators;

import javax.xml.transform.TransformerFactory;

import org.apache.fop.apps.FopFactory;
import org.apache.fop.servlet.ServletContextURIResolver;

public final class GeneratorFactory {
	
	public static Generator newInstance(ServletContextURIResolver uriResolver,
			TransformerFactory transFactory, FopFactory fopFactory, Generators type) {
				
		Generator result = null;
		
		switch (type) {
		
			case IMAGE:
				result = new ImageGenerator(uriResolver, transFactory, fopFactory);
				break;
			
			case PDF:
				result = new PDFGenerator(uriResolver, transFactory, fopFactory);
				break;
				
			default:
				break;	
		}
		
		return result;
	}
}
