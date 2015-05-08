package org.mpiwg.itgroup.escidoc;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class ESciDocException extends Exception{
	private static final long serialVersionUID = 798105291785018910L;
	
	private String message;
	private String title;
	private String stackTrace;
	private String cls;
	private boolean internalException = false;
	
	public ESciDocException(Element input){
		try{
			Element etitle = input.getChild("title");
			Element emessage = input.getChild("message");
			Element eclass = input.getChild("class");
			Element estackTrace = input.getChild("stack-trace");
			
			this.title = (etitle != null) ? etitle.getValue() : null;
			this.message = (emessage != null) ? emessage.getValue() : null;
			this.cls = (eclass != null) ? eclass.getValue() : null;
			this.stackTrace = (estackTrace != null) ? estackTrace.getValue() : null;	
		}catch (Exception e) {
			this.stackTrace = new XMLOutputter().outputString(input);
			this.internalException = true;
		}
	} 
	
	@Override
    public void printStackTrace() {
		printStackTrace(System.err);
		if(!internalException){
			String tmp = "Exception comming from sSciDoc Server: \n";
			tmp += title + "\n";
			tmp += message + "\n";
			tmp += cls + "\n";
			System.err.println(tmp + stackTrace);	
		}else{
			System.err.println(stackTrace);
		}
    }
	
	@Override
    public String getMessage() {
        return message;
    }
	
	public String getTitle(){
		return title;
	}
	
	public String getCls(){
		return cls;
	}
	
}
