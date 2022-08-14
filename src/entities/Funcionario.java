package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Funcionario {

	private static List<Funcionario> listaDeFuncionarios = new ArrayList<>();
	private String nome;
	private String setor;
	
	public Funcionario(Funcionario funcionario) {
		nome = funcionario.nome;
		setor = funcionario.setor;
	}
	
	public Funcionario(String nome, String setor) {
		this.nome = nome;
		this.setor = setor;
	}
	
	public static void addFuncionario(Funcionario produto)
		{ listaDeFuncionarios.add(produto); }
	
	public static void removeFuncionario(Funcionario produto)
		{ listaDeFuncionarios.remove(produto); }
	
	public static List<Funcionario> getUnmodifiableListaDeFuncionarios()
		{ return Collections.unmodifiableList(listaDeFuncionarios); }
	
	public static void setListaDeFuncionarios(List<Funcionario> lista)
		{ listaDeFuncionarios = lista; }
	
	public String getNome()
		{ return nome; }
	
	public void setNome(String nome)
		{ this.nome = nome; }
	
	public String getSetor()
		{ return setor; }
	
	public void setSetor(String setor)
		{ this.setor = setor; }

	public static void sort(Comparator<? super Funcionario> comparator)
		{ listaDeFuncionarios.sort(comparator); }

	@Override
	public int hashCode()
		{ return Objects.hash(nome, setor); }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Funcionario other = (Funcionario) obj;
		return Objects.equals(nome, other.nome) && Objects.equals(setor, other.setor);
	}

	
	
}
