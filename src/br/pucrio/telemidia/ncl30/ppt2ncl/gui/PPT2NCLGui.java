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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import br.pucrio.telemidia.ncl30.ppt2ncl.PPT2NCLFactory;

public class PPT2NCLGui extends JFrame {
	private JProgressBar jbar = new JProgressBar();
	private JMenuBar jmenu = new JMenuBar();
	private JFrameResolution jFrameResolution = new JFrameResolution();
	private Thumbnail thumbnail;
	private int widthPicture = 1200;
	private int heightPicture = 800;

	public int getWidthPicture() {
		return widthPicture;
	}

	public void setWidthPicture(int widthPicture) {
		this.widthPicture = widthPicture;
	}

	public int getHeightPicture() {
		return heightPicture;
	}

	public void setHeightPicture(int heightPicture) {
		this.heightPicture = heightPicture;
	}

	public PPT2NCLGui() {
		// TODO Auto-generated constructor stub
		createGui();
	}

	public void createGui() {
		setTitle(PPT2NCLConfiguration.getProperty("windowTitle"));
		getContentPane().setLayout(new BorderLayout());
		setSize(260, 300);
		thumbnail = new Thumbnail();
		// getting image in jar
		java.net.URL imgURL = getClass().getResource(
				PPT2NCLConfiguration.getProperty("ppt2nclLogo"));

		// insert the URL, rather than the address
		thumbnail.setImage(imgURL, 250, 215);
		getContentPane().add(thumbnail, BorderLayout.CENTER);

		addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent arg0) {
			}

			public void windowClosed(WindowEvent arg0) {
				System.exit(0);
			}

			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}

			public void windowDeactivated(WindowEvent arg0) {
			}

			public void windowDeiconified(WindowEvent arg0) {
			}

			public void windowIconified(WindowEvent arg0) {
			}

			public void windowOpened(WindowEvent arg0) {
			}

		});

		JMenu jmenuFile = new JMenu("File");
		JMenu jmenuEdit = new JMenu("Edit");
		JMenu jmenuHelp = new JMenu("Help");

		jmenu.add(jmenuFile);
		jmenu.add(jmenuEdit);
		jmenu.add(jmenuHelp);

		// Menu File
		JMenuItem jmenuFileNew = new JMenuItem("New");
		JMenuItem jmenuFileExit = new JMenuItem("Exit");
		jmenuFile.add(jmenuFileNew);
		jmenuFile.addSeparator();
		jmenuFile.add(jmenuFileExit);

		jmenuFileNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fchoose = new JFileChooser();
				File file = null;
				if (fchoose.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					file = fchoose.getSelectedFile();
					PPT2NCLFactory ppt2ncl = new PPT2NCLFactory(file
							.getAbsolutePath(), file.getParent() + "/main.ncl",
							widthPicture, heightPicture);
					PPT2NCLProcessObsever po = new PPT2NCLProcessObsever();
					// fchoose.setFileFilter(new
					// FileNameExtensionFilter("Power Point Presentation",
					// "ppt"));
					po.setJProgressBar(jbar);
					po.setThumbnail(thumbnail);

					ppt2ncl.addProcessObserver(po);
					ppt2ncl.setBasePath(file.getParentFile().getAbsolutePath());
					PPT2NCLGuiThread t2 = new PPT2NCLGuiThread();
					t2.setPpt2ncl(ppt2ncl);
					Thread t = new Thread(t2);
					t.start();
				}
			}
		});

		jmenuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		// Menu Edit
		JMenuItem jmenuEditResolution = new JMenuItem("Resolution");
		jmenuEditResolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jFrameResolution.setVisible(true);
			}
		});
		jmenuEdit.add(jmenuEditResolution);
		
		JMenuItem jmenuEditPreferences = new JMenuItem("Preferences");
		//TODO: Preferences
		jmenuEdit.addSeparator();
		jmenuEdit.add(jmenuEditPreferences);

		// create Window release notes
		String releaseNotesPath = PPT2NCLConfiguration
				.getProperty("releaseNotesPath");
		String aboutIcon = PPT2NCLConfiguration.getProperty("aboutIcon");
		final JFrame jFrame = new JFrame();
		jFrame.setBounds(100, 100, 400, 400);
		jFrame.setTitle(PPT2NCLConfiguration
				.getProperty("releaseNotesTitle"));
		
		JTextArea textArea = new JTextArea();

		JScrollPane areaScrollPane = new JScrollPane(textArea);
		areaScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(250, 250));

		try {
			// open README file and show its content
			InputStream is = getClass().getResourceAsStream(releaseNotesPath);

			StringBuffer sb = new StringBuffer();
			int chr;
			while ((chr = is.read()) != -1)
			{
				sb.append((char) chr);
			}
			is.close();
			textArea.setText(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jFrame.add(areaScrollPane);

		// Menu Help
		JMenuItem jmenuReadme = new JMenuItem("Release notes");
		jmenuReadme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jFrame.setVisible(true);
			}
		});
		jmenuHelp.add(jmenuReadme);

		jmenuHelp.addSeparator();

		JMenuItem jmenuHelpAbout = new JMenuItem("About");
		jmenuHelpAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String message = PPT2NCLConfiguration.getProperty("about");
				String aboutIcon = PPT2NCLConfiguration
						.getProperty("aboutIcon");
				java.net.URL aboutURL = getClass().getResource(aboutIcon);
				JOptionPane.showMessageDialog(null, message, "About",
						JOptionPane.INFORMATION_MESSAGE,
						new ImageIcon(aboutURL));
			}
		});
		jmenuHelp.add(jmenuHelpAbout);

		setJMenuBar(jmenu);

		jbar.setSize(300, 50);
		getContentPane().add(jbar, BorderLayout.SOUTH);

		jFrameResolution.setParent(this);
	}

	class JFrameResolution extends JFrame {
		JTextField jTextBoxWidth = new JTextField();
		JLabel jLabelWidth = new JLabel("Width:");
		JTextField jTextBoxHeight = new JTextField();
		JLabel jLabelHeight = new JLabel("Height:");
		JButton jButtonSave = new JButton("Save");
		JButton jButtonCancel = new JButton("Cancel");
		PPT2NCLGui parent = null;

		public JFrameResolution() {
			initialize();
		}

		public void initialize() {
			getContentPane().setLayout(new GridLayout(3, 2));
			setSize(260, 100);
			setTitle("Resolution");
			add(jLabelWidth);
			add(jTextBoxWidth);
			add(jLabelHeight);
			add(jTextBoxHeight);
			add(jButtonSave);
			add(jButtonCancel);

			jButtonSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (parent != null) {
						try {
							parent.setWidthPicture(new Integer(jTextBoxWidth
									.getText()));
							parent.setHeightPicture(new Integer(jTextBoxHeight
									.getText()));
						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(null,
									"Number format error.", "Error", -1);
							return;
						}
					}
					setVisible(false);
				}
			});

			jButtonCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);
				}
			});
		}

		public void setVisible(boolean b) {
			if (b) {
				jTextBoxHeight.setText((new Integer(parent.getHeightPicture())
						.toString()));
				jTextBoxWidth.setText((new Integer(parent.getWidthPicture())
						.toString()));
			}
			super.setVisible(b);
		}

		public void setParent(PPT2NCLGui jFrame) {
			this.parent = jFrame;
		}
	}

	public static void main(String[] args) {
		new PPT2NCLGui().setVisible(true);
	}

}
