package tomasulo;

public class Registro{
	private String Op;
	private String Qj;
	private int Valorj;
	private String Qk;
	private int Valork;
	private boolean Busy;
	private String tag;
	
	public Registro(){
		setOp("");
		setQj("");
		setValorj(-1);
		setQk("");
		setValork(-1);
		setBusy(false);
		setTag("");
	}
	//Registros genericos, inicializados en forma invalida, cuando una ER los contruye los inicializa
	// con lo que va a utilizar
	public String getOp() {
		return Op;
	}

	public void setOp(String op) {
		this.Op = op;
	}

	public String getQj() {
		return Qj;
	}

	public void setQj(String qj) {
		Qj = qj;
	}

	public int getValorj() {
		return Valorj;
	}

	public void setValorj(int valorj) {
		Valorj = valorj;
	}

	public String getQk() {
		return Qk;
	}	

	public void setQk(String qk) {
		Qk = qk;
	}

	public int getValork() {
		return Valork;
	}

	public void setValork(int valork) {
		Valork = valork;
	}

	public boolean isBusy() {
		return Busy;
	}

	public void setBusy(boolean busy) {
		Busy = busy;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
}