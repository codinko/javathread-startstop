package com.codinko.example;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 * 
 * 
 *         Aim of this program: To demonstrate the following:-
 * 
 *         1. WHY, When and How to use Threads in its simplest form? This
 *         program cannot be made without the use of multiple Threads.
 * 
 *         1. Run multiple threads at same time. Consider a start and stop
 *         button in a GUI. Start button starts a process & keep on running.
 *         Stop button ends that process. To achieve this, there must be a
 *         shared variable that the two threads work on.
 * 
 *         2. Use of volatile keyword for a shared variable. If this keyword is
 *         not present, then <code>while (RoboShield.getMonitor()!= -123</code>
 *         is usually cached by JVM and the value of monitor is not checked
 *         after sometime. But 'volatile' makes this to be checked when it is
 *         changed.
 * 
 *         3. You will also see why the first thread is run seperately and not
 *         in the existing UI thread. The UI thread means the one which displays
 *         the UI. If the first thread logic was as part of UI thread, then you
 *         cannot activate stop button, because UI remains unresponsive.
 * 
 * 
 * 
 */
public class Robo {

	public static void runRobo(int minutes, int pauseIntervalInSeconds)
			throws AWTException {
		// java.awt.Robot;
		Robot rob = new Robot();
		int timeCountInSeconds = 0;

		int actualminutesInSeconds = minutes * 60000;

		boolean movePointerFlag = false;
		if ("y".equalsIgnoreCase(RoboShield.movePointerFlag))
			movePointerFlag = true;
		while (RoboShield.getMonitor() != -123
				&& timeCountInSeconds < actualminutesInSeconds) {
			System.out.println("running...");
			rob.mouseMove(1000, 100); // arbitrary location
			rob.mousePress(InputEvent.BUTTON1_MASK); // left click
			rob.mouseRelease(InputEvent.BUTTON1_MASK);
			/*
			 * another mouse click to show that mouse is really! moving. Make
			 * this optional by reading the 'movePointerFlag'
			 */
			if (movePointerFlag) {
				rob.mouseMove(1100, 250);
				rob.mousePress(InputEvent.BUTTON1_MASK);
				rob.mouseRelease(InputEvent.BUTTON1_MASK);
			}
			/** Perform the mouse click after pauseIntervalInSeconds */
			try {
				Thread.sleep(pauseIntervalInSeconds * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			timeCountInSeconds = timeCountInSeconds + pauseIntervalInSeconds;
		}
		/* Other threads should stop execution after real-minutes. */
		RoboShield.setMonitor(-123);
	}
}
