package com.adgistics.metora.fop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.servlet.ServletContextURIResolver;

import com.adgistics.metora.generators.Generator;
import com.adgistics.metora.generators.GeneratorFactory;
import com.adgistics.metora.generators.Generators;

/**
 * Servlet implementation class FopSever
 */
@WebServlet("/FopSever")
public class FopServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final String TEMPLATE_REQUEST_PARAM = "template";
    private final String CONTENT_REQUEST_PARAM = "content";
    
    private ServletContextURIResolver uriResolver;
	private TransformerFactory transFactory;
	private FopFactory fopFactory;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FopServer() {
        super();
        // TODO Auto-generated constructor stub
    }

	public void init() throws ServletException {
 
        this.uriResolver = new ServletContextURIResolver(getServletContext());
        this.transFactory = TransformerFactory.newInstance();
        this.transFactory.setURIResolver(this.uriResolver);
        
        //Configure FopFactory as desired
        this.fopFactory = FopFactory.newInstance();
    }
	
    /**
     * {@inheritDoc} 
     */
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException {
        try {
        	
        	String template = request.getParameter(TEMPLATE_REQUEST_PARAM);
        	String content = request.getParameter(CONTENT_REQUEST_PARAM);
        	
        	Source contentSource = new StreamSource(new StringReader(content.trim()));
        	Source templateSource = convertString2Source(template);
        	
        	GeneratorFactory.newInstance(uriResolver, transFactory, fopFactory, Generators.IMAGE);
        	
        
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
        
    }
    
    
    /**
     * Converts a String parameter to a JAXP Source object.
     * @param param a String parameter
     * @return Source the generated Source object
     */
    private Source convertString2Source(String param) {
        Source src;
        try {
            src = uriResolver.resolve(param, null);
        } catch (TransformerException e) {
            src = null;
        }
        if (src == null) {
            src = new StreamSource(new File(param));
        }
        return src;
    }
    
    private void send(byte[] content, HttpServletResponse response) throws IOException {
    	
        //Send the result back to the client
        response.setContentType("image/png");
        response.setContentLength(content.length);
        response.getOutputStream().write(content);
        response.getOutputStream().flush();
    }
}
