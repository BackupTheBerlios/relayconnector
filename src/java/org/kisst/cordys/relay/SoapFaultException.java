package org.kisst.cordys.relay;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.kisst.cfg4j.Props;

import com.eibus.soap.BodyBlock;
import com.eibus.xml.nom.Node;


/** 
 * This Exception class is used to have more control of generated SOAP Fault messages.
 * 
 * When an error somewhere in the code happens, one could normally throw any kind of Exception.
 * This would be caught by the Cordys Framework and translated into a SOAP:Fault with 
 * the Exception Message and stack trace
 * If one would use this class or any of it's children classes instead, one has total control 
 * over the SOAP:Fault message created.
 * Also no stack trace is added to the details section. 
 * This is considered good practice, since Stack traces might show vulnerable information. 
 *
 */
public class SoapFaultException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final String faultcode;
	private final String faultstring;

	public SoapFaultException(String faultcode, String faultstring) {
		super(faultstring);
		this.faultcode=faultcode;
		this.faultstring=faultstring;
	}
	
	public SoapFaultException(String faultcode, String faultstring, Throwable e) {
		super(faultstring, e);
		this.faultcode=faultcode;
		this.faultstring=faultstring;
	}

	/** Creates the Cordys response XML, which should usually contain a SOAP:Fault
	 * @param response the respose BodyBlock 
	 * @param props 
	 */
	public void createResponse(BodyBlock response, Props props) {
		int node=response.createSOAPFault(faultcode,faultstring);
		int detailsNode=0;
		if (hasDetails()) {
			detailsNode=Node.createElement("details", node);
			fillDetails(detailsNode);
		}
		if (RelaySettings.showStacktrace.get(props)) {
			if (detailsNode==0)
				detailsNode=Node.createElement("details", node);
    		StringWriter sw = new StringWriter();
    		printStackTrace(new PrintWriter(sw));
    		String details= sw.toString();
   			Node.createTextElement("stacktrace", details, detailsNode);
   		}
	}
	
	protected boolean hasDetails()     { return false; }
	protected void    fillDetails(int node)    { }
	public String getFaultcode()   { return faultcode; } 
	public String getFaultstring() { return faultstring; } 
}	

