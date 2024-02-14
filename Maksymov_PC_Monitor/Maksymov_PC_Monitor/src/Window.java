import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;


public class Window {
	
	private JFrame window;
	private JPanel mainPanel;
	private JButton startButton;
	private JLabel cpuLabel;
	private JLabel searchLabel;
	private JScrollBar baudRateScrollBar;
	private JComboBox<String> chooseRS232;
	private JProgressBar cpuLoadBar;
	
	public Window(int winWidth, int winHeight) {
		final short width = (short)winWidth, height = (short)winHeight;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				initWindow(width, height);
				eventHandler();
			}
		});
	}
	
	private void initWindow(short winWidth, short winHeight) {
		window = new JFrame("PC Monitorig gadget control");
		initMainPanel(winWidth, winHeight);
		window.getContentPane().add(mainPanel);
		window.setResizable(false);
		window.pack();
		initComponents();
		window.setMinimumSize(new Dimension(window.getBounds().width, window.getBounds().height));
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	private void initMainPanel(short winWidth, short winHeight) {
		mainPanel = new JPanel(/*new BorderLayout()*/ new FlowLayout());
		mainPanel.setPreferredSize(new Dimension(winWidth, winHeight));
		mainPanel.setMinimumSize(mainPanel.getPreferredSize());
		//mainPanel.add(westSubPanel, BorderLayout.WEST);
	}
	
	
	private void initComponents() {
		
		
		startButton = new JButton("Start");
		startButton.setPreferredSize(new Dimension(64, 32) );
		
		searchLabel = new JLabel();
		searchLabel.setPreferredSize(new Dimension(80, 32) );
		searchLabel.setBorder(BorderFactory.createTitledBorder("baud rate:"));
		searchLabel.setText("9600");
		
		cpuLabel = new JLabel();
		cpuLabel.setPreferredSize(new Dimension(96, 32) );
		cpuLabel.setBorder(BorderFactory.createTitledBorder("CPU Usage: "));
		cpuLabel.setText("cpu --%");
		
		baudRateScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 2);
		baudRateScrollBar.setPreferredSize(new Dimension(128, 24));
		baudRateScrollBar.setMinimum(0);
		baudRateScrollBar.setMaximum(3);
		baudRateScrollBar.setVisibleAmount(1);
		
		String[] ports = new String[9];
		for(int port = 0; port < ports.length; port++) ports[port] = "COM" + (port + 1);
		
		chooseRS232 = new JComboBox<String>(ports);
		chooseRS232.setSelectedIndex(ports.length - 1);
		
		cpuLoadBar = new JProgressBar(0,100);
		
		//cpuLoadBar.setBounds(70, 50, 120, 30); lauout = null;
		cpuLoadBar.setPreferredSize(new Dimension(128, 24));
		cpuLoadBar.setForeground(Color.RED);
		cpuLoadBar.setBackground(Color.GREEN);

		cpuLoadBar.setValue(0);
		
		
		mainPanel.add(chooseRS232);
		mainPanel.add(baudRateScrollBar);
		mainPanel.add(searchLabel);
		mainPanel.add(startButton);
		mainPanel.add(cpuLabel);
		mainPanel.add(cpuLoadBar);
		
	}
	
	private void eventHandler() {
		
		startOnClick();
		scroll();
		chooseCOM();
		
	}
	
	private void chooseCOM() {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	/*
                JComboBox box = (JComboBox)e.getSource();
                        String item = (String)box.getSelectedItem();
                        label.setText(item);
                */
            	VirtualRS232.nCOM = (String)chooseRS232.getSelectedItem();
            }
        };
	}
	
	private void startOnClick() {
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//try {
					if(Threads.updateThread == null)
						Threads.updateThread = new UpdateThread();
					Threads.updateThread.start();
					
					
					new Thread() {
						
						@Override
						public void run() {
							setPriority(Thread.MIN_PRIORITY);
							this.setName("CPU gui update thread");
							while(Threads.updateThread.isRunning()) {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										//if(Threads.updateThread != null)
										//while(Threads.updateThread.isRunning())
										//for(int i = 0; i < 8000000; i++)
										byte cpuLoad = (byte)Threads.updateThread.getCurrentCPUload();
											cpuLabel.setText( String.valueOf(/*Threads.updateThread.getCurrentCPUload()*/cpuLoad) + " %" );
											cpuLoadBar.setValue(cpuLoad);
											//JOptionPane.showMessageDialog(window , "It was not possible to save the image", "Error", JOptionPane.ERROR_MESSAGE);
									}
								});
								
								//Thread.yield();
								
								
								synchronized(this) {
									try {
										Thread.yield();
										wait(500);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
								
								synchronized(this) {
									notify();
								}
								
							}
						
							
						}
						
					}.start();
					
					if(Threads.updateThread.isRunning() == false)
						cpuLabel.setText("cpu _%");

			}
			
		});
	}
	
	private void scroll() {
		baudRateScrollBar.addAdjustmentListener(new AdjustmentListener() {
			
			public void adjustmentValueChanged(AdjustmentEvent event) {
	
				switch(baudRateScrollBar.getValue()) {
				case 0:
					searchLabel.setText("9600");
					VirtualRS232.baudRate = 9600;
					break;
				case 1:
					searchLabel.setText("19200");
					VirtualRS232.baudRate = 19200;
					break;
				case 2:
					searchLabel.setText("115200");
					VirtualRS232.baudRate = 115200;
				}
			}
			
		});
		
	}
	

}
