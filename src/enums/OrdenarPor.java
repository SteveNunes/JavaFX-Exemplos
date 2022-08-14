package enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum OrdenarPor {
	
	NOME(1),
	PRECO(2),
	QUANTIDADE(3);
	
	private static Map<OrdenarPor, String> name;
	private final int value;

	OrdenarPor(int value)
		{ this.value = value; }

	public int getValue()
		{ return value; }

	public static List<OrdenarPor> getListOfAll() {
		List<OrdenarPor> list = new ArrayList<>();
		list.add(NOME);
		list.add(QUANTIDADE);
		list.add(PRECO);
		return list;
	}
	
	public String getName()
		{ return getName(this); }

	public static String getName(OrdenarPor s) {
		if (name == null) {
			name = new HashMap<>();
			name.put(NOME, "Nome");
			name.put(PRECO, "Preço");
			name.put(QUANTIDADE, "Quantidade");
		}
		return name.get(s);
	}
	
}
