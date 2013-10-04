/******************************************************************************
This file is part of the tool to convert files in the PowerPoint™ format into 
files in the NCL (Nested Context Language) format.

Copyright: 2007-2013 PUC-Rio/Telemídia Laboratory, All Rights Reserved.

This program is free software; you can redistribute it and/or modify it under 
the terms of the GNU General Public License version 2 as published by
the Free Software Foundation.

This program is distributed in the hope that it will be useful, but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License version 2 for more 
details.

You should have received a copy of the GNU General Public License version 2
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA

For further information contact:
robertogerson@telemidia.puc-rio.br
bslima@telemidia.puc-rio.br
*******************************************************************************/

package br.pucrio.telemidia.ncl30.ppt2ncl;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * Responsible to manage and create NCL Elements.
 * 
 * @author Bruno Lima - bslima @ telemidia.puc-rio.br
 * @author Roberto Gerson - robertogerson @ telemidia.puc-rio.br
 *
 */
public class NCLElementBuilder {
	
	/* NCL DOM document */
	private Document ncl;
	/* NCL filename that will be created */
	private String nclFileName;
	/* Connector name to be used */
	private String connectorId = "onSelectionStopStart";
	/* NCL <body> element */
	private Element body;
	
	
	public NCLElementBuilder(String nclFileName){
		this.nclFileName = nclFileName;
	}
	
	/**
	 * Creates the NCL document from DOM.
	 */
	public boolean createNCLFile(){
		try {
			FileOutputStream foStream = new FileOutputStream(nclFileName);
			XMLSerializer serializer = new XMLSerializer(new PrintStream(foStream), new OutputFormat(ncl, "iso-8859-1", true));
			serializer.serialize(ncl);
			foStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Creates a media that will represents an slide in the initial ppt.
	 * 
	 * @param src - URI do Slide que foi gerado
	 * @param numMedia - qual o número do Slide
	 */
	public void createMedia(String src, int numMedia){
		
		/* Cria o elemento de mídia do Slide no documento NCL */
		Element media = ncl.createElement("media");
        media.setAttribute("id", "media"+numMedia);
        media.setAttribute("src", src);
        media.setAttribute("descriptor", "descriptorId");
        body.appendChild(media);
	}
	
	/**
	 * Creates a port that will refers to an specific slide (numMedia).
	 * 
	 * @param numMedia - qual numero do Slide na qual a porta será mapeada
	 */
	public void createPort(int numMedia){
		
		Element port = ncl.createElement("port");
		port.setAttribute("id", "portId");
		port.setAttribute("component", "media"+numMedia);
		body.appendChild(port);
	}
	
	/**
	 * Creates a link to emulate the slides transitions.
	 *
	 * @param numSlide - número do slide atual
	 * @param KeyValue - representa para qual lado o link irá apontar (esuqerda ou direita)
	 */
	public void createLink(int numSlide, String KeyValue){
		
    	/* Cria o elmento link */
		Element link1 = ncl.createElement("link");
		
		/* Verifica se o link vai ser da esquerda ou direita */
    	if(KeyValue.equals("CURSOR_LEFT"))
    		link1.setAttribute("id", ("link"+numSlide)+(numSlide-1));
    	else
    		link1.setAttribute("id", ("link"+(numSlide-1)+(numSlide)));
    	
    	link1.setAttribute("xconnector", connectorId);
    	
    	/* Cria os binds com os roles e actions respectivos */
    	Element bind1 = ncl.createElement("bind");
    	if(KeyValue.equals("CURSOR_LEFT"))
    		bind1.setAttribute("component", "media"+(numSlide));
    	else 
    		bind1.setAttribute("component", "media"+(numSlide-1));
    	bind1.setAttribute("role", "onSelection");
    	
    	Element bind2 = ncl.createElement("bind");
    	if(KeyValue.equals("CURSOR_LEFT"))
    		bind2.setAttribute("component", "media"+(numSlide));
    	else 
    		bind2.setAttribute("component", "media"+(numSlide-1));    		
    	bind2.setAttribute("role", "stop");
    	
    	Element bind3 = ncl.createElement("bind");
    	if(KeyValue.equals("CURSOR_LEFT"))
    		bind3.setAttribute("component", "media"+(numSlide-1));
    	else 			
    		bind3.setAttribute("component", "media"+(numSlide));
    	
    	bind3.setAttribute("role", "start");
    	
    	/* Creates the <bindParam> to the 'key' parameter */
    	Element bindParam1 = ncl.createElement("bindParam");
    	bindParam1.setAttribute("name", "key");
    	bindParam1.setAttribute("value", KeyValue);
    	
    	/* Builds the link structure */
    	body.appendChild(link1);
    	link1.appendChild(bind1);
    	link1.appendChild(bind2);
    	link1.appendChild(bind3);
    	bind1.appendChild(bindParam1);
    	
	} 
	
	/**
	 * Creates a DOM tree to NCL Document
	 */
	public boolean createDomNCL(){
		
		Element nclRoot;
		Element head;
		
		try {
			ncl = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		}
				
		/* Cria um Elemento <ncl> e seus filhos <head> e <body> */ 
		nclRoot = ncl.createElement("ncl");
		ncl.appendChild(nclRoot);
		head = ncl.createElement("head");
		body = ncl.createElement("body");
		
		
		/* Cria a base de conectores  e coloca no NCL */
		head.appendChild(createConnectorBase());
		
		/* Cria os efeitos de transição */
		head.appendChild(createTransitionBase());
		
		/*Cria a região que irá mostrar os Slides */
        head.appendChild(createRegionBase());
        
        /*Cria o descritor da media */
        head.appendChild(createDescriptorBase());
        
        nclRoot.appendChild(head);
        nclRoot.appendChild(body);
        
        return true;
        
	}
	
	/**
	 * Creates the <transitionBase> that will be used by NCL document.
	 * 
	 * @return transitionBase - o elemento representando o <transitionBase>
	 */
	private Element createTransitionBase(){
		// TODO: The transitions should be read from ppt.
		
		/* Creates a fadeIn transition with duration of 0.7s */
		Element transitionBase = ncl.createElement("transitionBase");
		Element transition = ncl.createElement("transition");
		transition.setAttribute("id", "transall");
		transition.setAttribute("type", "fade");
		transition.setAttribute("dur", "0.7s");
		transitionBase.appendChild(transition);
		
		return transitionBase;
	}
	
	/**
	 * Creates the <regionBase> that will be used by NCL document.
	 * 
	 * @return regionBase - o elemento representando <regionBase>
	 */
	private Element createRegionBase(){
		Element regionBase = ncl.createElement("regionBase");
		Element region = ncl.createElement("region");
        region.setAttribute("id", "regionId");
        region.setAttribute("height", "100%");
        region.setAttribute("width", "100%");
        regionBase.appendChild(region);
        
        return regionBase;
        
	}
	
	/**
	 * Creates the <descriptionBase> that will be used by NCL document
	 * 
	 * @return descriptorBase - o elemento representando o <descriptorBase>
	 */
	private Element createDescriptorBase(){
		
		/* Creates a descriptor that will refers to the region and to transitions. */
		Element descriptorBase = ncl.createElement("descriptorBase");
		Element descriptor = ncl.createElement("descriptor");
        descriptor.setAttribute("id", "descriptorId");
        descriptor.setAttribute("region", "regionId");
        descriptor.setAttribute("transOut", "transall");
        
        /* Creates the transition parameter */
        Element descriptorParam = ncl.createElement("descriptorParam");
        descriptorParam.setAttribute("name", "transitionOut");
        descriptorParam.setAttribute("value", "fade");
        descriptor.appendChild(descriptorParam);
        descriptorBase.appendChild(descriptor);
        
        return descriptorBase;
        
	}
	
	/**
	 * Creates the elements that will defines the connector. The connector will be
	 * from onSelectionStopStart.
	 * 
	 * @return connectorBase - The <connectorBase> element.
	 */
	private Element createConnectorBase(){
		
		Element connectorBase = ncl.createElement("connectorBase");
		
		Element causalConnector = ncl.createElement("causalConnector");
		causalConnector.setAttribute("id", connectorId);
		Element connectorParam = ncl.createElement("connectorParam");
		connectorParam.setAttribute("name", "key");
		Element simpleCondition = ncl.createElement("simpleCondition");
		simpleCondition.setAttribute("role", "onSelection");
		simpleCondition.setAttribute("key", "$key");
		Element compoundAction = ncl.createElement("compoundAction");
		compoundAction.setAttribute("operator", "seq");
		Element simpleAction1 = ncl.createElement("simpleAction");
		simpleAction1.setAttribute("role", "stop");
		Element simpleAction2 = ncl.createElement("simpleAction");
		simpleAction2.setAttribute("role", "start");
		
		/*
		 * Cria a estrutura do connector
		 */
		connectorBase.appendChild(causalConnector);
		causalConnector.appendChild(connectorParam);
		causalConnector.appendChild(simpleCondition);
		causalConnector.appendChild(compoundAction);
		compoundAction.appendChild(simpleAction1);
		compoundAction.appendChild(simpleAction2);
		
		return connectorBase;
	}
	
	public String getNclFileName() {
		return nclFileName;
	}

	public void setNclFileName(String nclFileName) {
		this.nclFileName = nclFileName;
	}
	

}
