package ifsp.edu.br.view;

import ifsp.edu.br.control.ListaControle;
import ifsp.edu.br.model.dto.BuscarMateriaisListaDto;
import ifsp.edu.br.model.dto.RemoverMaterialListaDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;
import java.util.UUID;

public class ListaMeteriaisPanel {
    private JPanel panelConteudo;
    private JTable tableListaMateriais;
    private JButton buttonDeletar;
    private JLabel labelVoltar;

    private static ListaMeteriaisPanel instancia;
    private final ListaControle listaControle;
    private UUID idCliente;

    private ListaMeteriaisPanel() {
        listaControle = ListaControle.getInstancia();

        labelVoltar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                labelVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                PesquisarMateriaisPanel pesquisarMateriaisPanel = PesquisarMateriaisPanel.getInstancia();
                JPanel panelConteudoProximaPagina = pesquisarMateriaisPanel.getPanelConteudo();
                GerenciadorDePaineis.getInstancia().setContentPane(panelConteudoProximaPagina);
            }
        });
        buttonDeletar.addActionListener(e -> removerMaterialLista());
    }

    public static ListaMeteriaisPanel getInstancia() {
        if (instancia == null)
            instancia = new ListaMeteriaisPanel();
        return instancia;
    }

    public JPanel getPanelConteudo() {
        return panelConteudo;
    }

    public void setIdCliente(UUID idCliente) {
        this.idCliente = idCliente;
    }

    public void carregarTabela() {
        List<BuscarMateriaisListaDto> listaMateriais = listaControle.getListaMateriais(idCliente);

        DefaultTableModel modelo = (DefaultTableModel) tableListaMateriais.getModel();
        modelo.setNumRows(0);

        for (BuscarMateriaisListaDto materialLista : listaMateriais)
            modelo.addRow(new Object[] {
                    materialLista.getNomeMaterial(),
                    materialLista.getNomeReciclagem(),
                    materialLista.getEnderecoReciclagem() + ", " + materialLista.getNumero().toString(),
                    materialLista.getPreco()
            });
    }

    private void removerMaterialLista() {
        if (tableListaMateriais.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(
                    this.panelConteudo,
                    "Selecione algum material de sua lista para prosseguir",
                    "Houve um engano",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        listaControle.removerMaterialLista(new RemoverMaterialListaDto(
                idCliente,
                null,
                tableListaMateriais.getValueAt(tableListaMateriais.getSelectedRow(), 0).toString()
        ));

        JOptionPane.showMessageDialog(
                this.panelConteudo,
                "Material removido da sua lista",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
        );

        carregarTabela();
    }

    private void createUIComponents() {
        DefaultTableModel model = new DefaultTableModel(new String[]{
                "Material", "Reciclagem", "Endereço", "Valor pago (kg)"
        }, 0);
        tableListaMateriais = new JTable(model) {
            @Override
            public boolean editCellAt(int row, int column, EventObject e) {
                return false;
            }
        };
        tableListaMateriais.getTableHeader().repaint();
        tableListaMateriais.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
