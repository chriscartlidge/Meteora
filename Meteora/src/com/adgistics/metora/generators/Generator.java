/**
 * 
 */
package com.adgistics.metora.generators;

import javax.xml.transform.Source;

/**
 * @author Christopher Cartlidge
 * @since 0.1
 * @version 0.1
 */
public interface Generator {
	

	/**
	 * 
	 * @param template
	 * @param content
	 * @return
	 */
	byte[] generate(Source template, Source content);
	
}
