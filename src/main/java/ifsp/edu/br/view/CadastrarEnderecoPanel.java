package ifsp.edu.br.view;

import ifsp.edu.br.control.BuscarDadosCepControle;
import ifsp.edu.br.control.CadastrarEnderecoControle;
import ifsp.edu.br.control.exception.BuscarInformacoesCepException;
import ifsp.edu.br.control.exception.CadastrarEnderecoException;
import ifsp.edu.br.model.dto.CadastrarEnderecoDto;
import ifsp.edu.br.model.dto.InformacoesCepDto;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;

public class CadastrarEnderecoPanel {
    private JPanel panelConteudo;
    private JTextField textFieldLogradouro;
    private JLabel labelLogradouro;
    private JTextField textFieldNumero;
    private JTextField textFieldEstado;
    private JTextField textFieldCidade;
    private JFormattedTextField formattedTextFieldCep;
    private JButton buttonBuscarDadosCep;
    private JLabel labelCep;
    private JLabel labelNumero;
    private JLabel labelBairro;
    private JTextField textFieldBairro;
    private JLabel labelCidade;
    private JLabel labelEstado;
    private JButton buttonCadastrarEndereco;
    private BuscarDadosCepControle buscarInformacoesCepControle;
    private CadastrarEnderecoControle cadastrarEnderecoControle;

    public CadastrarEnderecoPanel() {
        buscarInformacoesCepControle = BuscarDadosCepControle.getInstancia();
        cadastrarEnderecoControle = CadastrarEnderecoControle.getInstancia();

        buttonBuscarDadosCep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarInformacoesCep();
            }
        });
        formattedTextFieldCep.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                if (e.getKeyCode() == 10) {
                    buscarInformacoesCep();
                }
            }
        });
        buttonCadastrarEndereco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarEndereco();
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
            informacoesCepDto = buscarInformacoesCepControle.buscarInformacoesCep(cep);
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

    public void cadastrarEndereco() {
        try {
            cadastrarEnderecoControle.cadastrarEndereco(new CadastrarEnderecoDto(
                    textFieldCidade.getText(),
                    textFieldLogradouro.getText(),
                    textFieldBairro.getText(),
                    textFieldEstado.getText(),
                    textFieldNumero.getText(),
                    formattedTextFieldCep.getText()
            ));

            JOptionPane.showMessageDialog(
                    this.panelConteudo,
                    "Cadastro realizado com sucesso",
                    "Cadastro realizado",
                    JOptionPane.INFORMATION_MESSAGE
            );
            limparCamposEndereco();
        } catch (CadastrarEnderecoException | ifsp.edu.br.model.exception.CadastrarEnderecoException e) {
            JOptionPane.showMessageDialog(
                    panelConteudo,
                    e.getMessage(),
                    "Erro ao cadastrar material",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void limparCamposEndereco() {
        formattedTextFieldCep.setValue("");
        textFieldLogradouro.setText("");
        textFieldBairro.setText("");
        textFieldNumero.setText("");
        textFieldCidade.setText("");
        textFieldEstado.setText("");
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

        JFrame frame = new JFrame("Cadastro de Endereço");
        frame.setContentPane(new CadastrarEnderecoPanel().panelConteudo);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
