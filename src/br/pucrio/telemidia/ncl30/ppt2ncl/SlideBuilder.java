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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;


/**
 * Class that will read the PPT file format and, when asked,
 * will create the Slides in PNG format. 
 * 
 * @author Bruno Lima - bslima @ telemidia.puc-rio.br
 * @author Roberto Gerson - robertogerson @ telemidia.puc-rio.br
 */
public class SlideBuilder {
	
	/* The final slide show resolution */
	private Dimension resolution;
	/* Input filename */
	private String powerPointFileName;
	private String mediaPath;
	
	public String getMediaPath() {
		return mediaPath;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public SlideBuilder(Dimension resolution){
		this.resolution = resolution;
	}
	
	public SlideBuilder(int width, int height){
		this.resolution = new Dimension(width, height);
	}
	
	public SlideBuilder(String powerPointFileName, int width, int height){
		this.powerPointFileName = powerPointFileName;
		this.resolution = new Dimension(width, height);
	}

	/**
	 * Creates an slideshow from current PPT file.
	 * @return
	 */
	public SlideShow createSlideShow(){
		
		SlideShow ppt = null;
		try {
			ppt = new SlideShow(new HSLFSlideShow(powerPointFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return ppt;
	}
	
	/**
	 * Creates a printscreen from the slide.
	 * 
	 * @param slide
	 * @param pgsize
	 * @return src - O caminho para aonde a imagem PNG foi criada
	 * @throws IOException
	 */
	public String createResource(Slide slide, Dimension pgsize){

	        /* Creates a canvas to render the slide */
			BufferedImage img = new BufferedImage(resolution.width, resolution.height, BufferedImage.TYPE_INT_RGB);
	        Graphics2D graphics = img.createGraphics();
	        
	        /* clear the area that will be drawn */
            graphics.setPaint(Color.white);
            graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
            
            /* Resize the image to the chosen resolution */
            graphics.scale((double)resolution.width/pgsize.width, (double)resolution.height/pgsize.height);
            
            /* Render the slide over the canvas */
            slide.draw(graphics);
            
            /* Save the image */
            String src = mediaPath+"/slide_" +slide.getSlideNumber()+".png";
            FileOutputStream out = null;
			try {
				 out = new FileOutputStream(src);
				 javax.imageio.ImageIO.write(img, "png", out);
		         out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
           
            
	       return "slide_" +slide.getSlideNumber()+".png";
	}
	
	public Dimension getResolution() {
		return resolution;
	}


	public void setResolution(Dimension resolution) {
		this.resolution = resolution;
	}

}
