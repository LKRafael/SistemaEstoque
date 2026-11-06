package controller;

import dao.ProdutoDAO;
import model.Produto;

import java.util.List;

public class ProdutoController {

    private ProdutoDAO produtoDAO;

    public ProdutoController() {
        produtoDAO = new ProdutoDAO();
    }

    // CADASTRAR
    public boolean cadastrar(Produto produto) {

        if (produto.getCodigo().isBlank() ||
            produto.getNome().isBlank() ||
            produto.getTipo().isBlank()) {
            return false;
        }

        if (produto.getQuantidade() < 0 || produto.getPreco() < 0) {
            return false;
        }

        return produtoDAO.cadastrar(produto);
    }

    // LISTAR
    public List<Produto> listar() {
        return produtoDAO.listar();
    }

    // ATUALIZAR
    public boolean atualizar(Produto produto) {

        if (produto.getCodigo().isBlank() ||
            produto.getNome().isBlank() ||
            produto.getTipo().isBlank()) {
            return false;
        }

        if (produto.getQuantidade() < 0 || produto.getPreco() < 0) {
            return false;
        }

        return produtoDAO.atualizar(produto);
    }

    // EXCLUIR
    public boolean excluir(int id) {
        return produtoDAO.excluir(id);
    }
}
