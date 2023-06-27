package ifsp.edu.br.view;

import ifsp.edu.br.control.ClienteControle;
import ifsp.edu.br.control.ListaControle;
import ifsp.edu.br.control.ReciclagemControle;
import ifsp.edu.br.control.exception.AdicionarMaterialListaException;
import ifsp.edu.br.model.dto.CadastrarMaterialListaDto;
import ifsp.edu.br.model.dto.PesquisarMateriaisDto;
import ifsp.edu.br.model.vo.EnderecoVo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;
import java.util.UUID;

public class PesquisarMateriaisPanel {
    private JPanel panelConteudo;
    private JTextField textFieldMaterial;
    private JTable tableMateriaisPesquisados;
    private JButton buttonAdicionarLista;
    private JButton buttonTrajeto;
    private JLabel labelSair;
    private JLabel labelLista;
    private JComboBox<String> comboBoxEndereco;
    private JLabel labelCadastrarEndereco;

    private static PesquisarMateriaisPanel instancia;
    private final ClienteControle clienteControle;
    private final ReciclagemControle reciclagemControle;
    private final ListaControle listaControle;
    private UUID idCliente;

    private PesquisarMateriaisPanel() {
        clienteControle = ClienteControle.getInstancia();
        reciclagemControle = ReciclagemControle.getInstancia();
        listaControle = ListaControle.getInstancia();

        tableMateriaisPesquisados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                buttonAdicionarLista.setEnabled(true);
        });
        buttonAdicionarLista.addActionListener(e -> adicionarMaterialLista());
        labelSair.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                labelSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                idCliente = null;
                JPanel panelConteudoProximaPagina = LoginPanel.getInstancia().getPanelConteudo();
                GerenciadorDePaineis.getInstancia().setContentPane(panelConteudoProximaPagina);
            }
        });
        labelLista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                labelLista.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                ListaMeteriaisPanel listaMaterialPanel = ListaMeteriaisPanel.getInstancia();
                listaMaterialPanel.setIdCliente(idCliente);
                listaMaterialPanel.carregarTabela();
                JPanel panelConteudoProximaPagina = listaMaterialPanel.getPanelConteudo();
                GerenciadorDePaineis.getInstancia().setContentPane(panelConteudoProximaPagina);
            }
        });
        labelCadastrarEndereco.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                labelCadastrarEndereco.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                CadastrarEnderecoPanel cadastrarEnderecoPanel = CadastrarEnderecoPanel.getInstancia();
                cadastrarEnderecoPanel.setIdUsuario(idCliente);
                JPanel panelConteudoProximaPagina = cadastrarEnderecoPanel.getPanelConteudo();
                GerenciadorDePaineis.getInstancia().setContentPane(panelConteudoProximaPagina);
            }
        });
        textFieldMaterial.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                carregarTabela();
            }
        });
    }

    public static PesquisarMateriaisPanel getInstancia() {
        if (instancia == null)
            instancia = new PesquisarMateriaisPanel();
        return instancia;
    }

    public JPanel getPanelConteudo() {
        return panelConteudo;
    }

    public void setIdCliente(UUID idCliente) {
        this.idCliente = idCliente;
    }

    public void adicionarMaterialLista() {
        int indexLinhaSeleciona = tableMateriaisPesquisados.getSelectedRow();

        String nomeMaterial = tableMateriaisPesquisados.getValueAt(indexLinhaSeleciona, 0).toString();
        String nomeReciclagem = tableMateriaisPesquisados.getValueAt(indexLinhaSeleciona, 1).toString();

        try {
            listaControle.cadastrarMaterialLista(new CadastrarMaterialListaDto(
                    idCliente,
                    null,
                    null,
                    nomeMaterial,
                    nomeReciclagem
            ));
        } catch (AdicionarMaterialListaException e) {
            JOptionPane.showMessageDialog(this.panelConteudo, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(
                this.panelConteudo,
                "Material adicionado à sua lista",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
        );

        tableMateriaisPesquisados.removeRowSelectionInterval(tableMateriaisPesquisados.getSelectedRow(), 0);
        textFieldMaterial.setText("");
        textFieldMaterial.requestFocus();
        carregarTabela();
    }

    public void carregarTabela() {
        if (textFieldMaterial.getText().isBlank()) {
            DefaultTableModel modelo = (DefaultTableModel) tableMateriaisPesquisados.getModel();
            modelo.setNumRows(0);
            return;
        }

        List<PesquisarMateriaisDto> reciclagens = reciclagemControle.pesquisarMateriais(
                new PesquisarMateriaisDto(textFieldMaterial.getText())
        );

        DefaultTableModel modelo = (DefaultTableModel) tableMateriaisPesquisados.getModel();
        modelo.setNumRows(0);

        for (PesquisarMateriaisDto reciclagem : reciclagens)
            modelo.addRow(new Object[] {
                    reciclagem.getNomeMaterial(),
                    reciclagem.getNomeReciclagem(),
                    reciclagem.getLogradouro() + ", " + reciclagem.getNumero().toString(),
                    reciclagem.getPreco()
            });
    }

    public void carregarComboBox() {
        comboBoxEndereco.removeAllItems();
        for (EnderecoVo e : clienteControle.getEnderecosByIdCliente(idCliente))
            comboBoxEndereco.addItem(e.getLogradouro() + ", " + e.getNumero());
    }

    private void createUIComponents() {
        DefaultTableModel model = new DefaultTableModel(new String[]{
                "Material", "Reciclagem", "Endereço", "Valor pago (kg)"
        }, 0);
        tableMateriaisPesquisados = new JTable(model) {
            @Override
            public boolean editCellAt(int row, int column, EventObject e) {
                return false;
            }
        };
        tableMateriaisPesquisados.getTableHeader().repaint();
        tableMateriaisPesquisados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
