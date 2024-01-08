package org.greatgamesonly.core.aurorasaver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.greatgamesonly.core.aurorasaver.GlobalConstants.CORE_PACKAGE_NAME;

@SpringBootApplication()
@EnableScheduling
@ComponentScan({
		CORE_PACKAGE_NAME,
		CORE_PACKAGE_NAME+".exception",
		CORE_PACKAGE_NAME+".controller",
		CORE_PACKAGE_NAME+".configuration",
		CORE_PACKAGE_NAME+".model.domain",
		CORE_PACKAGE_NAME+".model.repository",
		CORE_PACKAGE_NAME+".model.service",
		CORE_PACKAGE_NAME+".model.annotation",
		CORE_PACKAGE_NAME+".model.validation"
})
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		// Display confirmation dialog
		// Confirmation dialog to start the application
		int response = JOptionPane.showConfirmDialog(null, "Do you want to start the application?", "Confirmation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (response == JOptionPane.OK_OPTION) {
			// Start the Spring Boot application in a separate thread
			Thread springThread = new Thread(() -> SpringApplication.run(Application.class, args));
			springThread.start();

			// Create and display the stop dialog
			JFrame frame = new JFrame();
			JButton stopButton = new JButton("Stop Application");
			stopButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Stop the Spring Boot application and close the dialog
					System.exit(0);
				}
			});

			frame.setTitle("Aurora Autosaver 0.2.1");
			frame.add(stopButton);
			frame.setSize(500, 100);
			frame.setLocationRelativeTo(null); // Center the frame
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		} else {
			// If user clicks Cancel, exit the application
			System.exit(0);
		}
	}

	public static String getAppVersion() {
		try (InputStream input = Application.class.getClassLoader().getResourceAsStream("version.properties")) {
			Properties prop = new Properties();
			prop.load(input);
			return prop.getProperty("app.version");
		} catch (IOException ex) {
			logger.error(ex.getMessage());
			return null;
		}
	}

}
