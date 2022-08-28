package supervisorio;

import java.net.URL;
import java.util.Enumeration;
import java.util.ResourceBundle;

import gnu.io.CommPortIdentifier;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import serial.ConexaoSerial;
import serial.Parametros;
import serial.Protocolo;

/**
 * FXML Controller class
 *
 * @author fschi
 */
public class FXMLTelaPrincipalController implements Initializable {

	public static double temperatura;
	public static double tensao;
	public static double corrente;
	public static double potencia;
	private static double pan = 0.0;
	private static double tilt = 0.0;
	public static double split5;
	public static double split6;

	int count = 0;

	@FXML
	private Button btnConectar;

	@FXML
	private Button btnDesconectar;

	@FXML
	private Button btnPanInc;

	@FXML
	private Button btnPanDec;

	@FXML
	private Button btnTiltInc;

	@FXML
	private Button btnTiltDec;

	@FXML
	private Button enviar1;

	@FXML
	private Button enviar2;

	@FXML
	private ComboBox<String> cmbPortas;

	@FXML
	private ComboBox<String> cmbBaudRate;

	@FXML
	private Slider slPan;

	@FXML
	private Slider slTilt;

	@FXML
	private Label lblSliderPan;

	@FXML
	private Label lblSliderTilt;

	@FXML
	private Label lbl1;

	@FXML
	private Label lbl2;
	
	@FXML
	private Label lbl3;

	@FXML
	private Label lbl4;
	
	@FXML
	private Label lbl5;

	@FXML
	private Label lbl6;

	ConexaoSerial conexao = new ConexaoSerial();

	Timeline timerAtualizar;

	/**
	 * Initializes the controller class.
	 */
	public void initialize(URL url, ResourceBundle rb) {

		ObservableList<String> list = FXCollections.observableArrayList("1200", "2400", "4800", "9600", "19200",
				"38400", "57600", "115200");
		cmbBaudRate.setItems(list);

		// carregar o método para povoar o combobox das portas COM
		carregarPortas();

		slPan.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::slPanChanged);
		slTilt.addEventHandler(MouseEvent.MOUSE_DRAGGED, this::slTiltChanged);

		lblSliderPan.setText(String.format("%1.2f", pan));
		lblSliderTilt.setText(String.format("%1.2f", tilt));

		// criar o timer para atualizar os dados no supervisório vindo da serial
		timerAtualizar = new Timeline(new KeyFrame(Duration.millis(200), event -> {
			atualizarDadosSerial();
		}));
		timerAtualizar.setCycleCount(Timeline.INDEFINITE);
	}

	public void exitApplication() {
		conexao.close();
		System.out.println("stop");
	}

	@FXML
	private void selectBaud(ActionEvent event) {
		Integer s = Integer.parseInt(cmbBaudRate.getSelectionModel().getSelectedItem().toString());
		Parametros.baudRate = s;
	}

	@FXML
	private void desconectarArduino(ActionEvent event) {

		try {
			// timerAtualizar.stop();// Finaliza o timer
			conexao.close();
			cmbPortas.setDisable(false);// ativar o combobox para segurança
			cmbBaudRate.setDisable(false);
			btnConectar.setDisable(false);
			btnDesconectar.setDisable(true);// desabilitar o botão

			System.out.println("Fechou a conexão");
		} catch (Exception e) {
			System.out.println("Não fechou a conexão" + e.toString());
		}

	}

	// método para carregas as portas COM do computador e mostra no combobox
	public void carregarPortas() {
		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();

		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPorId = portEnum.nextElement();

			cmbPortas.getItems().add(currPorId.getName());
		}
	}

	@FXML
	private void conectarArduino(ActionEvent event) {

		try {

			Parametros.nomePorta = cmbPortas.getSelectionModel().getSelectedItem().toString();

			conexao.iniciar();
			cmbBaudRate.setDisable(true);
			cmbPortas.setDisable(true);// ativar o combobox para segurança
			btnConectar.setDisable(true);
			btnDesconectar.setDisable(false);// desabilitar o botão

			System.out.println("Fez a conexão.");

			System.out.println("Teste ==============" + Parametros.nomePorta);

			// inicializar o timer
			timerAtualizar.play();
		} catch (Exception e) {
			System.out.println("Não fez a conexão." + e.toString());
			e.printStackTrace();
		}
	}

	@FXML
	private void slPanChanged(MouseEvent event) {
		lblSliderPan.setText(String.format("%1.2f", slPan.getValue()));
		pan = slPan.getValue();
	}

	@FXML
	private void slTiltChanged(MouseEvent event) {
		lblSliderTilt.setText(String.format("%1.2f", slTilt.getValue()));
		tilt = slTilt.getValue();
	}

	@FXML
	private void slPanInc(ActionEvent event) {
		pan++; // = pan + 1.0;
		slPan.setValue(pan);
		lblSliderPan.setText(String.format("%1.2f", pan));
	}

	@FXML
	private void slPanDec(ActionEvent event) {
		pan = pan - 1.0;
		slPan.setValue(pan);
		lblSliderPan.setText(String.format("%1.2f", pan));
	}

	@FXML
	private void slTiltInc(ActionEvent event) {
		tilt = tilt + 1;
		slTilt.setValue(tilt);
		lblSliderTilt.setText(String.format("%1.2f", tilt));
		System.out.println(tilt);
	}

	@FXML
	private void slTiltDec(ActionEvent event) {
		tilt = tilt - 1;
		slTilt.setValue(tilt);
		lblSliderTilt.setText(String.format("%1.2f", tilt));
		System.out.println(tilt);
	}

	@FXML
	private void enviar1(ActionEvent event) {
		conexao.enviarDados("[20]");
	}

	@FXML
	private void enviar2(ActionEvent event) {
		conexao.enviarDados("[30]");
	}

	// método para atualizar o supervisório com os dados vindo da serial
	public void atualizarDadosSerial() {

		// ProtocoloIrrigacao.tipoProtocolo;

		temperatura = Protocolo.tempetatura;
		tensao = Protocolo.tensao;
		corrente = Protocolo.corrente;
		potencia = Protocolo.potencia;
		split5 = Protocolo.pan;
		split6 = Protocolo.tilt;
		// pan = ProtocoloIrrigacao.pan;
		// tilt = ProtocoloIrrigacao.tilt;

		lbl1.setText(String.format("%1.2f", temperatura));
		lbl2.setText(String.format("%1.2f", tensao));
		lbl3.setText(String.format("%1.2f", corrente));
		lbl4.setText(String.format("%1.2f", potencia));
		lbl5.setText(String.format("%1.2f", split5));
		lbl6.setText(String.format("%1.2f", split6));
	}
}