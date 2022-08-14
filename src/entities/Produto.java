package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Produto {

	private static List<Produto> listaDeProdutos = new ArrayList<>();
	private String nome;
	private int quantidade;
	private double valor;
	
	public Produto(Produto produto) {
		nome = produto.nome;
		quantidade = produto.quantidade;
		valor = produto.valor;
	}
	
	public Produto(String nome, int quantidade, double valor) {
		this.nome = nome;
		this.quantidade = quantidade;
		this.valor = valor;
	}
	
	public Produto(String nome, int quantidade)
		{ this(nome, quantidade, 0); }
	
	public Produto(String nome)
		{ this(nome, 0); }

	public static void addProduto(Produto produto)
		{ listaDeProdutos.add(produto); }
	
	public static void removeProduto(Produto produto)
		{ listaDeProdutos.remove(produto); }

	public static List<Produto> getUnmodifiableListaDeProdutos()
		{ return Collections.unmodifiableList(listaDeProdutos); }

	public static void setListaDeProdutos(List<Produto> lista)
		{ listaDeProdutos = lista; }

	public String getNome()
		{ return nome; }
	
	public void setNome(String nome)
		{ this.nome = nome; }
	
	public int getQuantidade()
		{ return quantidade; }
	
	public void setQuantidade(int quantidade)
		{ this.quantidade = quantidade; }
	
	public double getValor()
		{ return valor; }
	
	public void setValor(double valor)
		{ this.valor = valor; }

	public static void sort(Comparator<? super Produto> comparator)
		{ listaDeProdutos.sort(comparator); }

	@Override
	public int hashCode()
		{ return Objects.hash(nome, quantidade, valor); }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		return Objects.equals(nome, other.nome) && quantidade == other.quantidade && Double.doubleToLongBits(valor) == Double.doubleToLongBits(other.valor);
	}

	@Override
	public String toString()
		{ return "Produto [nome=" + nome + ", quantidade=" + quantidade + ", valor=" + valor + "]"; }

}
