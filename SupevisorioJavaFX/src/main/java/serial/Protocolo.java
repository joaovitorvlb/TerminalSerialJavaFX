package serial;

public class Protocolo {

	// atributos static
	public static double tempetatura = 0.0;
	public static double tensao = 0.0;
	public static double corrente = 0.0;
	public static double potencia = 0.0;
	public static double pan = 0.0;
	public static double tilt = 0.0;

	// ler o Protocolo inteiro
	public static String lerProtocolo;

	// parta saber o tipo do protocolo
	public static String tipoProtocolo;

	public static double getTempetatura() {
		return tempetatura;
	}

	public static void setTempetatura(double tempetatura) {
		Protocolo.tempetatura = tempetatura;
	}

	public static double getTensao() {
		return tensao;
	}

	public static void setTensao(double tensao) {
		Protocolo.tensao = tensao;
	}

	public static double getCorrente() {
		return corrente;
	}

	public static void setCorrente(double corrente) {
		Protocolo.corrente = corrente;
	}

	public static double getPotencia() {
		return potencia;
	}

	public static void setPotencia(double potencia) {
		Protocolo.potencia = potencia;
	}

	public static String getLerProtocolo() {
		return lerProtocolo;
	}

	public static void setLerProtocolo(String lerProtocolo) {
		Protocolo.lerProtocolo = lerProtocolo;
	}

	public static String getTipoProtocolo() {
		return tipoProtocolo;
	}

	public static void setTipoProtocolo(String tipoProtocolo) {
		Protocolo.tipoProtocolo = tipoProtocolo;
	}

	public static double getPan() {
		return pan;
	}

	public static void setPan(double pan) {
		Protocolo.pan = pan;
	}

	public static double getTilt() {
		return tilt;
	}

	public static void setTilt(double tilt) {
		Protocolo.tilt = tilt;
	}
}
