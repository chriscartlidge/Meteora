/**
 * 
 */
package com.furious.meteora.generators;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.servlet.ServletContextURIResolver;

/**
 * 
 * @author Christopher Cartlidge
 *
 */
public abstract class AbstractGenerator implements Generator {
	
	private final ServletContextURIResolver uriResolver;
	private final TransformerFactory transFactory;
	private final FopFactory fopFactory;
	private final String renderType;

	protected AbstractGenerator(
			ServletContextURIResolver uriResolver,
			TransformerFactory transFactory, 
			FopFactory fopFactory, 
			String renderType) {
		this.uriResolver = uriResolver;
		this.transFactory = transFactory;
        this.fopFactory = fopFactory;
        this.renderType = renderType;
	}

	@Override
	public byte[] generate(Source template, Source content) {
		
		byte[] result = null;
		
		try{
			result = render(template, content);
		}
		catch(Exception e){
			//TODO: Error handling.
		}
		
		return result;
	}
	
    /**
     * Renders an input file (XML or XSL-FO) into a PDF file. It uses the JAXP
     * transformer given to optionally transform the input document to XSL-FO.
     * The transformer may be an identity transformer in which case the input
     * must already be XSL-FO. The PDF is written to a byte array that is
     * returned as the method's result.
     * @param src Input XML or XSL-FO
     * @param transformer Transformer to use for optional transformation
     * @param response HTTP response object
     * @throws FOPException If an error occurs during the rendering of the
     * XSL-FO
     * @throws TransformerException If an error occurs during XSL
     * transformation
     * @throws IOException In case of an I/O problem
     */
    private byte[] render(Source template, Source content)
                throws FOPException, TransformerException, IOException {
    	
        //Setup the identity transformation
        Transformer transformer = this.transFactory.newTransformer(template);
        transformer.setURIResolver(this.uriResolver);
    	
        FOUserAgent foUserAgent = getFOUserAgent();

        //Setup output
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        //Setup FOP
        Fop fop = fopFactory.newFop(this.renderType, foUserAgent, out);

        //Make sure the XSL transformation's result is piped through to FOP
        SAXResult res = new SAXResult(fop.getDefaultHandler());
       
        //Start the transformation and rendering process
        transformer.transform(content, res);

        //Return the result
        return out.toByteArray();
    }

	private FOUserAgent getFOUserAgent() {
        FOUserAgent userAgent = fopFactory.newFOUserAgent();
        return userAgent;
    } 
}
