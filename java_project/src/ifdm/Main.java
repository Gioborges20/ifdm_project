package ifdm;
import ifdm.view.JanelaPrincipal;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception ignored){}
        SwingUtilities.invokeLater(()->{ new JanelaPrincipal().setVisible(true); });
    }
}
