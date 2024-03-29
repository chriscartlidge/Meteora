package com.furious.meteora.servers;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FilenameUtils;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.servlet.ServletContextURIResolver;

import com.furious.meteora.generators.Generator;
import com.furious.meteora.generators.GeneratorFactory;
import com.furious.meteora.generators.Generators;
import com.furious.meteora.stores.DiskStorage;

/**
 * Servlet implementation class FopSever
 */
@WebServlet("/FopServer")
public class FopServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final String TEMPLATE_REQUEST_PARAM = "template";
    private final String CONTENT_REQUEST_PARAM = "content";
    private final String RENDER_TYPE_REQUEST_PARAM = "render";
    
    private ServletContextURIResolver uriResolver;
	private TransformerFactory transFactory;
	private FopFactory fopFactory;
	private String templateRoot;
	private String root;

       
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
        
        this.templateRoot = getServletContext().getRealPath("/templates");
        this.root = getServletContext().getRealPath("/");
        
        //Configure FopFactory as desired
        this.fopFactory = FopFactory.newInstance();
        try {
			this.fopFactory.setBaseURL(this.root);
		} catch (MalformedURLException e) {
			//TODO:
		}
    }
	
    /**
     * {@inheritDoc} 
     */
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException {
        try {
        	
        	String template = request.getParameter(TEMPLATE_REQUEST_PARAM);
        	String content = request.getParameter(CONTENT_REQUEST_PARAM);
        	String render = request.getParameter(RENDER_TYPE_REQUEST_PARAM);
        	
        	Source contentSource = new StreamSource(new StringReader(content.trim()));
        	Source templateSource = convertString2Source(template);
        	Generators renderType = Generators.valueOf(render.toUpperCase());
        	
        	Generator generator = GeneratorFactory.newInstance(
        			uriResolver, transFactory, fopFactory, renderType);
        	
        	byte[] result = generator.generate(templateSource, contentSource);
    
        	DiskStorage storage = new DiskStorage(this.root, "renders");
  
        	String filename = generateContentFileName(renderType, request);
        	
        	storage.add(filename, result);
        	
        	send(filename.getBytes(), response);
        
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
            src = new StreamSource(new File(FilenameUtils.concat(templateRoot, param)));
        }
        return src;
    }
    
    //TODO: Hack.. think of a better way.
    private String generateContentFileName(Generators renderType, HttpServletRequest request){
    	
    	String result = null;
    	
    	String sessionId = UUID.randomUUID().toString();
    	
    	if(renderType == Generators.IMAGE){
    		result = String.format("%s.%s", sessionId, "png");
    	}
    	
    	if (renderType == Generators.PDF){
    		result = String.format("%s.%s", sessionId, "pdf");
    	}
    	
    	return result;
    }
    
    private void send(byte[] content, HttpServletResponse response) throws IOException {
    	
        //Send the result back to the client
        response.setContentType("text/plain");
        response.setContentLength(content.length);
        response.getOutputStream().write(content);
        response.getOutputStream().flush();
    }
}
