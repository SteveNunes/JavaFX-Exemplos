package dao;

import java.util.List;

import entities.Funcionario;
import entities.Produto;

public interface DaoInterface {
	
	List<Funcionario> carregarFuncionario();
	List<Produto> carregarProdutos();
	void salvarFuncionario();
	void salvarProdutos();
	
}
