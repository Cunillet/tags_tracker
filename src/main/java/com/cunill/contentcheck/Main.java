package com.cunill.contentcheck;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader checkPaneLoader = new FXMLLoader(getClass().getResource("/com/cunill/contentcheck/frame/CheckFrame.fxml"));
		Parent checkPane = checkPaneLoader.load();
		Scene checkScene = new Scene(checkPane);
		
		primaryStage.setTitle("Count web elements");
		primaryStage.setScene(checkScene);
		primaryStage.show();
	}
}
