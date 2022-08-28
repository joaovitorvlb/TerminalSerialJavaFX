package serial;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 *
 * @author fschi
 */
public class ConexaoSerial implements SerialPortEventListener {

	SerialPort serialport;
	private BufferedReader input;
	private OutputStream output;
	private static final int TIME_OUT = 2000;// em millesegundos
	private static final int DATA_RATE = 9600;// velocidade de comunicação do arduino

	// método para inciar a comunicação
	public void iniciar() {
		String serialPortName = Parametros.nomePorta;

		CommPortIdentifier portId = null;

		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPorId = (CommPortIdentifier) portEnum.nextElement();

			if (currPorId.getName().equals(serialPortName)) {
				portId = currPorId;
				System.out.println("Conectado com sucesso na porta: " + currPorId.getName());
				break;
			}
		}

		if (portId == null) {
			System.out.println("Não foi possível procurar a porta COM");
			System.out.println(serialPortName);
			return;
		}

		try {
			// abrir a porta serial
			serialport = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

			// os parametros da porta serial
			serialport.setSerialPortParams(Parametros.baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// abris as strems
			serialport.addEventListener(this);
			serialport.notifyOnDataAvailable(true);

			Thread t = new Thread() {
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.err.println(e.toString());
					}
				}
			};

			t.start();
			System.out.println("Iniciando...");

		} catch (Exception e) {
			System.err.println(e.toString());
		}

	}// fim método para inciar a comunicação

	// método para fechar a comunicação com a porta serial
	public synchronized void close() {
		System.out.println("Close");

		if (serialport != null) {
			serialport.removeEventListener();
			serialport.close();
			System.out.println("Fechada a conexão com a porta serial: " + serialport.getName());
		}

	}// fim método para fechar a comunicação com a porta serial

	// método para enviar dados
	public void enviarDados(String dados) {
		try {
			output = serialport.getOutputStream();
			output.write(dados.getBytes());
		} catch (Exception e) {
			System.err.println("Erro ao enviar dados: " + e.toString());
		}
	}// fim método para enviar dados

	// Método para saber da chegada de dados
	public void serialEvent(SerialPortEvent spe) {

		try {
			switch (spe.getEventType()) {
			case SerialPortEvent.DATA_AVAILABLE:
				if (input == null) {
					input = new BufferedReader(new InputStreamReader(serialport.getInputStream()));
				}

				if (input.ready()) {
					String inputLine = input.readLine();
					System.out.println("Mensegem que chegou no serialEvent: " + inputLine);// é para teste
					Protocolo.lerProtocolo = inputLine;
					receberProtocolo();
				}

				break;

			default:
				break;
			}
		} catch (Exception e) {
			System.err.println("Erro no evento de recepção de dados: " + e.toString());
			e.printStackTrace();
		}
	}// fim Método para saber da chegada de dados

	public void receberProtocolo() {
		// Protocolo
		// $%&;100.0;100.0;12;150;150;150;150;150;150;40;50;40;50;40;0;0;0;0;0;1;1;1;1

		String separaPro[] = Protocolo.lerProtocolo.split(":");
		Protocolo.tipoProtocolo = separaPro[0];// $%&
		Protocolo.tempetatura = Double.valueOf(separaPro[1]);
		Protocolo.tensao = Double.valueOf(separaPro[2]);
		Protocolo.corrente = Double.valueOf(separaPro[3]);
		Protocolo.potencia = Double.valueOf(separaPro[4]);
		Protocolo.pan = Double.valueOf(separaPro[5]);
		Protocolo.tilt = Double.valueOf(separaPro[6]);
	}
}
