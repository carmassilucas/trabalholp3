package ifsp.edu.br.view;

import ifsp.edu.br.control.ClienteControle;
import ifsp.edu.br.control.ReciclagemControle;
import ifsp.edu.br.control.exception.LoginClienteException;
import ifsp.edu.br.control.exception.LoginReciclagemException;
import ifsp.edu.br.model.dto.LoginClienteDto;
import ifsp.edu.br.model.dto.LoginReciclagemDto;
import ifsp.edu.br.model.vo.ClienteVo;
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
    private JComboBox<String> comboBoxPerfil;
    private JLabel labelUsuario;

    private static LoginPanel instancia;
    private final ReciclagemControle reciclagemControle;
    private final ClienteControle clienteControle;

    private LoginPanel() {
        reciclagemControle = ReciclagemControle.getInstancia();
        clienteControle = ClienteControle.getInstancia();

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
                        CadastrarClientePanel.getInstancia().getPanelConteudo() :
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
        textFieldUsuario.setText("");
        passwordFieldSenha.setText("");
        return panelConteudo;
    }

    private void fazerLogin() {
        try {
            JPanel panelConteudoProximaPagina;
            if (Objects.equals(comboBoxPerfil.getSelectedItem(), "Cliente")) {
                ClienteVo clienteVo = clienteControle.loginCliente(new LoginClienteDto(
                        textFieldUsuario.getText(),
                        new String(passwordFieldSenha.getPassword())
                ));

                if (clienteVo == null) {
                    JOptionPane.showMessageDialog(
                            this.panelConteudo,
                            "E-mail e/ou senha incorretos",
                            "Erro ao realizar login",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                panelConteudoProximaPagina = PesquisarMateriaisPanel.getInstancia().getPanelConteudo();
                PesquisarMateriaisPanel.getInstancia().setIdUsuario(clienteVo.getId());
                PesquisarMateriaisPanel.getInstancia().carregarComboBox();
            } else {
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

                panelConteudoProximaPagina = GerenciarMateriaisPanel.getInstancia().getPanelConteudo();
                GerenciarMateriaisPanel.getInstancia().setIdReciclagem(reciclagemVo.getId());
                GerenciarMateriaisPanel.getInstancia().carregarTabelas();
            }
            GerenciadorDePaineis.getInstancia().setContentPane(panelConteudoProximaPagina);
        } catch (LoginReciclagemException | LoginClienteException e) {
            JOptionPane.showMessageDialog(this.panelConteudo, e.getMessage(), "Houve um engano", JOptionPane.ERROR_MESSAGE);
        }
    }
}
