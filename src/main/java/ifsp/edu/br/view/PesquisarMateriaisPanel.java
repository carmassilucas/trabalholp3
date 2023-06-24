package ifsp.edu.br.view;

import ifsp.edu.br.control.ClienteControle;
import ifsp.edu.br.control.ReciclagemControle;
import ifsp.edu.br.model.dto.PesquisarMateriaisDto;
import ifsp.edu.br.model.vo.EnderecoVo;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private UUID idUsuario;

    private PesquisarMateriaisPanel() {
        clienteControle = ClienteControle.getInstancia();
        reciclagemControle = ReciclagemControle.getInstancia();

        tableMateriaisPesquisados.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting())
                    buttonAdicionarLista.setEnabled(true);
            }
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

                idUsuario = null;
                JPanel panelConteudoProximaPagina = LoginPanel.getInstancia().getPanelConteudo();
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

                CadastrarEnderecoPanel.getInstancia().setIdUsuario(idUsuario);
                JPanel panelConteudoProximaPagina = CadastrarEnderecoPanel.getInstancia().getPanelConteudo();
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

    public void setIdUsuario(UUID idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void adicionarMaterialLista() {

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
        for (EnderecoVo e : clienteControle.getEnderecosByIdCliente(idUsuario))
            comboBoxEndereco.addItem(e.getLogradouro() + ", " + e.getNumero());
    }

    private void createUIComponents() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Material", "Reciclagem", "Endereço", "Valor pago (kg)"}, 0);
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
