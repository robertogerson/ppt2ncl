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
package br.pucrio.telemidia.ncl30.ppt2ncl.gui;

import javax.swing.JProgressBar;

import br.pucrio.telemidia.ncl30.ppt2ncl.PPT2NCLFactory;

public class PPT2NCLProcessObsever {
	JProgressBar jProgressBar;
	private Thumbnail thumbnail;

	public Thumbnail getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Thumbnail thumbnail) {
		this.thumbnail = thumbnail;
	}

	private String lastImagePath = "";

	public void update(PPT2NCLFactory ppt2ncl) {
		if (ppt2ncl.getCurrentSlide() == -1) {
			if (jProgressBar != null) {
				jProgressBar.setString("Working...");
				jProgressBar.setMaximum(ppt2ncl.getNumSlides());
				jProgressBar.setValue(0);
				thumbnail.setImage("ppt2ncl.png", 250, 215);
				System.out.println(ppt2ncl.getNumSlides());
			}
			System.out.println("Begin ncl creation...");
			java.net.URL imgURL = getClass().getResource(PPT2NCLConfiguration.getProperty("ppt2nclLogo"));
	         // insert the URL, rather than the address
			thumbnail.setImage(imgURL, 250, 215);
		} else {
			if (jProgressBar != null) {
				jProgressBar.setValue(ppt2ncl.getCurrentSlide());
				jProgressBar.setString("Creating slide " + ppt2ncl.getCurrentSlide()
						+ "/" + ppt2ncl.getNumSlides());
			}
			if (ppt2ncl.getCurrentSlide() > 1) {
				System.out.println(ppt2ncl.getCurrentMediaPath());
				thumbnail.setImage(ppt2ncl.getCurrentMediaPath(), 250, 215);
			}else{
				lastImagePath = ppt2ncl.getCurrentMediaPath();
			}
			System.out.println("Creating slide " + ppt2ncl.getCurrentSlide()
					+ "/" + ppt2ncl.getNumSlides());
		}
	}

	public JProgressBar getJProgressBar() {
		return jProgressBar;
	}

	public void setJProgressBar(JProgressBar progressBar) {
		jProgressBar = progressBar;
	}
}
