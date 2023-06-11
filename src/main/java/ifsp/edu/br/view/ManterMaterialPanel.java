package ifsp.edu.br.view;

import ifsp.edu.br.control.MaterialControle;
import ifsp.edu.br.control.exception.CadastrarMaterialException;
import ifsp.edu.br.model.dto.CadastrarMaterialDto;
import ifsp.edu.br.model.vo.MaterialVo;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;

public class ManterMaterialPanel {
    private JPanel panelConteudo;
    private JTextField textFieldNome;
    private JTextArea textAreaDescricao;
    private JLabel labelNome;
    private JLabel labelDescricao;
    private JButton cadastrarButton;
    private JTable tableMateriais;
    private JButton editarButton;
    private JScrollPane scrollPaneTabela;
    private MaterialControle materialControle;
    private List<MaterialVo> materiais;
    public ManterMaterialPanel() {
        materialControle = MaterialControle.getInstancia();

        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarMaterial();
            }
        });
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarMaterial();
            }
        });
        tableMateriais.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting())
                    selecionarMaterial();
            }
        });
        panelConteudo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(tableMateriais.getSelectedRow() != -1) {
                    limparCampos();
                }
            }
        });

        carregarTabela();
    }

    private void cadastrarMaterial() {
        try {
            materialControle.cadastrarMaterial(new CadastrarMaterialDto(
                    textFieldNome.getText().trim(),
                    textAreaDescricao.getText().trim()
            ));

            JOptionPane.showMessageDialog(
                    panelConteudo,
                    "Cadastro realizado com sucesso",
                    "Cadastro realizado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limparCampos();
            carregarTabela();
        } catch (CadastrarMaterialException | ifsp.edu.br.model.exception.CadastrarMaterialException e) {
            JOptionPane.showMessageDialog(
                    panelConteudo,
                    e.getMessage(),
                    "Erro ao cadastrar material",
                    JOptionPane.ERROR_MESSAGE
            );
            textFieldNome.requestFocus();
        }
    }

    private void editarMaterial() {
        if (tableMateriais.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(
                    panelConteudo,
                    "Selecione algum material para poder editar suas informações",
                    "Houve um engano",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (textFieldNome.getText().isEmpty() || textAreaDescricao.getText().isEmpty()) {
            JOptionPane.showMessageDialog(
                    panelConteudo,
                    "Preencha as informações para seguir com a edição do material",
                    "Houve um engano",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        MaterialVo material = getMaterialByNome(tableMateriais.getValueAt(tableMateriais.getSelectedRow(),
                0).toString());

        if (material.getNome().equals(textFieldNome.getText().trim()) &&
                material.getDescricao().equals(textAreaDescricao.getText().trim())) {
            JOptionPane.showMessageDialog(
                    panelConteudo,
                    "Nenhuma modificação nas inforamações do material foi feita",
                    "Houve um engano",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        material.setNome(textFieldNome.getText().trim());
        material.setDescricao(textAreaDescricao.getText().trim());
        materialControle.editarMaterial(material);

        JOptionPane.showMessageDialog(
                panelConteudo,
                "Informações do material alteradas com sucesso",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
        );

        limparCampos();
        carregarTabela();
    }

    public void selecionarMaterial() {
        if (tableMateriais.getSelectedRow() != -1) {
            MaterialVo material = getMaterialByNome(tableMateriais.getValueAt(tableMateriais.getSelectedRow(),
                    0).toString());
            textFieldNome.setText(material.getNome());
            textAreaDescricao.setText(material.getDescricao());
        }
    }

    public void carregarTabela() {
        DefaultTableModel modelo = (DefaultTableModel) tableMateriais.getModel();
        modelo.setNumRows(0);

        materiais = materialControle.buscarMateriais();

        for (MaterialVo material : materiais)
            modelo.addRow(new Object[] { material.getNome() });
    }

    public MaterialVo getMaterialByNome(String nome) {
        for (MaterialVo material : materiais)
            if (material.getNome().equals(nome))
                return material;
        return null;
    }

    public void limparCampos() {
        textFieldNome.setText("");
        textAreaDescricao.setText("");
        textFieldNome.requestFocus();
        tableMateriais.removeRowSelectionInterval(tableMateriais.getSelectedRow(), 0);
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

        JFrame frame = new JFrame("Cadastro de Material");
        frame.setContentPane(new ManterMaterialPanel().panelConteudo);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Material"}, 0);
        tableMateriais = new JTable(model) {
            @Override
            public boolean editCellAt(int row, int column, EventObject e) {
                return false;
            }
        };
        tableMateriais.getColumnModel().getColumn(0).setHeaderValue("Material");
        tableMateriais.getTableHeader().repaint();
        tableMateriais.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}

