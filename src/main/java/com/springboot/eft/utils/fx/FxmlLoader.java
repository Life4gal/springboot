package com.springboot.eft.utils.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;

public class FxmlLoader {

	private static final Logger logger = Logger.getLogger(FxmlLoader.class);

	private FxmlLoader() {
	}

	/**
	 * 加密fxml文件
	 *
	 * @param url {@link URL}
	 * @return {@link Scene}
	 * @throws IOException 异常
	 */
	public static Scene loadFxml(URL url) throws IOException {
		logger.info("load fxml from url: " + url);
		BorderPane root = FXMLLoader.load(url);
		return new Scene(root);
	}
}
