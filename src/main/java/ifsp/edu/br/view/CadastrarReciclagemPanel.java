package ifsp.edu.br.view;

import ifsp.edu.br.control.BuscarDadosCepControle;
import ifsp.edu.br.control.ReciclagemControle;
import ifsp.edu.br.control.exception.BuscarInformacoesCepException;
import ifsp.edu.br.control.exception.CadastrarReciclagemException;
import ifsp.edu.br.model.dto.CadastrarReciclagemDto;
import ifsp.edu.br.model.dto.InformacoesCepDto;
import ifsp.edu.br.model.exception.UsuarioDuplicadoException;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;

public class CadastrarReciclagemPanel {
    private JTextField textFieldNome;
    private JTextField textFieldUsuario;
    private JPasswordField passwordFieldSenha;
    private JFormattedTextField formattedTextFieldCep;
    private JTextField textFieldLogradouro;
    private JTextField textFieldNumero;
    private JTextField textFieldBairro;
    private JButton buttonBuscarDadosCep;
    private JTextField textFieldCidade;
    private JTextField textFieldEstado;
    private JPanel panelConteudo;
    private JButton buttonCadastrar;
    private JLabel labelLogin;

    private static CadastrarReciclagemPanel instancia;
    private final ReciclagemControle reciclagemControle;
    private final BuscarDadosCepControle buscarDadosCepControle;

    private CadastrarReciclagemPanel() {
        reciclagemControle = ReciclagemControle.getInstancia();
        buscarDadosCepControle =  BuscarDadosCepControle.getInstancia();

        formattedTextFieldCep.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                if (e.getKeyCode() == 10)
                    buscarInformacoesCep();
            }
        });

        buttonBuscarDadosCep.addActionListener(e -> buscarInformacoesCep());
        buttonCadastrar.addActionListener(e -> cadastrarReciclagem());
        labelLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                labelLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JPanel panelConteudoProximaPagina = LoginPanel.getInstancia().getPanelConteudo();
                GerenciadorDePaineis.getInstancia().setContentPane(panelConteudoProximaPagina);
            }
        });
    }

    public static CadastrarReciclagemPanel getInstancia() {
        if (instancia == null)
            instancia = new CadastrarReciclagemPanel();
        return instancia;
    }

    public JPanel getPanelConteudo() {
        return panelConteudo;
    }

    private void createUIComponents() {
        try {
            formattedTextFieldCep = new JFormattedTextField(new MaskFormatter("#####-###"));
        } catch (ParseException e) {
            System.err.println("Não foi possível aplicar a máscara no campo Cep, motivo do erro: " + e.getMessage());
        }
    }

    private void buscarInformacoesCep() {
        Object cep = formattedTextFieldCep.getValue();

        InformacoesCepDto informacoesCepDto;

        try {
            informacoesCepDto = buscarDadosCepControle.buscarInformacoesCep(cep);
        } catch (BuscarInformacoesCepException e) {
            JOptionPane.showMessageDialog(this.panelConteudo, e.getMessage(), "Houve um engano", JOptionPane.WARNING_MESSAGE);
            formattedTextFieldCep.requestFocus();
            return;
        }

        if (informacoesCepDto == null) {
            JOptionPane.showMessageDialog(
                    this.panelConteudo,
                    "Não conseguimos encontrar informações sobre seu cep, tente novamente",
                    "Cep não encontrado",
                    JOptionPane.ERROR_MESSAGE
            );
            limparCamposEndereco();
            return;
        }
        setDadosCepTextFields(informacoesCepDto);
    }

    private void cadastrarReciclagem() {
        try {
            reciclagemControle.cadastrarReciclagem(new CadastrarReciclagemDto(
                    textFieldNome.getText(),
                    textFieldUsuario.getText(),
                    new String(passwordFieldSenha.getPassword()),
                    textFieldCidade.getText(),
                    textFieldLogradouro.getText(),
                    textFieldBairro.getText(),
                    textFieldEstado.getText(),
                    textFieldNumero.getText(),
                    formattedTextFieldCep.getValue()
            ));

            JOptionPane.showMessageDialog(
                    this.panelConteudo,
                    "Cadastro realizado com sucesso",
                    "Cadastro realizado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limparCampos();
        } catch (CadastrarReciclagemException e) {
            JOptionPane.showMessageDialog(this.panelConteudo, e.getMessage(), "Erro ao cadastrar-se", JOptionPane.ERROR_MESSAGE);
        } catch (UsuarioDuplicadoException e) {
            JOptionPane.showMessageDialog(this.panelConteudo, e.getMessage(), "Erro ao cadastrar-se", JOptionPane.ERROR_MESSAGE);

            textFieldUsuario.requestFocus();
            textFieldUsuario.selectAll();
        }
    }

    public void setDadosCepTextFields(InformacoesCepDto informacoesCepDto) {
        limparCamposEndereco();
        formattedTextFieldCep.setText(informacoesCepDto.getCep());
        textFieldLogradouro.setText(informacoesCepDto.getLogradouro());
        textFieldEstado.setText(informacoesCepDto.getUf());
        textFieldBairro.setText(informacoesCepDto.getBairro());
        textFieldCidade.setText(informacoesCepDto.getLocalidade());
        textFieldNumero.setEditable(true);
        textFieldNumero.requestFocus();
    }

    public void limparCamposEndereco() {
        formattedTextFieldCep.setValue("");
        textFieldLogradouro.setText("");
        textFieldBairro.setText("");
        textFieldNumero.setText("");
        textFieldCidade.setText("");
        textFieldEstado.setText("");
        formattedTextFieldCep.requestFocus();
    }

    private void limparCampos() {
        textFieldNome.setText("");
        textFieldUsuario.setText("");
        passwordFieldSenha.setText("");
        limparCamposEndereco();
        textFieldNome.requestFocus();
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException ignored) { }

        JFrame frame = new JFrame("Cadastro de Reciclagem");
        frame.setContentPane(new CadastrarReciclagemPanel().panelConteudo);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
