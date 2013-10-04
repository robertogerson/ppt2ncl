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
package br.pucrio.telemidia.ncl30.ppt2ncl.tests;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Picture;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.record.Document;
import org.apache.poi.hslf.record.DocumentAtom;
import org.apache.poi.hslf.usermodel.PictureData;
import org.apache.poi.hslf.usermodel.SlideShow;

public class Animations {

	public static void main(String[] args) {
		SlideShow ppt;
		try {
			ppt = new SlideShow(new HSLFSlideShow("exemplos/A01introducao.ppt"));
			Slide[] slide = ppt.getSlides();
			Document doc = ppt.getDocumentRecord();
			DocumentAtom docAtom = doc.getDocumentAtom();
			docAtom.getRecordType();
			
			for (int i = 0; i < slide.length; i++) {
				// extract all pictures contained in the presentation
				PictureData[] pdata = slide[i].getSlideShow().getPictureData();
				for (int j = 0; j < pdata.length; j++) {
					PictureData pict = pdata[j];

					// picture data
					byte[] data = pict.getData();

					int type = pict.getType();
					String ext;
					switch (type) {
					case Picture.JPEG:
						ext = ".jpg";
						break;
					case Picture.PNG:
						ext = ".png";
						break;
					case Picture.WMF:
						ext = ".wmf";
						break;
					case Picture.EMF:
						ext = ".emf";
						break;
					case Picture.PICT:
						ext = ".pict";
						break;
					default:
						continue;
					}
					FileOutputStream out;
					try {
						out = new FileOutputStream("exemplos/tests/pict_" + i
								+ ext);
						out.write(data);
						out.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
