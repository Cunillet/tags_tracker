package com.cunill.contentcheck.controller;

import java.net.URL;
import java.util.ResourceBundle;
import org.jetbrains.annotations.NotNull;
import com.cunill.contentcheck.utils.StringUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FrameController implements Initializable {

	private Scene loginScene;
	private Scene checkScene;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}

	protected void appendTextArea(final String text, TextArea textArea) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				textArea.appendText(text + StringUtils.NEW_LINE);
				textArea.setScrollTop(Double.MAX_VALUE);
				textArea.positionCaret(Integer.MAX_VALUE);
			}
		});
	}
	
	protected void appendText(final String val, Text text) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				text.setText(text.getText() + val);
			}
		});
	}

	private void loadScene(Scene scene, String sceneName) {
		Task<Void> task = new Task<Void>() {
			{
				setOnSucceeded(workerStateEvent -> {
					openScene(scene);
				});

				setOnFailed(workerStateEvent -> {
				});
			}

			@Override
			public Void call() {
				return null;
			}
		};
		@NotNull
		Thread loadingThread = new Thread(task, "switch to " + sceneName + " scene");
		loadingThread.setDaemon(true);
		loadingThread.start();
	}

	public void loadCheckScene() {
		loadScene(checkScene, "check");
	}

	public void close() {
		Platform.exit();
	}

	public void openScene(Scene newScene) {

		Stage primaryStage = getCurrentStage();
		primaryStage.setScene(newScene);
		primaryStage.centerOnScreen();
	}

	private Stage getCurrentStage() {

		Stage currentStage = (Stage) loginScene.getWindow();
		if (currentStage != null) {
			return currentStage;
		}
		currentStage = (Stage) checkScene.getWindow();
		if (currentStage != null) {
			return currentStage;
		}
		return null;
	}
}
