package GenerateImagewithAPI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class imagegenerate {
    private JFrame frame;
    private JTextField textField;
    private JLabel imageLabel;

    public imagegenerate() {
        frame = new JFrame("Text to Image Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        textField = new JTextField(30);
        JButton generateButton = new JButton("Generate Image");
        imageLabel = new JLabel();

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                generateImage(text);
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter Text:"));
        panel.add(textField);
        panel.add(generateButton);

        frame.getContentPane().add(BorderLayout.NORTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, imageLabel);

        frame.setVisible(true);
    }

    private void generateImage(String text) {
        try {
            String accessKey = "MPQ2DZQ21Px9Ql-JOjUtREnIDYf-ZvswWaHhPbvJYNk";
            String query = URLEncoder.encode(text, "UTF-8");
            String apiUrl = "https://api.unsplash.com/search/photos?query=" + query + "&client_id=" + accessKey;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String json = response.toString();
            int index = json.indexOf("\"regular\":\"");
            if (index != -1) {
                int start = index + 10;
                int end = json.indexOf("\"", start);
                String imageUrl = json.substring(start, end).replace("\\u0026", "&");

                ImageIcon icon = new ImageIcon(new URL(imageUrl));
                Image scaledImage = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                System.out.println("No image found.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching image: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new imagegenerate();
            }
        });
    }
}
