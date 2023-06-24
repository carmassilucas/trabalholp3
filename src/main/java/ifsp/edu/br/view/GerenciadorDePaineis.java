package ifsp.edu.br.view;

import javax.swing.*;
import java.awt.*;

public class GerenciadorDePaineis {
    private final JFrame frame;

    private static GerenciadorDePaineis instancia;
    private GerenciadorDePaineis() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException ignored) { }

        frame = new JFrame("Reciclaqui");
        frame.setContentPane(LoginPanel.getInstancia().getPanelConteudo());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(600, 400));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static GerenciadorDePaineis getInstancia() {
        if (instancia == null)
            instancia = new GerenciadorDePaineis();
        return instancia;
    }

    public void setContentPane(JPanel contentPane) {
        frame.setContentPane(contentPane);
        frame.revalidate();
        frame.transferFocus();
    }
}
