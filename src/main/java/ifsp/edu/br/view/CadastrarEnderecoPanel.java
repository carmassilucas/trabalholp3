package ifsp.edu.br.view;

import ifsp.edu.br.control.BuscarDadosCepControle;
import ifsp.edu.br.control.CadastrarEnderecoControle;
import ifsp.edu.br.control.exception.BuscarInformacoesCepException;
import ifsp.edu.br.control.exception.CadastrarEnderecoException;
import ifsp.edu.br.model.dto.CadastrarEnderecoDto;
import ifsp.edu.br.model.dto.InformacoesCepDto;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.UUID;

public class CadastrarEnderecoPanel {
    private JPanel panelConteudo;
    private JTextField textFieldLogradouro;
    private JTextField textFieldNumero;
    private JTextField textFieldEstado;
    private JTextField textFieldCidade;
    private JFormattedTextField formattedTextFieldCep;
    private JButton buttonBuscarDadosCep;
    private JTextField textFieldBairro;
    private JButton buttonCadastrarEndereco;
    private JLabel labelVoltar;

    private static CadastrarEnderecoPanel instancia;
    private final BuscarDadosCepControle buscarInformacoesCepControle;
    private final CadastrarEnderecoControle cadastrarEnderecoControle;
    private UUID idCliente;

    private CadastrarEnderecoPanel() {
        buscarInformacoesCepControle = BuscarDadosCepControle.getInstancia();
        cadastrarEnderecoControle = CadastrarEnderecoControle.getInstancia();

        buttonBuscarDadosCep.addActionListener(e -> buscarInformacoesCep());
        buttonCadastrarEndereco.addActionListener(e -> cadastrarEndereco());
        formattedTextFieldCep.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                if (e.getKeyCode() == 10) {
                    buscarInformacoesCep();
                }
            }
        });
        labelVoltar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                labelVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                idCliente = null;
                JPanel panelConteudoProximaPagina = PesquisarMateriaisPanel.getInstancia().getPanelConteudo();
                PesquisarMateriaisPanel.getInstancia().carregarComboBox();
                GerenciadorDePaineis.getInstancia().setContentPane(panelConteudoProximaPagina);
            }
        });
    }

    public static CadastrarEnderecoPanel getInstancia() {
        if (instancia == null)
            instancia = new CadastrarEnderecoPanel();
        return instancia;
    }

    public JPanel getPanelConteudo() {
        return panelConteudo;
    }

    public void setIdCliente(UUID idCliente) {
        this.idCliente = idCliente;
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
                    idCliente,
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
        } catch (CadastrarEnderecoException e) {
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

        formattedTextFieldCep.requestFocus();
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
}
