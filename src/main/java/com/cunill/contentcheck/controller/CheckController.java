package com.cunill.contentcheck.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;

import com.cunill.contentcheck.connection.CountWebContent;
import com.cunill.contentcheck.utils.StringUtils;

import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CheckController extends FrameController {

	// Proxy elements
	public TextField domainTextField;
	public TextField userTextField;
	public TextField pwdTextField;
	public TextField addressTextField;
	public TextField portTextField;
	public Button useProxyButton;
	private boolean proxyDisabled = true;

	public TextField urlTextField;
	public TextField classTextField;
	public Button countButton;
	public TextArea resultTextArea;
	public ImageView loadingImg;

	protected final Log log = LogFactory.getLog(this.getClass());

	public void handleCheckWebContent() {
		initializeTasks();
		Task<Void> task = new Task<Void>() {
			{
				setOnSucceeded(workerStateEvent -> {
					endLoading();
				});

				setOnFailed(workerStateEvent -> {
					endLoading();
				});
			}

			@Override
			public Void call() {
				CountWebContent cwc = new CountWebContent();
				if (StringUtils.isEmpty(urlTextField.getText()) || StringUtils.isEmpty(classTextField.getText())) {
					appendTextArea("ERROR --> please review" + StringUtils.NEW_LINE, resultTextArea);
					appendTextArea("--> full url required" + StringUtils.NEW_LINE, resultTextArea);
					appendTextArea("--> element class searched required", resultTextArea);
					return null;
				}
				appendTextArea("Initializing process...", resultTextArea);
				appendTextArea("Searched element: " + classTextField.getText() + StringUtils.NEW_LINE, resultTextArea);
				if (proxyDisabled) {
					cwc.setCommonConfiguration();
				} else {
					if ((StringUtils.isNotEmpty(domainTextField.getText())
							|| StringUtils.isNotEmpty(userTextField.getText())
							|| StringUtils.isNotEmpty(pwdTextField.getText())
							|| StringUtils.isNotEmpty(addressTextField.getText())
							|| StringUtils.isNotEmpty(portTextField.getText()))
							&& StringUtils.isNumeric(portTextField.getText())) {
						cwc.setCommonConfigurationWithProxy(addressTextField.getText(),
								Integer.parseInt(portTextField.getText()), userTextField.getText(),
								pwdTextField.getText(), domainTextField.getText());
					} else {
						appendTextArea("ERROR --> please review Proxy fields!" , resultTextArea);
						return null;
					}
				}
				int countResult = cwc.countClassElements(urlTextField.getText(), classTextField.getText());
				appendTextArea("resulting number of searched element: " + countResult, resultTextArea);
				return null;
			}
		};
		@NotNull
		Thread loadingThread = new Thread(task, "Check web responses");
		loadingThread.setDaemon(true);
		loadingThread.start();
	}

	public void handleSwitchProxyPreferences() {
		proxyDisabled = !proxyDisabled;
		setProxyDisabledEnabled(proxyDisabled);
	}

	/**
	 * Value obtained defines if fields are DISABLED
	 */
	private void setProxyDisabledEnabled(boolean proxyDisabled) {
		domainTextField.setDisable(proxyDisabled);
		userTextField.setDisable(proxyDisabled);
		pwdTextField.setDisable(proxyDisabled);
		addressTextField.setDisable(proxyDisabled);
		portTextField.setDisable(proxyDisabled);
	}

	private void initializeTasks() {
		try {
			resultTextArea.setText("");
			setProxyDisabledEnabled(true);
			urlTextField.setDisable(true);
			classTextField.setDisable(true);
			// loading img
			FileInputStream inputstream = new FileInputStream("./img/loading.gif");
			loadingImg.setImage(new Image(inputstream));
			loadingImg.setVisible(true);
		} catch (FileNotFoundException e) {
			log.error("ERROR || initializeTasks encountred an error while initializing values");
		}
	}

	private void endLoading() {
		setProxyDisabledEnabled(proxyDisabled);
		urlTextField.setDisable(false);
		classTextField.setDisable(false);
		loadingImg.setVisible(false);
	}
}
