package com.codinko.example;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class RoboShield extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JPanel panel2;
	private JTextField textField;
	private JLabel lblRunningStatus;
	private JComboBox comboBox;
	private static volatile int monitor = 0;
	public static String movePointerFlag = "n";
	private JTextField activityIntervalText;

	private static final int PAUSE_SECONDS_INTERVAL_DEFAULT_VALUE = 60;
	private static final int RUNNING_MINUTES_DEFAULT_VALUE = 5;

	public static int getMonitor() {
		return monitor;
	}

	public static void setMonitor(int monitorValue) {
		monitor = monitorValue;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RoboShield frame = new RoboShield();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RoboShield() {
		setTitle("Cdko Robo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));

		panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(null);

		textField = new JTextField();
		textField.setBounds(241, 27, 89, 20);
		panel.add(textField);
		textField.setColumns(10);

		JButton btnStart = new JButton("Start");
		btnStart.setBackground(Color.GREEN);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String comboBoxValue = comboBox.getSelectedItem().toString();
				String activityIntervalTextValue = activityIntervalText
						.getText();
				if (activityIntervalTextValue == null
						|| activityIntervalTextValue.isEmpty()
						|| activityIntervalTextValue == "") {
					activityIntervalTextValue = null;
				}
				movePointerFlag = comboBoxValue;
				/**
				 * Running Robo.runRobo(minutes), here will run the UI thread to
				 * the loop of minutes and you cannot do any other operation on
				 * UI [eg clicking on stop]. Hence run in a seperate thread.
				 */
				Runnable roboStartThread = new Runnable() {
					@Override
					public void run() {
						System.out.println("Thread running is : "
								+ Thread.currentThread().getName());
						int minutes = 0;
						monitor = 0;
						try {
							String textFieldValue = textField.getText();
							minutes = Integer.valueOf(textFieldValue);
						} catch (Exception ex) {
							minutes = RUNNING_MINUTES_DEFAULT_VALUE;
						}
						int pauseInterval = 0;
						try {
							pauseInterval = Integer
									.valueOf(activityIntervalText.getText());
						} catch (Exception ex) {
							pauseInterval = PAUSE_SECONDS_INTERVAL_DEFAULT_VALUE;
						}
						try {
							Robo.runRobo(minutes, pauseInterval);
						} catch (AWTException e) {
							e.printStackTrace();
						}
					}
				};

				new Thread(roboStartThread, "roboStartThread").start();

				/**
				 * Execute another logic in a diferent thread. This should
				 * constantly update the 'status-label' as running... so that
				 * the user knows something is running!!!. and the status
				 * displays stopped!! only when the shared variable(monitor)
				 * reads as -123.
				 */

				// -------------
				Runnable statusLabelUpdateThread = new Runnable() {
					@Override
					public void run() {
						int flag = 2;
						while (monitor != -123) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (flag % 2 == 0)
								lblRunningStatus.setText("running..");
							else
								lblRunningStatus.setText("running......");
							flag += 1;
						}
						lblRunningStatus.setText("Stopped!!");
						System.out.println("Stopped!!");
					}
				};
				new Thread(statusLabelUpdateThread, "statusLabelUpdateThread")
						.start();
				// -------------

			}
		});
		btnStart.setBounds(72, 124, 89, 23);
		panel.add(btnStart);
		;

		JButton btnStop = new JButton("Stop");
		btnStop.setBackground(Color.RED);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {/*
														 * 
														 * Runnable
														 * roboStopThread = new
														 * Runnable() {
														 * 
														 * @Override public void
														 * run() {
														 * System.out.println
														 * ("Thread running is : "
														 * +
														 * Thread.currentThread
														 * () .getName());
														 * monitor = -123; } };
														 * 
														 * new
														 * Thread(roboStopThread
														 * , "roboStopThread"
														 * ).start();
														 */
				monitor = -123;
			}
		});
		btnStop.setBounds(169, 124, 89, 23);
		panel.add(btnStop);

		JLabel lblTimeinMinutes = new JLabel("Time (in minutes):");
		lblTimeinMinutes.setBounds(72, 30, 123, 14);
		panel.add(lblTimeinMinutes);

		JLabel lblStatus = new JLabel("Current Status : ");
		lblStatus.setBounds(38, 173, 123, 14);
		panel.add(lblStatus);

		lblRunningStatus = new JLabel("");
		lblRunningStatus.setBackground(Color.GRAY);
		lblRunningStatus.setBounds(171, 173, 134, 14);
		panel.add(lblRunningStatus);

		JLabel lblNewLabel = new JLabel("Move Pointer (y/n) ?");
		lblNewLabel.setBounds(73, 55, 185, 14);
		panel.add(lblNewLabel);

		comboBox = new JComboBox();
		comboBox.setBounds(287, 52, 43, 20);
		comboBox.addItem("n");
		comboBox.addItem("y");
		panel.add(comboBox);

		JLabel lblNewLabel_1 = new JLabel("Activity Interval (in seconds) ? ");
		lblNewLabel_1.setBounds(72, 80, 186, 14);
		panel.add(lblNewLabel_1);

		activityIntervalText = new JTextField();
		activityIntervalText.setBounds(266, 77, 64, 20);
		panel.add(activityIntervalText);
		activityIntervalText.setColumns(10);

		JLabel lblLeaveAsBlank = new JLabel(
				"Leave as blank for default 1 minute interval");
		lblLeaveAsBlank.setBounds(120, 99, 294, 14);
		panel.add(lblLeaveAsBlank);

	}
}
