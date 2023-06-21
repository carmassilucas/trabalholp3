package ifsp.edu.br.view;

import ifsp.edu.br.control.BuscarDadosCepControle;
import ifsp.edu.br.control.CadastrarUsuarioControle;
import ifsp.edu.br.control.exception.BuscarInformacoesCepException;
import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.model.dto.CadastrarClienteDto;
import ifsp.edu.br.model.dto.InformacoesCepDto;
import ifsp.edu.br.model.exception.EmailDuplicadoException;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public class CadastrarUsuarioPanel {
    private JPanel panelConteudo;
    private JTextField textFieldNome;
    private JTextField textFieldEmail;
    private JTextField textFieldLogradouro;
    private JFormattedTextField formattedTextFieldCep;
    private JTextField textFieldNumero;
    private JLabel labelNome;
    private JLabel labelEmail;
    private JLabel labelLogradouro;
    private JLabel labelCep;
    private JLabel labelCidade;
    private JButton buttonCadastrar;
    private JTextField textFieldBairro;
    private JLabel labelNumero;
    private JLabel labelEstado;
    private JTextField textFieldEstado;
    private JLabel labelSenha;
    private JPasswordField passwordFieldSenha;
    private JLabel labelBairro;
    private JTextField textFieldCidade;
    private JButton buttonBuscarDadosCep;

    private final CadastrarUsuarioControle controle;
    private final BuscarDadosCepControle buscarDadosCepControle;

    public CadastrarUsuarioPanel() {
        controle = CadastrarUsuarioControle.getInstancia();
        buscarDadosCepControle = BuscarDadosCepControle.getInstancia();

        formattedTextFieldCep.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
            super.keyReleased(e);

            if (e.getKeyCode() == 10) {
                buscarInformacoesCep();
            }
            }
        });

        buttonCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarCliente();
            }
        });
        buttonBuscarDadosCep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarInformacoesCep();
            }
        });
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

        JFrame frame = new JFrame("Cadastro de Usuário");
        frame.setContentPane(new CadastrarUsuarioPanel().panelConteudo);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
