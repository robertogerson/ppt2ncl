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

import javax.swing.JOptionPane;

import br.pucrio.telemidia.ncl30.ppt2ncl.PPT2NCLFactory;

public class PPT2NCLGuiThread implements Runnable{

	private PPT2NCLFactory ppt2ncl;
	
	public PPT2NCLFactory getPpt2ncl() {
		return ppt2ncl;
	}
	public void setPpt2ncl(PPT2NCLFactory ppt2ncl) {
		this.ppt2ncl = ppt2ncl;
	}
	public void run() {
		// TODO Auto-generated method stub
		ppt2ncl.generateNCLFromPowerPoint();
		JOptionPane.showMessageDialog(null, "File " + ppt2ncl.getNclFileName()+ " was created sucessfully!", "PPT2NCL v.1.0 Information", -1);
		System.out.println("File " + ppt2ncl.getNclFileName()+ " was created successfully!");
	}

}
