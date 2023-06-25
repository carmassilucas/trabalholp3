package ifsp.edu.br.view;

import ifsp.edu.br.control.AutenticacaoControle;
import ifsp.edu.br.control.exception.AutenticacaoException;
import ifsp.edu.br.model.dto.AutenticacaoDto;
import ifsp.edu.br.model.vo.AdministradorVo;
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
    private final AutenticacaoControle autenticacaoControle;

    private LoginPanel() {
        autenticacaoControle = AutenticacaoControle.getInstancia();

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
        String perfil = Objects.requireNonNull(comboBoxPerfil.getSelectedItem()).toString();

        try {
            var autenticacaoRetorno = autenticacaoControle.autenticar(new AutenticacaoDto(
                    perfil,
                    textFieldUsuario.getText(),
                    new String(passwordFieldSenha.getPassword())
            ));

            if (autenticacaoRetorno == null) {
                JOptionPane.showMessageDialog(
                        this.panelConteudo,
                        "Usuário e/ou senha incorretos",
                        "Erro ao realizar login",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JPanel panelConteudoProximaPagina = redirecionarPagina(perfil, autenticacaoRetorno);
            GerenciadorDePaineis.getInstancia().setContentPane(panelConteudoProximaPagina);
        } catch (AutenticacaoException e) {
            JOptionPane.showMessageDialog(
                    this.panelConteudo,
                    e.getMessage(),
                    "Houve um engano",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private JPanel redirecionarPagina(String perfil, Object autenticacaoRetorno) {
        switch (perfil) {
            case "Cliente" -> {
                PesquisarMateriaisPanel pesquisarMateriaisPanel = PesquisarMateriaisPanel.getInstancia();
                pesquisarMateriaisPanel.setIdCliente(((ClienteVo) autenticacaoRetorno).getId());
                pesquisarMateriaisPanel.carregarComboBox();
                return pesquisarMateriaisPanel.getPanelConteudo();
            }
            case "Reciclagem" -> {
                GerenciarMateriaisPanel gerenciarMateriaisPanel = GerenciarMateriaisPanel.getInstancia();
                gerenciarMateriaisPanel.setIdReciclagem(((ReciclagemVo) autenticacaoRetorno).getId());
                gerenciarMateriaisPanel.carregarTabelas();
                return gerenciarMateriaisPanel.getPanelConteudo();
            }
            case "Administrador" -> {
                ManterMaterialPanel manterMaterialPanel = ManterMaterialPanel.getInstancia();
                manterMaterialPanel.setIdAdministrador(((AdministradorVo) autenticacaoRetorno).getId());
                return manterMaterialPanel.getPanelConteudo();
            }
        }
        return null;
    }
}
