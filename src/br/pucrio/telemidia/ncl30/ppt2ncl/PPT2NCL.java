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

public class PPT2NCL {

	public static void main(String args[]) {
		
		PPT2NCLFactory ppt2ncl = null;

		System.out.println("Running...");
		
		if (args.length < 1) {
			System.out.println("Usage: java -jar ncl30-pptConverter.jar PPTFileNAME.ppt [NCLFileName.ncl] [ Width Height ]");
			System.exit(0);
		} else if (args.length == 1){
			ppt2ncl = new PPT2NCLFactory(args[0], 1200, 800);
		} else if (args.length == 2){
			ppt2ncl = new PPT2NCLFactory(args[0],args[1], 1200, 800);
		} else if (args.length > 2){
			ppt2ncl = new PPT2NCLFactory(args[0],args[1],Integer.parseInt(args[2]),Integer.parseInt(args[3]));
		}
		
		ppt2ncl.generateNCLFromPowerPoint();
		System.out.println("File " + ppt2ncl.getNclFileName() + " created.");
		System.out.println("Finished!");	

	}

}
