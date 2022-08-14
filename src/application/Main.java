package application;

import java.util.Locale;

import dao.DaoFactory;
import gui.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.Alerts;

public class Main extends Application {

	@Override
	public void start(Stage stage) {
		try {
			Locale.setDefault(new Locale("es"));

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainWindowView.fxml"));
			VBox vBox = loader.load();
			Scene scene = new Scene(vBox);
			stage.setScene(scene);
			MainWindowController controller = loader.getController();
			stage.setTitle("Teste");
			stage.setResizable(false);
			controller.init();
			stage.show();
		}
		catch (Exception e)
			{ Alerts.exception("Erro", "Erro ao iniciar o programa", e.getMessage(), e); }
	}
	
	public static void main(String[] args) {
		launch(args);
		DaoFactory.newDaoFile().salvarFuncionario();
		DaoFactory.newDaoFile().salvarProdutos();
	}

}
