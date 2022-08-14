package gui;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import dao.DaoFactory;
import entities.Funcionario;
import entities.Produto;
import enums.HBoxWitnButtonsEditMode;
import enums.HBoxWitnButtonsMode;
import enums.Icons;
import enums.OrdenarPor;
import gui.util.BackupMenu;
import gui.util.Controller;
import gui.util.EditableHBox;
import gui.util.HBoxWithButtons;
import gui.util.ListenerHandle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import util.Alerts;
import util.Misc;

public class MainWindowController implements Initializable {

	private HBoxWithButtons hBoxWithButtonsFuncionariosSelected;
	private HBoxWithButtons hBoxWithButtonsFuncionariosHover;
	private HBoxWithButtons hBoxWithButtonsProdutosSelected;
	private HBoxWithButtons hBoxWithButtonsProdutosHover;
	private EditableHBox editableBox;
	
	private ListenerHandle<OrdenarPor> listenerHandleOrdenarPor;
	
  @FXML
  private Menu menuMain;
  @FXML
  private MenuItem menuItemClose;
  @FXML
  private MenuItem menuItemAbout;
  @FXML
  private TabPane tabPane;
  @FXML
  private Tab tabFuncionarios;
  @FXML
  private Tab tabProdutos;
  @FXML
  private Tab tabOutros;
  @FXML
  private SplitPane splitPane;
  @FXML
  private VBox vBoxLeft;
  @FXML
  private VBox vBoxRight;
  @FXML
  private GridPane gridPaneProdutosInfo;
  @FXML
  private Label labelTotalFuncionarios;
  @FXML
  private Label labelTotalProdutos;
  @FXML
  private Label labelTotalProdutosQuantidade;
  @FXML
  private Label labelValorTotal;
  @FXML
  private ComboBox<OrdenarPor> comboBoxOrdenarPor;
  @FXML
  private TableView<Produto> tableViewProdutos;
  @FXML
  private TableColumn<Produto, Produto> tableColumnPreco;
  @FXML
  private TableColumn<Produto, Produto> tableColumnProduto;
  @FXML
  private TableColumn<Produto, Produto> tableColumnQuantidade;
  @FXML
  private TableColumn<Produto, Produto> tableColumnButtons;
  @FXML
  private Button buttonAdicionarProduto;
  @FXML
  private Button buttonRemoverProduto;
  @FXML
  private ListView<Funcionario> listViewFuncionarios;
  @FXML
  private Button buttonAdicionarFuncionario;
  @FXML
  private Button buttonRemoverFuncionario;
  
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		editableBox = new EditableHBox();
		BackupMenu backupMenu = new BackupMenu("./src/Backup/", "./src/Config/");
		menuMain.getItems().add(0, backupMenu.getMenu());

		Funcionario.setListaDeFuncionarios(DaoFactory.newDaoFile().carregarFuncionario());
		listViewFuncionarios.setFixedCellSize(25);
		listViewFuncionarios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		Produto.setListaDeProdutos(DaoFactory.newDaoFile().carregarProdutos());
		tableViewProdutos.setFixedCellSize(25);
		tableViewProdutos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		Controller.setListToComboBox(comboBoxOrdenarPor, OrdenarPor.getListOfAll(), 0);
		Controller.changeHowComboBoxDisplayItens(comboBoxOrdenarPor, e -> e.getName());

		for (Node node : Arrays.asList(listViewFuncionarios, tableViewProdutos, comboBoxOrdenarPor))
			node.setStyle("-fx-font-size: 14px; -fx-font-family: \"Lucida Console\";");
		
		Controller.addIconToButton(buttonAdicionarFuncionario, Icons.ICON_NEWITEM.getValue(), 20, 20, 200);
		Controller.addIconToButton(buttonRemoverFuncionario, Icons.ICON_DELETE.getValue(), 20, 20, 200);
		Controller.addIconToButton(buttonAdicionarProduto, Icons.ICON_NEWITEM.getValue(), 20, 20, 200);
		Controller.addIconToButton(buttonRemoverProduto, Icons.ICON_DELETE.getValue(), 20, 20, 200);

		setListViewFuncionariosFactories();
		setTableViewProdutosFactories();
		createHBoxWithButtons();
		addListeners();
		updateListaDeFuncionarios();
		updateListaDeProdutos();
	}

	private void addListeners() {
		buttonRemoverFuncionario.setOnAction(e -> excluirFuncionario());
		buttonRemoverProduto.setOnAction(e -> excluirProduto());
		listenerHandleOrdenarPor = new ListenerHandle<>(comboBoxOrdenarPor.valueProperty(), (obs, oldValue, newValue) -> updateListaDeProdutos());
		listenerHandleOrdenarPor.attach();
		buttonAdicionarFuncionario.setOnAction(e -> {
			Funcionario.addFuncionario(new Funcionario("Novo", ""));
			updateListaDeFuncionarios();
		});
		buttonAdicionarProduto.setOnAction(e -> {
			Produto.addProduto(new Produto("Novo", 0, 0));
			updateListaDeProdutos();
		});

	}

	public void init() {
	}

	private void createHBoxWithButtons() {
		hBoxWithButtonsFuncionariosSelected = new HBoxWithButtons(HBoxWitnButtonsMode.TEXT_AND_BUTTONS);
		hBoxWithButtonsFuncionariosSelected.setButtonSizeAndSpacing(18, 3);
		hBoxWithButtonsFuncionariosSelected.addButton(Icons.ICON_MOVEUP);
		hBoxWithButtonsFuncionariosSelected.addButton(Icons.ICON_MOVEDOWN);
		hBoxWithButtonsFuncionariosSelected.addButton(Icons.ICON_MOVEMAXUP);
		hBoxWithButtonsFuncionariosSelected.addButton(Icons.ICON_MOVEMAXDOWN);
		hBoxWithButtonsFuncionariosSelected.addButton(Icons.ICON_DELETE);
		hBoxWithButtonsFuncionariosSelected.setEditMode(HBoxWitnButtonsEditMode.TEXT_VIEW);
		
		hBoxWithButtonsFuncionariosHover = new HBoxWithButtons(HBoxWitnButtonsMode.TEXT_AND_BUTTONS);
		hBoxWithButtonsFuncionariosHover.setButtonSizeAndSpacing(18, 3);
		hBoxWithButtonsFuncionariosHover.addButton(Icons.ICON_MOVEUP);
		hBoxWithButtonsFuncionariosHover.addButton(Icons.ICON_MOVEDOWN);
		hBoxWithButtonsFuncionariosHover.addButton(Icons.ICON_MOVEMAXUP);
		hBoxWithButtonsFuncionariosHover.addButton(Icons.ICON_MOVEMAXDOWN);
		hBoxWithButtonsFuncionariosHover.addButton(Icons.ICON_DELETE);

		hBoxWithButtonsProdutosSelected = new HBoxWithButtons(HBoxWitnButtonsMode.ONLY_BUTTONS);
		hBoxWithButtonsProdutosSelected.setButtonSizeAndSpacing(18, 3);
		hBoxWithButtonsProdutosSelected.addButton(Icons.ICON_MOVEUP);
		hBoxWithButtonsProdutosSelected.addButton(Icons.ICON_MOVEDOWN);
		hBoxWithButtonsProdutosSelected.addButton(Icons.ICON_MOVEMAXUP);
		hBoxWithButtonsProdutosSelected.addButton(Icons.ICON_MOVEMAXDOWN);
		hBoxWithButtonsProdutosSelected.addButton(Icons.ICON_DELETE);
		hBoxWithButtonsProdutosSelected.setEditMode(HBoxWitnButtonsEditMode.TEXT_VIEW);

		hBoxWithButtonsProdutosHover = new HBoxWithButtons(HBoxWitnButtonsMode.ONLY_BUTTONS);
		hBoxWithButtonsProdutosHover.setButtonSizeAndSpacing(18, 3);
		hBoxWithButtonsProdutosHover.addButton(Icons.ICON_MOVEUP);
		hBoxWithButtonsProdutosHover.addButton(Icons.ICON_MOVEDOWN);
		hBoxWithButtonsProdutosHover.addButton(Icons.ICON_MOVEMAXUP);
		hBoxWithButtonsProdutosHover.addButton(Icons.ICON_MOVEMAXDOWN);
		hBoxWithButtonsProdutosHover.addButton(Icons.ICON_DELETE);
	}	

	private void updateHBoxWithButtonsFuncionariosListeners(HBoxWithButtons hBox, ListCell<Funcionario> cell) {
		hBox.setText(cell.getItem().getNome());
		hBox.setButtonOnAction(0, e -> {
			Misc.moveItemIndex(Funcionario.getUnmodifiableListaDeFuncionarios(), cell.getIndex(), -1);
			updateListaDeFuncionarios();
		});
		hBox.setButtonOnAction(1, e -> {
			Misc.moveItemIndex(Funcionario.getUnmodifiableListaDeFuncionarios(), cell.getIndex(), 1);
			updateListaDeFuncionarios();
		});
		hBox.setButtonOnAction(2, e -> {
			Misc.moveItemIndex(Funcionario.getUnmodifiableListaDeFuncionarios(), cell.getIndex(), Funcionario.getUnmodifiableListaDeFuncionarios().size() * -1);
			updateListaDeFuncionarios();
		});
		hBox.setButtonOnAction(3, e -> {
			Misc.moveItemIndex(Funcionario.getUnmodifiableListaDeFuncionarios(), cell.getIndex(), Funcionario.getUnmodifiableListaDeFuncionarios().size());
			updateListaDeFuncionarios();
		});
		hBox.setButtonOnAction(4, e -> {
			listViewFuncionarios.getSelectionModel().clearAndSelect(cell.getIndex());
			listViewFuncionarios.scrollTo(cell.getIndex());
			listViewFuncionarios.refresh();
			excluirFuncionario();
		});
		hBox.getButton(0).setDisable(cell.getIndex() == 0);
		hBox.getButton(1).setDisable(cell.getIndex() == Funcionario.getUnmodifiableListaDeFuncionarios().size() - 1);
		hBox.getButton(2).setDisable(cell.getIndex() == 0);
		hBox.getButton(3).setDisable(cell.getIndex() == Funcionario.getUnmodifiableListaDeFuncionarios().size() - 1);
	}
	
	private void setListViewFuncionariosFactories() {
		listViewFuncionarios.setCellFactory(lv -> {
			ListCell<Funcionario> cell = new ListCell<Funcionario>() {
				@Override
				public void updateItem(Funcionario funcionario, boolean empty) {
					super.updateItem(funcionario, empty);
					Controller.setListViewCellBackgroundColor(this);
					if (funcionario != null) {
						if (isSelected()) {
							hBoxWithButtonsFuncionariosSelected.setEditableTextField(listViewFuncionarios.getWidth() * 0.85, s -> {
								funcionario.setNome(s);
								hBoxWithButtonsFuncionariosSelected.setText(s);
							});
							setGraphic(hBoxWithButtonsFuncionariosSelected.getHBox());
							hBoxWithButtonsFuncionariosSelected.getHBoxButtons().setVisible(isHover());
							updateHBoxWithButtonsFuncionariosListeners(hBoxWithButtonsFuncionariosSelected, this);
						}
						else
							setGraphic(new Text(funcionario.getNome()));
					}
					else
						setGraphic(null);
				}
			};
			cell.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
				if (cell.getItem() == null)
					return;
				final Funcionario funcionario = cell.getItem();
				if (cell.isSelected())
					hBoxWithButtonsFuncionariosSelected.getHBoxButtons().setVisible(isNowHovered);
				else if (isNowHovered) {
					hBoxWithButtonsFuncionariosHover.setText(funcionario.getNome());
					cell.setGraphic(hBoxWithButtonsFuncionariosHover.getHBox());
					updateHBoxWithButtonsFuncionariosListeners(hBoxWithButtonsFuncionariosHover, cell);
				}
				else
					cell.setGraphic(new Text(funcionario.getNome()));
				Controller.setListViewCellBackgroundColor(cell);
			});
			return cell;
		});
	}

	private void updateHBoxWithButtonsProdutosListeners(HBoxWithButtons hBox, TableCell<Produto, Produto> cell) {
		hBox.setButtonOnAction(0, e -> {
			Misc.moveItemIndex(Produto.getUnmodifiableListaDeProdutos(), cell.getIndex(), -1);
			updateListaDeProdutos();
		});
		hBox.setButtonOnAction(1, e -> {
			Misc.moveItemIndex(Produto.getUnmodifiableListaDeProdutos(), cell.getIndex(), 1);
			updateListaDeProdutos();
		});
		hBox.setButtonOnAction(2, e -> {
			Misc.moveItemIndex(Produto.getUnmodifiableListaDeProdutos(), cell.getIndex(), Produto.getUnmodifiableListaDeProdutos().size() * -1);
			updateListaDeProdutos();
		});
		hBox.setButtonOnAction(3, e -> {
			Misc.moveItemIndex(Produto.getUnmodifiableListaDeProdutos(), cell.getIndex(), Produto.getUnmodifiableListaDeProdutos().size());
			updateListaDeProdutos();
		});
		hBox.setButtonOnAction(4, e -> {
			tableViewProdutos.getSelectionModel().clearAndSelect(cell.getIndex());
			tableViewProdutos.scrollTo(cell.getIndex());
			tableViewProdutos.refresh();
			excluirProduto();
		});
		hBox.getButton(0).setDisable(cell.getIndex() == 0);
		hBox.getButton(1).setDisable(cell.getIndex() == Produto.getUnmodifiableListaDeProdutos().size() - 1);
		hBox.getButton(2).setDisable(cell.getIndex() == 0);
		hBox.getButton(3).setDisable(cell.getIndex() == Produto.getUnmodifiableListaDeProdutos().size() - 1);
	}

	private void setTableViewProdutosFactories() {
		for (TableColumn<Produto, Produto> col : Arrays.asList(tableColumnProduto, tableColumnQuantidade, tableColumnPreco, tableColumnButtons)) {
			col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
			col.setCellFactory(lv -> {
				TableCell<Produto, Produto> cell = new TableCell<>() {
					@Override
					protected void updateItem(Produto produto, boolean empty) {
						super.updateItem(produto, empty);
						HBox hBox = new HBox();
						if (produto != null) {
							if (col.getId().equals("tableColumnProduto")) {
								hBox.getChildren().add(new Text(produto.getNome()));
								editableBox.setEditableHBoxThroughTextField(hBox, tableViewProdutos.getWidth() * 0.8, e -> {
									produto.setNome(e);
									tableViewProdutos.refresh();
								}); 
							}
							else if (col.getId().equals("tableColumnQuantidade")) {
								hBox.getChildren().add(new Text("" + produto.getQuantidade()));
								editableBox.setEditableHBoxThroughTextField(hBox, tableViewProdutos.getWidth() * 0.8, e -> {
									try {
										produto.setQuantidade(Integer.parseInt(e));
										updateListaDeProdutos();
									}
									catch (Exception ex)
										{ hBox.getChildren().set(0, new Text("" + produto.getQuantidade())); }
								}); 
							}
							else if (col.getId().equals("tableColumnPreco")) {
								hBox.getChildren().add(new Text(String.format("R$ %.2f", produto.getValor())));
								editableBox.setEditableHBoxThroughTextField(hBox, tableViewProdutos.getWidth() * 0.8, "" + produto.getValor(), e -> {
									try {
										produto.setValor(Double.parseDouble(e));
										updateListaDeProdutos();
									}
									catch (Exception ex)
										{ hBox.getChildren().set(0, new Text(String.format("R$ %.2f", produto.getValor()))); }
								}); 
							}
							else if (col.getId().equals("tableColumnButtons")) {
								Controller.setTableViewRowBackgroundColor(getTableRow());
								getTableRow().selectedProperty().addListener((obs, wasSelected, isSelected) -> {
									Controller.setTableViewRowBackgroundColor(getTableRow());
									if (isSelected) {
										setGraphic(hBoxWithButtonsProdutosSelected.getHBox());
										updateHBoxWithButtonsProdutosListeners(hBoxWithButtonsProdutosSelected, this);
									}
									else
										setGraphic(null);
								});
								getTableRow().hoverProperty().addListener((obs, wasHover, isHover) -> {
									Controller.setTableViewRowBackgroundColor(getTableRow());
									if (getItem() == null)
										return;
									if (isHover && !isSelected()) {
										setGraphic(hBoxWithButtonsProdutosHover.getHBox());
										updateHBoxWithButtonsProdutosListeners(hBoxWithButtonsProdutosHover, this);
									}
									else
										setGraphic(null);
								});
							}
						}
						setGraphic(hBox);
					}
				};
				return cell;
			});
		}
	}
	
	private void excluirFuncionario() {
		int total = listViewFuncionarios.getSelectionModel().getSelectedItems().size();
		if (Alerts.confirmation("Confirmação de exclusão", "Deseja mesmo excluir " +
				(total == 1 ? ("o funcionário " + listViewFuncionarios.getSelectionModel().getSelectedItem().getNome() + "?") :
				("os " + total + " funcionários selecionados?")))) {
					for (Funcionario f : listViewFuncionarios.getSelectionModel().getSelectedItems())
						Funcionario.removeFuncionario(f);
					updateListaDeFuncionarios();
				}
	}

	private void excluirProduto() {
		int total = tableViewProdutos.getSelectionModel().getSelectedItems().size();
		if (Alerts.confirmation("Confirmação de exclusão", "Deseja mesmo excluir " +
				(total == 1 ? ("o produto " + tableViewProdutos.getSelectionModel().getSelectedItem().getNome() + "?") :
				("os " + total + " produtos selecionados?")))) {
					for (Produto f : tableViewProdutos.getSelectionModel().getSelectedItems())
						Produto.removeProduto(f);
					updateListaDeProdutos();
				}
	}

	private void updateListaDeFuncionarios() {
		listViewFuncionarios.getSelectionModel().clearSelection();
		Controller.setListToListView(listViewFuncionarios, Funcionario.getUnmodifiableListaDeFuncionarios());
		listViewFuncionarios.refresh();
	}

	private void updateListaDeProdutos() {
		tableViewProdutos.getSelectionModel().clearSelection();
		if (comboBoxOrdenarPor.getSelectionModel().getSelectedItem() == OrdenarPor.PRECO)
			Produto.sort((p1, p2) -> p1.getValor() > p2.getValor() ? 1 : p1.getValor() < p2.getValor() ? -1 : 0);
		else if (comboBoxOrdenarPor.getSelectionModel().getSelectedItem() == OrdenarPor.QUANTIDADE)
			Produto.sort((p1, p2) -> p1.getQuantidade() > p2.getQuantidade() ? 1 : p1.getQuantidade() < p2.getQuantidade() ? -1 : 0);
		else
			Produto.sort((p1, p2) -> p1.getNome().compareTo(p2.getNome()));
		Controller.setListToTableView(tableViewProdutos, Produto.getUnmodifiableListaDeProdutos());
		labelTotalFuncionarios.setText("" + Funcionario.getUnmodifiableListaDeFuncionarios().size());
		labelTotalProdutos.setText("" + Produto.getUnmodifiableListaDeProdutos().size());
		labelTotalProdutosQuantidade.setText("" + Produto.getUnmodifiableListaDeProdutos()
			.stream().map(e -> e.getQuantidade()).reduce(0, (x, y) -> x + y));
		labelValorTotal.setText(String.format("R$ %.2f", Produto.getUnmodifiableListaDeProdutos()
			.stream().map(e -> e.getValor() * e.getQuantidade()).reduce(0.0, (x, y) -> x + y)));
		tableViewProdutos.refresh();
	}

}
