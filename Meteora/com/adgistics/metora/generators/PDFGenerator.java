package com.adgistics.metora.generators;

import javax.xml.transform.TransformerFactory;

import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.servlet.ServletContextURIResolver;

public final class PDFGenerator extends AbstractGenerator {

	protected PDFGenerator(ServletContextURIResolver uriResolver,
			TransformerFactory transFactory, FopFactory fopFactory) {
		super(uriResolver, transFactory, fopFactory, MimeConstants.MIME_PDF);
		// Empty Block.
	}

}
