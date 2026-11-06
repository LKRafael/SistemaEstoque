package view;

import controller.ProdutoController;
import model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ProdutoView extends JFrame {

    private ProdutoController controller;

    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtTipo;
    private JTextField txtQuantidade;
    private JTextField txtPreco;
    private JTextField txtDescricao;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private int idSelecionado = -1;

    public ProdutoView() {

        controller = new ProdutoController();

        setTitle("Sistema de Gerenciamento de Estoque");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        criarInterface();
        listarProdutos();
    }

    private void criarInterface() {

        setLayout(new BorderLayout(10, 10));

        JPanel painelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        painelFormulario.setBorder(
            BorderFactory.createTitledBorder("Dados do Produto")
        );

        txtCodigo = new JTextField();
        txtNome = new JTextField();
        txtTipo = new JTextField();
        txtQuantidade = new JTextField();
        txtPreco = new JTextField();
        txtDescricao = new JTextField();

        painelFormulario.add(new JLabel("Código:"));
        painelFormulario.add(txtCodigo);

        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(txtNome);

        painelFormulario.add(new JLabel("Tipo:"));
        painelFormulario.add(txtTipo);

        painelFormulario.add(new JLabel("Quantidade:"));
        painelFormulario.add(txtQuantidade);

        painelFormulario.add(new JLabel("Preço:"));
        painelFormulario.add(txtPreco);

        painelFormulario.add(new JLabel("Descrição:"));
        painelFormulario.add(txtDescricao);

        add(painelFormulario, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(
            new String[]{"ID", "Código", "Nome", "Tipo",
                         "Quantidade", "Preço", "Descrição"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(
            BorderFactory.createTitledBorder("Produtos cadastrados")
        );

        add(scroll, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout());

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        add(painelBotoes, BorderLayout.SOUTH);

        btnCadastrar.addActionListener(e -> cadastrarProduto());
        btnEditar.addActionListener(e -> editarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());
        btnLimpar.addActionListener(e -> limparCampos());

        tabela.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                preencherCampos();
            }

        });
    }

    private Produto obterDadosFormulario() {

        String codigo = txtCodigo.getText().trim();
        String nome = txtNome.getText().trim();
        String tipo = txtTipo.getText().trim();
        String descricao = txtDescricao.getText().trim();

        int quantidade = Integer.parseInt(
            txtQuantidade.getText().trim()
        );

        BigDecimal preco = new BigDecimal(
            txtPreco.getText().trim().replace(",", ".")
        );

        if (preco.scale() > 2) {
            throw new IllegalArgumentException(
                "O preço deve ter no máximo duas casas decimais."
            );
        }

        if (preco.precision() > 10) {
            throw new IllegalArgumentException(
                "O preço informado é muito alto."
            );
        }

        return new Produto(
            codigo,
            nome,
            tipo,
            quantidade,
            preco.doubleValue(),
            descricao
        );
    }

    private void cadastrarProduto() {

        try {

            Produto produto = obterDadosFormulario();

            if (controller.cadastrar(produto)) {

                JOptionPane.showMessageDialog(
                    this, "Produto cadastrado com sucesso!"
                );

                listarProdutos();
                limparCampos();

            } else {

                JOptionPane.showMessageDialog(
                    this, "Não foi possível cadastrar o produto. Verifique os campos."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this, "Quantidade ou preço inválido."
            );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(this, e.getMessage());

        }
    }

    private void editarProduto() {

        if (idSelecionado == -1) {

            JOptionPane.showMessageDialog(
                this, "Selecione um produto na tabela."
            );

            return;
        }

        try {

            Produto produto = obterDadosFormulario();
            produto.setId(idSelecionado);

            if (controller.atualizar(produto)) {

                JOptionPane.showMessageDialog(
                    this, "Produto atualizado com sucesso!"
                );

                listarProdutos();
                limparCampos();

            } else {

                JOptionPane.showMessageDialog(
                    this, "Não foi possível atualizar o produto."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this, "Quantidade ou preço inválido."
            );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(this, e.getMessage());

        }
    }

    private void excluirProduto() {

        if (idSelecionado == -1) {

            JOptionPane.showMessageDialog(
                this, "Selecione um produto na tabela."
            );

            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir este produto?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION
        );

        if (resposta == JOptionPane.YES_OPTION) {

            if (controller.excluir(idSelecionado)) {

                JOptionPane.showMessageDialog(
                    this, "Produto excluído com sucesso!"
                );

                listarProdutos();
                limparCampos();

            } else {

                JOptionPane.showMessageDialog(
                    this, "Não foi possível excluir o produto."
                );
            }
        }
    }

    private void listarProdutos() {

        modeloTabela.setRowCount(0);

        List<Produto> produtos = controller.listar();

        for (Produto produto : produtos) {

            modeloTabela.addRow(new Object[]{
                produto.getId(),
                produto.getCodigo(),
                produto.getNome(),
                produto.getTipo(),
                produto.getQuantidade(),
                String.format("%.2f", produto.getPreco()),
                produto.getDescricao()
            });
        }
    }

    private void preencherCampos() {

        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            return;
        }

        idSelecionado = Integer.parseInt(
            modeloTabela.getValueAt(linha, 0).toString()
        );

        txtCodigo.setText(
            modeloTabela.getValueAt(linha, 1).toString()
        );

        txtNome.setText(
            modeloTabela.getValueAt(linha, 2).toString()
        );

        txtTipo.setText(
            modeloTabela.getValueAt(linha, 3).toString()
        );

        txtQuantidade.setText(
            modeloTabela.getValueAt(linha, 4).toString()
        );

        txtPreco.setText(
            modeloTabela.getValueAt(linha, 5).toString()
        );

        txtDescricao.setText(
            modeloTabela.getValueAt(linha, 6).toString()
        );
    }

    private void limparCampos() {

        txtCodigo.setText("");
        txtNome.setText("");
        txtTipo.setText("");
        txtQuantidade.setText("");
        txtPreco.setText("");
        txtDescricao.setText("");

        idSelecionado = -1;
        tabela.clearSelection();
    }
}
