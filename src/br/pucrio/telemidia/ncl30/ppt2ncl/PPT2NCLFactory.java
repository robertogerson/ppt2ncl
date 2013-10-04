/******************************************************************************
This file is part of the tool to convert files in the PowerPoint™ format into 
files in the NCL (Nested Context Language) format

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
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;

import br.pucrio.telemidia.ncl30.ppt2ncl.gui.PPT2NCLProcessObsever;

/**
 * 
 * Opens an PowerPoint file and convert it to an NCL SlideShow.
 * 
 * @author Bruno Lima - bslima @ telemidia.puc-rio.br
 * @author Roberto Gerson - robertogerson @ telemidia.puc-rio.br
 *
 */
public class PPT2NCLFactory {
	
	private NCLElementBuilder nclBuilder;
	private SlideBuilder slideBuilder;
	private String basePath = "media";
	private Collection<PPT2NCLProcessObsever> observers = new ArrayList<PPT2NCLProcessObsever>();
	private int numSlides = -1;
	private int currentSlide = -1;
	private String currentMediaPath = "";

	public int getNumSlides() {
		return numSlides;
	}

	public void setNumSlides(int numSlides) {
		this.numSlides = numSlides;
	}

	public int getCurrentSlide() {
		return currentSlide;
	}

	public void setCurrentSlide(int currentSlide) {
		this.currentSlide = currentSlide;
	}

	public String getCurrentMediaPath() {
		return currentMediaPath;
	}

	public void setCurrentMediaPath(String currentMediaPath) {
		this.currentMediaPath = currentMediaPath;
	}

	/**
	 * Creates a new PPT to NCL transformation (with an specific final resolution)
	 * 
	 * @param powerPointFileName
	 * @param nclFileName
	 * @param width
	 * @param height
	 */
	public PPT2NCLFactory(String powerPointFileName, String nclFileName, int width, int height) {
		
		slideBuilder = new SlideBuilder(powerPointFileName,width,height);
		
		nclBuilder = new NCLElementBuilder(nclFileName);
		
	}
	
	/**
	 * Creates a new PPT to NCL transformation (with an specific final resolution)
	 * 
	 * @param powerPointFileName
	 * @param width
	 * @param height
	 */
	public PPT2NCLFactory(String powerPointFileName, int width, int height) {
		
		slideBuilder = new SlideBuilder(powerPointFileName,width,height);
		
		nclBuilder = new NCLElementBuilder("out.ncl");
		
	}
	
	public boolean generateNCLFromPowerPoint(){
		
		if ( !nclBuilder.createDomNCL() )
			return false;
		
		createMediaFolder();
		
		if ( !createSlides() )
			return false;
		
		if ( !nclBuilder.createNCLFile())
			return false;
		
		return true;
	}
	
	/**
	 * Creates the Slides from and SlideShow ppt file.
	 */
	public boolean createSlides() {
		
		SlideShow slideShow = slideBuilder.createSlideShow();
		slideBuilder.setMediaPath(getBasePath()+"/media");
		
		if(slideShow == null)
			return false;
		
		/* Get the slides from PPT presentation */
		Slide[] slides = slideShow.getSlides();
		numSlides = slides.length;
        
		//Call observers
		Iterator<PPT2NCLProcessObsever> it = observers.iterator();
		while(it.hasNext()){
			PPT2NCLProcessObsever po = (PPT2NCLProcessObsever) it.next();
			po.update(this);
		}
		
	    for (int numSlide = 0; numSlide < slides.length; numSlide++){
	    	
	    	/* Creates an image from Slide */
	    	String src = slideBuilder.createResource(slides[numSlide], slideShow.getPageSize());	
	        currentMediaPath = getBasePath()+"/media/"+src;
	        currentSlide = numSlide+1;
	        
	    	if(src == null)
	    		return false;
	    	
	    	// Call observers
			it = observers.iterator();
			while(it.hasNext()){
				PPT2NCLProcessObsever po = (PPT2NCLProcessObsever) it.next();
				po.update(this);
			}
	    		
	        /* Create <media> element to represent the slide */
	    	nclBuilder.createMedia("media/"+src, numSlide);
	        
	        if( numSlide == 0 ){
	        	/* Cria a porta de entrada para o primeiro SLide */
	    		nclBuilder.createPort(numSlide);
	    	}
	        else{
	        	/* Cria os links para navegar pelos Slides */
	        	nclBuilder.createLink(numSlide, "CURSOR_LEFT");
	        	nclBuilder.createLink(numSlide, "CURSOR_RIGHT");
	        }
	        
	      }
	    currentSlide = -1;
	    
	    // Call observers
		it = observers.iterator();
		while(it.hasNext()){
			PPT2NCLProcessObsever po = (PPT2NCLProcessObsever) it.next();
			po.update(this);
		}
		
	    return true;
	}	
	
	public String getNclFileName(){
		return nclBuilder.getNclFileName();
	}
	
	private void createMediaFolder(){
		File d = new File(this.basePath+"/media");
		if(!d.exists() || !d.isDirectory())
			d.mkdir();
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getBasePath() {
		return basePath;
	}
	
	public void addProcessObserver(PPT2NCLProcessObsever observer){
		if(!observers.contains(observer))
			observers.add(observer);
	}
	
}
