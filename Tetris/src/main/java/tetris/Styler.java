package tetris;

import javafx.scene.control.Button;

//The Styler class is responsible for styling UI components
public class Styler {

	// Sets the style of the given button to a non-active style
	public static void setBtnStyle(Button startBtn) {
		startBtn.setStyle("-fx-background-color: \r\n"
				+ "        #000000,\r\n"
				+ "        linear-gradient(#7ebcea, #2f4b8f),\r\n"
				+ "        linear-gradient(#426ab7, #263e75),\r\n"
				+ "        linear-gradient(#395cab, #223768);\r\n"
				+ "    -fx-background-insets: 0,1,2,3;\r\n"
				+ "    -fx-background-radius: 3,2,2,2;\r\n"
				+ "    -fx-padding: 12 30 12 30;\r\n"
				+ "    -fx-text-fill: white;\r\n"
				+ "    -fx-font-size: 12px;");
	}

	// Sets the style of the given button to an active style
	public static void setBtnStyleActive(Button stopBtn) {
		stopBtn.setStyle("-fx-background-color: \r\n"
				+ "        #000000,\r\n"
				+ "        linear-gradient(#7ebcea, #2f4b8f),\r\n"
				+ "        linear-gradient(#426ab7, #263e75),\r\n"
				+ "        linear-gradient(#395cab, #223768);\r\n"
				+ "    -fx-background-insets: 0,1,2,3;\r\n"
				+ "    -fx-background-radius: 3,2,2,2;\r\n"
				+ "    -fx-padding: 12 30 12 30;\r\n"
				+ "    -fx-text-fill: white;\r\n"
				+ "    -fx-font-size: 12px;");
	}

}
