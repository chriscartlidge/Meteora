package com.adgistics.metora.generators;

import javax.xml.transform.TransformerFactory;

import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.servlet.ServletContextURIResolver;

public final class ImageGenerator extends AbstractGenerator {

	protected ImageGenerator(ServletContextURIResolver uriResolver,
			TransformerFactory transFactory, FopFactory fopFactory) {
		super(uriResolver, transFactory, fopFactory, MimeConstants.MIME_PNG);
		// Empty Block. 
	}
}
