package ifsp.edu.br.view;

import ifsp.edu.br.control.MaterialControle;
import ifsp.edu.br.control.ReciclagemControle;
import ifsp.edu.br.model.dto.NegociarMaterialDto;
import ifsp.edu.br.model.exception.NegociarMaterialException;
import ifsp.edu.br.model.vo.MaterialReciclagemVo;
import ifsp.edu.br.model.vo.MaterialVo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.UUID;

public class NegociarMateriaisPanel {
    private JPanel panelConteudo;
    private JTextField textFieldPeso;
    private JTextField textFieldEmail;
    private JTable tableMateriais;
    private JButton buttonNegociar;
    private JLabel labelMateriais;
    private JLabel labelSair;

    private static NegociarMateriaisPanel instancia;
    private final ReciclagemControle reciclagemControle;
    private final MaterialControle materialControle;
    private List<MaterialReciclagemVo> materiaisReciclagem;
    private List<MaterialVo> materiais;
    private UUID idReciclagem;

    private NegociarMateriaisPanel() {
        reciclagemControle = ReciclagemControle.getInstancia();
        materialControle = MaterialControle.getInstancia();
        materiaisReciclagem = new ArrayList<>();
        materiais = new ArrayList<>();

        labelMateriais.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                labelMateriais.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                GerenciarMateriaisPanel gerenciarMateriaisPanel = GerenciarMateriaisPanel.getInstancia();
                gerenciarMateriaisPanel.setIdReciclagem(idReciclagem);
                gerenciarMateriaisPanel.carregarTabelas();
                JPanel panelConteudoProximaPagina = gerenciarMateriaisPanel.getPanelConteudo();
                GerenciadorDePaineis.getInstancia().setContentPane(panelConteudoProximaPagina);
            }
        });
        labelSair.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                labelSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                idReciclagem = null;
                JPanel panelConteudoProximaPagina = LoginPanel.getInstancia().getPanelConteudo();
                GerenciadorDePaineis.getInstancia().setContentPane(panelConteudoProximaPagina);
            }
        });
        buttonNegociar.addActionListener(e -> negociar());
    }

    public static NegociarMateriaisPanel getInstancia() {
        if (instancia == null)
            instancia = new NegociarMateriaisPanel();
        return instancia;
    }

    public JPanel getPanelConteudo() {
        return panelConteudo;
    }

    public void setIdReciclagem(UUID idReciclagem) {
        this.idReciclagem = idReciclagem;
    }

    public void carregarTabelas() {
        DefaultTableModel modeloTableMateriaisSistema = (DefaultTableModel) tableMateriais.getModel();
        modeloTableMateriaisSistema.setNumRows(0);

        materiaisReciclagem = reciclagemControle.buscarMateriais(idReciclagem);

        for (MaterialReciclagemVo materialReciclagem : materiaisReciclagem) {
            MaterialVo material = materialControle.getById(materialReciclagem.getIdMaterial());
            materiais.add(material);
            modeloTableMateriaisSistema.addRow(new Object[] { material.getNome() });
        }
    }

    private void negociar() {
        if (tableMateriais.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(
                    this.panelConteudo,
                    "Selecione um material em sua lista para prosseguir",
                    "Houve um engano",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (textFieldEmail.getText().isBlank() || textFieldPeso.getText().isBlank()) {
            JOptionPane.showMessageDialog(
                    this.panelConteudo,
                    "Preencha todos os campos para prosseguir",
                    "Houve um engano",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        MaterialVo material = getByNome(tableMateriais.getValueAt(
                tableMateriais.getSelectedRow(), 0).toString()
        );

        assert material != null;
        Float preco = getPrecoById(material.getId());

        try {
            reciclagemControle.negociar(new NegociarMaterialDto(
                    idReciclagem,
                    material.getId(),
                    null,
                    textFieldEmail.getText(),
                    Float.parseFloat(textFieldPeso.getText().replace(',', '.')),
                    preco
            ));
        } catch (NegociarMaterialException e) {
            JOptionPane.showMessageDialog(this.panelConteudo, e.getMessage(), "Erro ao negociar material", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this.panelConteudo,
                    "Campo peso preenchido com um valor inválido inválido",
                    "Houve um engano",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
                this.panelConteudo,
                "Negociação feita com sucesso",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
        );

        textFieldPeso.setText("");
        textFieldPeso.requestFocus();

        tableMateriais.removeRowSelectionInterval(tableMateriais.getSelectedRow(), 0);
    }

    private MaterialVo getByNome(String nome) {
        for (MaterialVo material : materiais)
            if (material.getNome().equals(nome))
                return material;
        return null;
    }

    private Float getPrecoById(UUID id) {
        for (MaterialReciclagemVo materialReciclagem : materiaisReciclagem)
            if (materialReciclagem.getIdMaterial().equals(id))
                return materialReciclagem.getPreco();
        return null;
    }

    private void createUIComponents() {
        tableMateriais = new JTable(new DefaultTableModel(new String[]{"Materiais"}, 0)) {
            @Override
            public boolean editCellAt(int row, int column, EventObject e) {
                return false;
            }
        };
        tableMateriais.getTableHeader().repaint();
        tableMateriais.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
