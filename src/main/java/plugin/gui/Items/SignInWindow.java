package plugin.gui.Items;

import plugin.core.rest.Sender;
import plugin.gui.KotoedPlugin;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.tree.DefaultMutableTreeNode;

import static plugin.gui.Utils.Strings.*;

public class SignInWindow extends JDialog {

    private JPanel contentPane;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JButton cancelButton;
    private KotoedPlugin kotoedPlugin;

    public SignInWindow(KotoedPlugin kotoedPlugin) {
        this.kotoedPlugin = kotoedPlugin;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(signInButton);

        signInButton.addActionListener(e -> onSignIn());
        cancelButton.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e ->
                        onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setTitle(SIGN_IN);

        PromptSupport.setPrompt(USERNAME_TEXT, usernameField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, usernameField);

        PromptSupport.setPrompt(PASSWORD_TEXT, passwordField);
        PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, passwordField);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void onSignIn() {
        /*String denizen = usernameField.getText();
        String password = passwordField.getText();
        Sender sender = new Sender("LOCAL");
        String cookie = sender.signIn(denizen, password);
        this.kotoedPlugin.LoadTree(new DefaultMutableTreeNode());
        dispose();*/
        if (usernameField.getText().equals(CREDENTIALS))
            if (passwordField.getText().equals(CREDENTIALS)) {
                this.kotoedPlugin.LoadTree(new DefaultMutableTreeNode());
                dispose();
                return;
            }
        JOptionPane.showMessageDialog(null,
                AUTH_ERROR_MESSAGE,
                AUTH_ERROR,
                JOptionPane.ERROR_MESSAGE);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
