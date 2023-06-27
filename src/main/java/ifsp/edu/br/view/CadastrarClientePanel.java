package ifsp.edu.br.view;

import ifsp.edu.br.control.BuscarDadosCepControle;
import ifsp.edu.br.control.ClienteControle;
import ifsp.edu.br.control.exception.BuscarInformacoesCepException;
import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.model.dto.CadastrarClienteDto;
import ifsp.edu.br.model.dto.InformacoesCepDto;
import ifsp.edu.br.model.exception.EmailDuplicadoException;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;

public class CadastrarClientePanel {
    private JPanel panelConteudo;
    private JTextField textFieldNome;
    private JTextField textFieldEmail;
    private JTextField textFieldLogradouro;
    private JFormattedTextField formattedTextFieldCep;
    private JTextField textFieldNumero;
    private JButton buttonCadastrar;
    private JTextField textFieldBairro;
    private JTextField textFieldEstado;
    private JPasswordField passwordFieldSenha;
    private JTextField textFieldCidade;
    private JButton buttonBuscarDadosCep;
    private JLabel labelLogin;

    private static CadastrarClientePanel instancia;
    private final ClienteControle controle;
    private final BuscarDadosCepControle buscarDadosCepControle;

    private CadastrarClientePanel() {
        controle = ClienteControle.getInstancia();
        buscarDadosCepControle = BuscarDadosCepControle.getInstancia();

        formattedTextFieldCep.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                if (e.getKeyCode() == 10)
                    buscarInformacoesCep();
            }
        });

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

        buttonCadastrar.addActionListener(e -> cadastrarCliente());
        buttonBuscarDadosCep.addActionListener(e -> buscarInformacoesCep());
    }

    public static CadastrarClientePanel getInstancia() {
        if (instancia == null)
            instancia = new CadastrarClientePanel();
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

    public void cadastrarCliente() {
        try {
            controle.cadastrarCliente(new CadastrarClienteDto(
                textFieldNome.getText(),
                textFieldEmail.getText(),
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

            limparCamposCliente();
        } catch (CadastrarClienteException e) {
            JOptionPane.showMessageDialog(this.panelConteudo, e.getMessage(), "Erro ao cadastrar-se", JOptionPane.ERROR_MESSAGE);
        } catch (EmailDuplicadoException e) {
            JOptionPane.showMessageDialog(this.panelConteudo, e.getMessage(), "Erro ao cadastrar-se", JOptionPane.ERROR_MESSAGE);

            textFieldEmail.requestFocus();
            textFieldEmail.selectAll();
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
    }

    public void limparCamposCliente() {
        textFieldNome.setText("");
        textFieldEmail.setText("");
        passwordFieldSenha.setText("");
        limparCamposEndereco();
        textFieldNome.requestFocus();
    }
}
