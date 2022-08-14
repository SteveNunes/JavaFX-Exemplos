package dao;

import java.util.ArrayList;
import java.util.List;

import entities.Funcionario;
import entities.Produto;
import util.Alerts;
import util.IniFile;

public final class DaoFile implements DaoInterface {

	@Override
	public List<Funcionario> carregarFuncionario() {
		List<Funcionario> funcionarios = new ArrayList<>();
		
		try {
			IniFile ini = IniFile.getNewIniFileInstance("./src/Config/Funcionarios.ini");
		  for (String section : ini.getSectionList())
				funcionarios.add(new Funcionario(ini.read(section, "Nome"), ini.read(section, "Setor")));
		}
		catch (Exception e)
			{ Alerts.exception("Erro", "Não foi possível acessar o arquivo Funcionarios.ini", e.getMessage(), e); }
		
		return funcionarios;
	}

	@Override
	public List<Produto> carregarProdutos() {
		List<Produto> produtos = new ArrayList<>();
		String itemDebug = null, sectionDebug = null;
		IniFile ini = null;
		
		try {
			ini = IniFile.getNewIniFileInstance("./src/Config/Produtos.ini");
		  for (String section : ini.getSectionList()) {
		  	sectionDebug = section;
		  	String nome = ini.read(section, "Produto");
		  	int quantidade = Integer.parseInt(ini.read(section, "Quantidade"));
		  	double valor = Double.parseDouble(ini.read(section, "Valor"));
		  	produtos.add(new Produto(nome, quantidade, valor));
		  }
		}
		catch (Exception e) {
			if (ini == null) 
				Alerts.exception("Erro", "Não foi possível acessar o arquivo Produtos.ini", e.getMessage(), e);
			else {
				Alerts.exception("Erro", "Erro ao tentar ler dados no arquivo \"Produtos.ini\"" +
					"\n		Sessão: " + sectionDebug + "\n		Item: " + itemDebug + "\n		Valor: " +
					(ini.getLastReadVal() == null || ini.getLastReadVal().isEmpty() ? "" : ini.getLastReadVal()), e.getMessage(), e);
			}
		}
		return produtos;
	}

	@Override
	public void salvarFuncionario() {
		try {
			int id = 1;
			IniFile ini = IniFile.getNewIniFileInstance("./src/Config/Funcionarios.ini", true);
			for (Funcionario funcionario : Funcionario.getUnmodifiableListaDeFuncionarios()) {
				ini.write("" + id, "Nome", funcionario.getNome());
				ini.write("" + id++, "Setor", funcionario.getSetor());
			}
			ini.saveToDisk();
		}
		catch (Exception e)
			{ Alerts.exception("Erro", "Não foi possível acessar o arquivo Funcionarios.ini", e.getMessage(), e); }
	}

	@Override
	public void salvarProdutos() {
		try {
			int id = 1;
			IniFile ini = IniFile.getNewIniFileInstance("./src/Config/Produtos.ini", true);
			for (Produto produto : Produto.getUnmodifiableListaDeProdutos()) {
				ini.write("" + id, "Produto", produto.getNome());
				ini.write("" + id, "Quantidade", "" + produto.getQuantidade());
				ini.write("" + id++, "Valor", "" + produto.getValor());
			}
			ini.saveToDisk();
		}
		catch (Exception e)
			{ Alerts.exception("Erro", "Não foi possível acessar o arquivo Produtos.ini", e.getMessage(), e); }
	}

}
