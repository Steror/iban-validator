package ibanvalidator;

import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.List;
import java.util.Map;

public class Panel {

    private final Validator validator;

    @Autowired
    public Panel(final Validator validator) {
        this.validator = validator;
    }

    public void createPanel() {

        JFrame panel = new JFrame("IBAN validator");
        // Enter IBAN label
        JLabel ibanLabel = new JLabel();
        ibanLabel.setText("Enter IBAN:");
        ibanLabel.setBounds(10, 10, 80, 100);
        // Textfield to enter IBAN
        JTextField textfield= new JTextField();
        textfield.setBounds(90, 50, 220, 30);
        // Empty label for result after button click
        JLabel resultLabel = new JLabel();
        resultLabel.setBounds(160, 40, 200, 100);
        // Validate button
        JButton validateButton = new JButton("Validate IBAN");
        validateButton.setBounds(120,110,140, 40);
        // Button to select file
        JButton fileButton = new JButton("Select file");
        fileButton.setBounds(120,160,140, 40);
        // Empty label for result after button click
        JLabel fileLabel = new JLabel();
        fileLabel.setBounds(50, 210, 200, 30);
        //add to frame
        panel.add(ibanLabel);
        panel.add(textfield);
        panel.add(resultLabel);
        panel.add(validateButton);
        panel.add(fileButton);
        panel.add(fileLabel);
        panel.setSize(350,300);
        panel.setLayout(null);
        panel.setVisible(true);
        panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Action listeners
        validateButton.addActionListener(arg0 -> {
            if (validator.isValidIban(textfield.getText())) {
                resultLabel.setText("IBAN is valid");
            }
            else {
                resultLabel.setText("IBAN is invalid");
            }
        });
        fileButton.addActionListener(arg0 -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "text files", "txt");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(panel);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose to open this file: " +
                        chooser.getSelectedFile().getAbsolutePath());

                FileIO fileIO = new FileIO(chooser.getSelectedFile().getAbsolutePath());    // Set path to file
                List<String> ibanList = fileIO.readIban();                                  // Read from file
                Map<String, Boolean> ibanResult = validator.isValidIban(ibanList);          // Validate list
                if (!fileIO.writeIban(ibanResult)) {                                        // Write to file
                    System.out.println("Writing failed");
                    fileLabel.setText("Writing failed");
                }
                fileLabel.setText("Result saved to " + chooser.getSelectedFile().getName().replace(".txt", ".out"));
            }
        });
    }
}
