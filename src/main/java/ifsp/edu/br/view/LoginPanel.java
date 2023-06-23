package ifsp.edu.br.view;

import ifsp.edu.br.control.ReciclagemControle;
import ifsp.edu.br.control.exception.LoginReciclagemException;
import ifsp.edu.br.model.dto.LoginReciclagemDto;
import ifsp.edu.br.model.vo.ReciclagemVo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class LoginPanel {
    private JPanel panelConteudo;
    private JTextField textFieldUsuario;
    private JPasswordField passwordFieldSenha;
    private JButton buttonEntrar;
    private JLabel labelCadastrar;
    private JComboBox comboBoxPerfil;
    private JLabel labelUsuario;

    private static LoginPanel instancia;
    private final ReciclagemControle reciclagemControle;

    private LoginPanel() {
        reciclagemControle = ReciclagemControle.getInstancia();

        labelCadastrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                labelCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                JPanel panelConteudoProximaPagina = Objects.equals(comboBoxPerfil.getSelectedItem(), "Cliente") ?
                        CadastrarUsuarioPanel.getInstancia().getPanelConteudo() :
                        CadastrarReciclagemPanel.getInstancia().getPanelConteudo();
                GerenciadorDePaineis.getInstancia().setContentPane(panelConteudoProximaPagina);
            }
        });

        buttonEntrar.addActionListener(e -> fazerLogin());

        comboBoxPerfil.addActionListener(e ->
            labelUsuario.setText(
                    Objects.equals(comboBoxPerfil.getSelectedItem(), "Cliente") ? "E-mail" : "Usuário"
            )
        );
    }

    public static LoginPanel getInstancia() {
        if (instancia == null)
            instancia = new LoginPanel();
        return instancia;
    }

    public JPanel getPanelConteudo() {
        return panelConteudo;
    }

    private void fazerLogin() {
        try {
            ReciclagemVo reciclagemVo = reciclagemControle.loginReciclagem(new LoginReciclagemDto(
                    textFieldUsuario.getText(),
                    new String(passwordFieldSenha.getPassword())
            ));

            if (reciclagemVo == null) {
                JOptionPane.showMessageDialog(
                        this.panelConteudo,
                        "Usuário e/ou senha incorretos",
                        "Erro ao realizar login",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JPanel panelConteudoProximaPagina;
            if (Objects.equals(comboBoxPerfil.getSelectedItem(), "Cliente")) {
                panelConteudoProximaPagina = CadastrarUsuarioPanel.getInstancia().getPanelConteudo();
            } else {
                panelConteudoProximaPagina = GerenciarMateriaisPanel.getInstancia().getPanelConteudo();
                GerenciarMateriaisPanel.getInstancia().setIdReciclagem(reciclagemVo.getId());
                GerenciarMateriaisPanel.getInstancia().carregarTabelas();
            }
            GerenciadorDePaineis.getInstancia().setContentPane(panelConteudoProximaPagina);
        } catch (LoginReciclagemException e) {
            JOptionPane.showMessageDialog(this.panelConteudo, e.getMessage(), "Houve um engano", JOptionPane.ERROR_MESSAGE);
        }
    }
}
