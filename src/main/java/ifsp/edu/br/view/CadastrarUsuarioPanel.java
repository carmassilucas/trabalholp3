package ifsp.edu.br.view;

import ifsp.edu.br.control.CadastrarUsuarioControle;
import ifsp.edu.br.control.exception.BuscarInformacoesCepException;
import ifsp.edu.br.control.exception.CadastrarClienteException;
import ifsp.edu.br.model.dto.ClienteDto;
import ifsp.edu.br.model.dto.InformacoesCepDto;
import ifsp.edu.br.model.exception.EmailDuplicadoException;
import ifsp.edu.br.model.util.MessageDigestUtil;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Arrays;

public class CadastrarUsuarioPanel {
    private JPanel panelConteudo;
    private JTextField textFieldNome;
    private JTextField textFieldEmail;
    private JTextField textFieldLogradouro;
    private JFormattedTextField formattedTextFieldCep;
    private JTextField textFieldLocalidade;
    private JLabel labelNome;
    private JLabel labelEmail;
    private JLabel labelLogradouro;
    private JLabel labelCep;
    private JLabel labelCidade;
    private JButton buttonCadastrar;
    private JTextField textFieldBairro;
    private JLabel labelBairro;
    private JLabel labelEstado;
    private JTextField textFieldUf;
    private JLabel labelSenha;
    private JPasswordField passwordFieldSenha;

    private final CadastrarUsuarioControle controle;

    public CadastrarUsuarioPanel() {
        controle = CadastrarUsuarioControle.getInstancia();

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
            informacoesCepDto = controle.buscarInformacoesCep(cep);
        } catch (BuscarInformacoesCepException e) {
            JOptionPane.showMessageDialog(this.panelConteudo, e.getMessage(), "Houve um engano", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (informacoesCepDto == null) {
            setTextContextoCepTextFields(new InformacoesCepDto());
            setEnabledContextoCepTextFields(true);
            setEditableContextoCepTextFields(true);

            JOptionPane.showMessageDialog(
                    this.panelConteudo,
                    "Não conseguimos encontrar informações sobre seu cep. Fique a vontade para tentar novamente ou inserir manualmente",
                    "Cep não encontrado",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        setTextContextoCepTextFields(informacoesCepDto);
        setEnabledContextoCepTextFields(true);
        setEditableContextoCepTextFields(false);
    }

    public void cadastrarCliente() {
        try {
            Integer statusCadastro = controle.cadastrarCliente(new ClienteDto(
                    textFieldNome.getText(),
                    textFieldEmail.getText(),
                    MessageDigestUtil.hashSenha(Arrays.toString(passwordFieldSenha.getPassword())),
                    textFieldLocalidade.getText(),
                    textFieldLogradouro.getText(),
                    textFieldBairro.getText(),
                    textFieldUf.getText(),
                    formattedTextFieldCep.getValue()
            ));

            if (statusCadastro != 1) {
                JOptionPane.showMessageDialog(
                        this.panelConteudo,
                        "Erro inesperado ao se cadastrar, tente novamente",
                        "Erro ao cadastrar-se",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            JOptionPane.showMessageDialog(
                    this.panelConteudo,
                    "Cadastro realizado com sucesso",
                    "Cadastro realizado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limparCampos();
            setEnabledContextoCepTextFields(false);
            setEditableContextoCepTextFields(false);
        } catch (CadastrarClienteException e) {
            JOptionPane.showMessageDialog(this.panelConteudo, e.getMessage(), "Erro ao cadastrar-se", JOptionPane.ERROR_MESSAGE);
        } catch (EmailDuplicadoException e) {
            JOptionPane.showMessageDialog(this.panelConteudo, e.getMessage(), "Erro ao cadastrar-se", JOptionPane.ERROR_MESSAGE);

            textFieldEmail.requestFocus();
            textFieldEmail.selectAll();
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this.panelConteudo, e.getMessage(), "Erro ao aplicar proteção hash à senha", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setTextContextoCepTextFields(InformacoesCepDto informacoesCepDto) {
        textFieldLogradouro.setText(informacoesCepDto.getLogradouro());
        textFieldUf.setText(informacoesCepDto.getUf());
        textFieldBairro.setText(informacoesCepDto.getBairro());
        textFieldLocalidade.setText(informacoesCepDto.getLocalidade());
    }

    public void setEnabledContextoCepTextFields(Boolean status) {
        textFieldLogradouro.setEnabled(status);
        textFieldUf.setEnabled(status);
        textFieldBairro.setEnabled(status);
        textFieldLocalidade.setEnabled(status);
    }

    public void setEditableContextoCepTextFields(Boolean status) {
        textFieldLogradouro.setEditable(status);
        textFieldUf.setEditable(status);
        textFieldBairro.setEditable(status);
        textFieldLocalidade.setEditable(status);
    }

    public void limparCampos() {
        textFieldNome.setText("");
        textFieldEmail.setText("");
        passwordFieldSenha.setText("");
        textFieldLogradouro.setText("");
        textFieldUf.setText("");
        textFieldBairro.setText("");
        textFieldLocalidade.setText("");
        formattedTextFieldCep.setValue("");

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
