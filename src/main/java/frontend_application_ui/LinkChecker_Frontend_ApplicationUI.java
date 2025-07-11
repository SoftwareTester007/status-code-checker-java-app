package frontend_application_ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import backendLogic.SeleniumLinkChecker;

public class LinkChecker_Frontend_ApplicationUI extends JFrame{

	//	private JTextField urlField;
	//	private JComboBox<String> categorySelector;
	//	private JButton findButton;
	//	private JTextArea resultArea;


	//		setTitle("HTTP Link Status Checker");
	//		setSize(700, 500);
	//		setDefaultCloseOperation(EXIT_ON_CLOSE);
	//		setLocationRelativeTo(null);
	//
	//		// Top Panel
	//		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	//		topPanel.add(new JLabel("Enter URL:"));
	//		urlField = new JTextField(30);
	//		topPanel.add(urlField);
	//
	//		String[] categories = {
	//				"Informational (100–199)",
	//				"Successful (200–299)",
	//				"Redirection (300–399)",
	//				"Client Error (400–499)",
	//				"Server Error (500–599)"};
	//		
	//		
	//		
	//		categorySelector = new JComboBox<>(categories);
	//	    topPanel.add(categorySelector);
	//
	//	    findButton = new JButton("Find");
	//	    topPanel.add(findButton);
	//
	//	    // Result Area
	//	    resultArea = new JTextArea();
	//	    resultArea.setEditable(false);
	//	    JScrollPane scrollPane = new JScrollPane(resultArea);
	//
	//	    add(topPanel, BorderLayout.NORTH);
	//	    add(scrollPane, BorderLayout.CENTER);
	//
	//	    findButton.addActionListener(e -> performLinkCheck());


	private JTextField urlField;
	private JComboBox<String> categorySelector;
	private JTextArea resultArea;

	public LinkChecker_Frontend_ApplicationUI() {
		
		setTitle("Status code Checker");
	
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel(new FlowLayout());
		urlField = new JTextField(40);
		JButton findButton = new JButton("Find");
		categorySelector = new JComboBox<>(new String[]{
				"Informational responses (100 – 199)",
				"Successful responses (200 – 299)",
				"Redirection messages (300 – 399)",
				"Client error responses (400 – 499)",
				"Server error responses (500 – 599)"
		});

		topPanel.add(new JLabel("URL:"));
		topPanel.add(urlField);
		topPanel.add(categorySelector);
		topPanel.add(findButton);

		resultArea = new JTextArea();
		resultArea.setEditable(false);
		resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		JScrollPane scrollPane = new JScrollPane(resultArea);

		add(topPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

		findButton.addActionListener(this::performLinkCheck);
	}




	private void performLinkCheck(ActionEvent event) {
		String url = urlField.getText().trim();
		String category = (String) categorySelector.getSelectedItem();

		if (url.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter a valid URL.", "Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		final int[] range = new int[2];
		Color backgroundColor;

		if (category.contains("200")) {
			range[0] = 200; range[1] = 299;
			backgroundColor = new Color(220, 255, 220);
		} else if (category.contains("300")) {
			range[0] = 300; range[1] = 399;
			backgroundColor = new Color(220, 235, 255);
		} else if (category.contains("400")) {
			range[0] = 400; range[1] = 499;
			backgroundColor = new Color(255, 230, 230);
		} else if (category.contains("500")) {
			range[0] = 500; range[1] = 599;
			backgroundColor = new Color(255, 255, 220);
		} else {
			range[0] = 100; range[1] = 199;
			backgroundColor = Color.WHITE;
		}

		resultArea.setBackground(backgroundColor);
		resultArea.setText("🔍 Scanning links for status: " + category + " on " + url + "...\n\n");

		new Thread(() -> {
			try {
				List<String> allLinks = SeleniumLinkChecker.getAllPageLinks(url);
				List<String> matchedLinks = new ArrayList<>();

				for (String link : allLinks) {
					int code = SeleniumLinkChecker.getHttpStatusCode(link);
					if (code >= range[0] && code <= range[1]) {
						matchedLinks.add(link + " → " + code);
					}
				}

				int total = matchedLinks.size();

				SwingUtilities.invokeLater(() -> {
					resultArea.append("✅ Total matching links: " + total + "\n");
					resultArea.append("⏳ Processing...\n\n");
				});

				int index = 1;
				for (String line : matchedLinks) {
					int code = Integer.parseInt(line.substring(line.lastIndexOf("→") + 1).trim());
					Color textColor;
					String icon;

					if (code >= 200 && code <= 299) {
						textColor = new Color(0, 128, 0);
						icon = "✅";
					} else if (code >= 300 && code <= 399) {
						textColor = new Color(0, 102, 204);
						icon = "🔁";
					} else if (code >= 400 && code <= 499) {
						textColor = new Color(204, 0, 0);
						icon = "❌";
					} else if (code >= 500 && code <= 599) {
						textColor = new Color(204, 153, 0);
						icon = "⚠️";
					} else {
						textColor = Color.GRAY;
						icon = "ℹ️";
					}

					final String output = index++ + ". " + icon + " " + line + "\n";
					SwingUtilities.invokeLater(() -> resultArea.append(output));
					Thread.sleep(200);
				}

				SwingUtilities.invokeLater(() -> {
					String updated = resultArea.getText().replace("⏳ Processing...\n\n", "");
					resultArea.setText(updated);
				});
			} catch (Exception ex) {
				SwingUtilities.invokeLater(() -> resultArea.append("❌ Error: " + ex.getMessage()));
			}
		}).start();





	}
}

























//	private void performLinkCheck() {
//	    String url = urlField.getText().trim();
//	    String category = (String) categorySelector.getSelectedItem();
//
//	    if (url.isEmpty()) {
//	        JOptionPane.showMessageDialog(this, "Please enter a valid URL.", "Input Error", JOptionPane.ERROR_MESSAGE);
//	        return;
//	    }
//
//	    final int[] range = new int[2];
//
//	    if (category.contains("200")) {
//	        range[0] = 200; range[1] = 299;
//	    } else if (category.contains("300")) {
//	        range[0] = 300; range[1] = 399;
//	    } else if (category.contains("400")) {
//	        range[0] = 400; range[1] = 499;
//	    } else if (category.contains("500")) {
//	        range[0] = 500; range[1] = 599;
//	    } else {
//	        range[0] = 100; range[1] = 199;
//	    }
//
//	    resultArea.setBackground(new Color(245, 250, 240)); // Optional: light background
//	    resultArea.setText("🔍 Scanning links for status: " + category + " on " + url + "...\n\n");
//
//	    new Thread(() -> {
//	        try {
//	            List<String> allLinks = SeleniumLinkChecker.getAllPageLinks(url);
//	            List<String> matchedLinks = new ArrayList<>();
//
//	            for (String link : allLinks) {
//	                int code = SeleniumLinkChecker.getHttpStatusCode(link);
//	                if (code >= range[0] && code <= range[1]) {
//	                    matchedLinks.add(link + " → " + code);
//	                }
//	            }
//
//	            int total = matchedLinks.size();
//
//	            // Add total count and "processing" line below existing text
//	            SwingUtilities.invokeLater(() -> {
//	                resultArea.append("✅ Total matching links: " + total + "\n");
//	                resultArea.append("⏳ Processing...\n\n");
//	            });
//
//	            int index = 1;
//	            for (String line : matchedLinks) {
//	                final String output = index++ + ". " + line + "\n";
//
//	                SwingUtilities.invokeLater(() -> resultArea.append(output));
//	                Thread.sleep(200); // Delay to simulate progressive printing
//	            }
//
//	            // Remove loader text after completion
//	            SwingUtilities.invokeLater(() -> {
//	                String updatedText = resultArea.getText().replace("⏳ Processing...\n\n", "");
//	                resultArea.setText(updatedText);
//	            });
//
//	        } catch (Exception ex) {
//	            SwingUtilities.invokeLater(() -> resultArea.append("❌ Error: " + ex.getMessage()));
//	        }
//	    }).start();
//	}











//	private void performLinkCheck() {
//	    String url = urlField.getText().trim();
//	    String category = (String) categorySelector.getSelectedItem();
//
//	    if (url.isEmpty()) {
//	        JOptionPane.showMessageDialog(this, "Please enter a valid URL.", "Input Error", JOptionPane.ERROR_MESSAGE);
//	        return;
//	    }
//
//	    int lower = 100, upper = 199;
//	    if (category.contains("200")) {
//	        lower = 200; upper = 299;
//	    } else if (category.contains("300")) {
//	        lower = 300; upper = 399;
//	    } else if (category.contains("400")) {
//	        lower = 400; upper = 499;
//	    } else if (category.contains("500")) {
//	        lower = 500; upper = 599;
//	    }
//
//	    resultArea.setText("🔍 Scanning links for status: " + category + " on " + url + "...\n\n");
//
//	    new Thread(() -> {
//	        try {
//	            java.util.List<String> results = SeleniumLinkChecker.findLinksWithRange(url, lower, upper);
//
//	            SwingUtilities.invokeLater(() -> {
//	                if (results.isEmpty()) {
//	                    resultArea.append("✅ No matching links found.\n");
//	                } else {
//	                    results.forEach(link -> resultArea.append("➤ " + link + "\n"));
//	                }
//	            });
//
//	        } catch (Exception ex) {
//	            SwingUtilities.invokeLater(() -> resultArea.append("❌ Error: " + ex.getMessage()));
//	        }
//	    }).start();
//	}

