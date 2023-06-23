package ifsp.edu.br.view;

import ifsp.edu.br.control.MaterialControle;
import ifsp.edu.br.control.ReciclagemControle;
import ifsp.edu.br.model.dto.RelacionarMaterialReciclagemDto;
import ifsp.edu.br.model.vo.MaterialReciclagemVo;
import ifsp.edu.br.model.vo.MaterialVo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GerenciarMateriaisPanel {
    private JPanel panelConteudo;
    private JTextField textFieldNome;
    private JTextField textFieldPreco;
    private JTextArea textAreaDescricao;
    private JButton buttonAdicionar;
    private JButton buttonEditar;
    private JTable tableMateriaisReciclagem;
    private JTable tableMateriaisSistema;

    private final MaterialControle materialControle;
    private final ReciclagemControle reciclagemControle;
    private List<MaterialVo> materiaisSistema;
    private List<MaterialReciclagemVo> materiaisReciclagem;
    private UUID idReciclagem;

    private static GerenciarMateriaisPanel instancia;

    private GerenciarMateriaisPanel() {
        materialControle = MaterialControle.getInstancia();
        reciclagemControle = ReciclagemControle.getInstancia();

        tableMateriaisSistema.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                selecionarMaterialTableMateriaisSistema();
        });
        tableMateriaisReciclagem.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                selecionarMaterialTableMateriaisReciclagem();
        });
        buttonAdicionar.addActionListener(e -> adicionarMaterial());
        buttonEditar.addActionListener(e -> editarMaterial());
        panelConteudo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                limparCampos();
            }
        });
    }

    public static GerenciarMateriaisPanel getInstancia() {
        if (instancia == null)
            instancia = new GerenciarMateriaisPanel();
        return instancia;
    }

    public JPanel getPanelConteudo() {
        return panelConteudo;
    }

    public void setIdReciclagem(UUID idReciclagem) {
        this.idReciclagem = idReciclagem;
    }

    public void carregarTabelas() {
        DefaultTableModel modeloTableMateriaisSistema = (DefaultTableModel) tableMateriaisSistema.getModel();
        modeloTableMateriaisSistema.setNumRows(0);

        materiaisSistema = materialControle.buscarMateriais();

        for (MaterialVo material : materiaisSistema)
            modeloTableMateriaisSistema.addRow(new Object[] { material.getNome() });

        DefaultTableModel modeloTableMateriaisReciclagem = (DefaultTableModel) tableMateriaisReciclagem.getModel();
        modeloTableMateriaisReciclagem.setNumRows(0);

        materiaisReciclagem = reciclagemControle.buscarMateriais(idReciclagem);

        for (MaterialReciclagemVo material : materiaisReciclagem) {
            MaterialVo materialVo = getMaterialById(material.getIdMaterial());
            assert materialVo != null;
            modeloTableMateriaisReciclagem.addRow(new Object[] { materialVo.getNome() });
        }
    }

    private void selecionarMaterialTableMateriaisSistema() {
        textFieldPreco.setText("");
        if (tableMateriaisSistema.getSelectedRow() != -1) {
            MaterialVo material = getMaterialByNome(tableMateriaisSistema.getValueAt(
                    tableMateriaisSistema.getSelectedRow(), 0).toString()
            );
            assert material != null;
            textFieldNome.setText(material.getNome());
            textAreaDescricao.setText(material.getDescricao());

            if (tableMateriaisReciclagem.getSelectedRow() != -1)
                tableMateriaisReciclagem.removeRowSelectionInterval(tableMateriaisReciclagem.getSelectedRow(), 0);

            buttonEditar.setEnabled(false);
            buttonAdicionar.setEnabled(true);
        }
    }

    private void selecionarMaterialTableMateriaisReciclagem() {
        if (tableMateriaisReciclagem.getSelectedRow() != -1) {
            MaterialVo material = getMaterialByNome(tableMateriaisReciclagem.getValueAt(
                    tableMateriaisReciclagem.getSelectedRow(), 0).toString()
            );
            assert material != null;
            textFieldNome.setText(material.getNome());
            textAreaDescricao.setText(material.getDescricao());

            MaterialReciclagemVo materialReciclagemVo = getMaterialReciclagemVoById(material.getId());
            assert materialReciclagemVo != null;
            textFieldPreco.setText(materialReciclagemVo.getPreco().toString());

            if (tableMateriaisSistema.getSelectedRow() != -1)
                tableMateriaisSistema.removeRowSelectionInterval(tableMateriaisSistema.getSelectedRow(), 0);

            buttonAdicionar.setEnabled(false);
            buttonEditar.setEnabled(true);
        }
    }

    private MaterialVo getMaterialByNome(String nome) {
        for (MaterialVo material : materiaisSistema)
            if (material.getNome().equals(nome))
                return material;
        return null;
    }

    private MaterialVo getMaterialById(UUID id) {
        for (MaterialVo material : materiaisSistema)
            if (material.getId().equals(id))
                return material;
        return null;
    }

    private MaterialReciclagemVo getMaterialReciclagemVoById(UUID id) {
        for (MaterialReciclagemVo materialReciclagemVo : materiaisReciclagem)
            if (materialReciclagemVo.getIdMaterial().equals(id))
                return materialReciclagemVo;
        return null;
    }

    private void limparCampos() {
        textFieldNome.setText("");
        textAreaDescricao.setText("");
        textFieldPreco.setText("");

        if (tableMateriaisSistema.getSelectedRow() != -1)
            tableMateriaisSistema.removeRowSelectionInterval(tableMateriaisSistema.getSelectedRow(), 0);

        if (tableMateriaisReciclagem.getSelectedRow() != -1)
            tableMateriaisReciclagem.removeRowSelectionInterval(tableMateriaisReciclagem.getSelectedRow(), 0);

        buttonAdicionar.setEnabled(true);
        buttonEditar.setEnabled(true);

        textFieldPreco.requestFocus();
    }

    private void adicionarMaterial() {
        if (tableMateriaisSistema.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(
                    panelConteudo,
                    "Selecione um material na lista de materiais cadastrados no sistema para prosseguir",
                    "Houve um engano",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            Integer status = reciclagemControle.relacionarMaterialReciclagem(new RelacionarMaterialReciclagemDto(
                    Objects.requireNonNull(getMaterialByNome(textFieldNome.getText())).getId(),
                    idReciclagem,
                    textFieldPreco.getText().isBlank() ? 0.0f :
                            Float.parseFloat(textFieldPreco.getText().replace(',', '.'))
            ));

            if (status == 0)
                return;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    panelConteudo,
                    "Por favor, preencha o campo de preço apenas com número. Exemplo: 3,99",
                    "Houve um engano",
                    JOptionPane.WARNING_MESSAGE
            );

            textFieldPreco.requestFocus();
            textFieldPreco.selectAll();
            return;
        }

        JOptionPane.showMessageDialog(
                panelConteudo,
                "Parabéns, agora você recicla um novo material",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
        );
        carregarTabelas();
    }

    private void editarMaterial() {
        if (tableMateriaisReciclagem.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(
                    panelConteudo,
                    "Selecione um material na sua lista de materiais para prosseguir",
                    "Houve um engano",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Float precoOriginalMaterial = getMaterialReciclagemVoById(
                getMaterialByNome(textFieldNome.getText()).getId()
        ).getPreco();

        Float precoTextFieldPreco = textFieldPreco.getText().isBlank() ?
                0.0f : Float.parseFloat(textFieldPreco.getText().replace(',', '.'));

        if (precoTextFieldPreco.equals(precoOriginalMaterial)) {
            JOptionPane.showMessageDialog(
                    panelConteudo,
                    "Não houve uma modificação no preço em que você paga pelo material",
                    "Houve um engano",
                    JOptionPane.WARNING_MESSAGE
            );
            textFieldPreco.requestFocus();
            textFieldPreco.selectAll();
            return;
        }

        try {
            reciclagemControle.editarPrecoMaterial(new RelacionarMaterialReciclagemDto(
                    Objects.requireNonNull(getMaterialByNome(textFieldNome.getText())).getId(),
                    idReciclagem,
                    textFieldPreco.getText().isBlank() ?
                            0.0f : Float.parseFloat(textFieldPreco.getText().replace(',', '.'))
            ));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    panelConteudo,
                    "Por favor, preencha o campo de preço apenas com número. Exemplo: 3,99",
                    "Houve um engano",
                    JOptionPane.WARNING_MESSAGE
            );

            textFieldPreco.requestFocus();
            textFieldPreco.selectAll();
            return;
        }

        JOptionPane.showMessageDialog(
                panelConteudo,
                "Preço do material atualizado com sucesso",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
        );
        limparCampos();
        carregarTabelas();
    }

    private void createUIComponents() {
        tableMateriaisSistema = new JTable(new DefaultTableModel(new String[]{"Material"}, 0)) {
            @Override
            public boolean editCellAt(int row, int column, EventObject e) {
                return false;
            }
        };
        tableMateriaisSistema.getColumnModel().getColumn(0).setHeaderValue("Materiais cadastrados no sistema");
        tableMateriaisSistema.getTableHeader().repaint();
        tableMateriaisSistema.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableMateriaisReciclagem = new JTable(new DefaultTableModel(new String[]{"Material"}, 0)) {
            @Override
            public boolean editCellAt(int row, int column, EventObject e) {
                return false;
            }
        };
        tableMateriaisReciclagem.getColumnModel().getColumn(0).setHeaderValue("Materiais que você recicla");
        tableMateriaisReciclagem.getTableHeader().repaint();
        tableMateriaisReciclagem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
