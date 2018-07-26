package tomasulo;


public class Multiplicador {
	private int valorj;
	private int valork;
	private boolean busy;
	private long timeStamp;
	private int returnValue;
	private int delayTime;
	private String tag;
	
	public Multiplicador(int delay){
		valorj = 0;
		valork = 0 ;
		setBusy(false);
		this.delayTime = delay;
	}
	
	//ingresan los valores al multiplicador
	public void values_in(int valor1 , int valor2 , String tag1){
		setBusy(true);
		setValorj(valor1);
		setValork(valor2);
		setTag(tag1);
		//inicia timestamp
		long timeStamp=System.currentTimeMillis();		
	}
//	Si paso el tiempo definido puedo multiplicar
//	IMPORTANTE luego de multiplicar, buscar el tag, con getTag()
	public int multiply(){
		if(timeStamp+delayTime > System.currentTimeMillis()){
			setBusy(false);
			return valorj * valork;
		}
		else{
			return -1;
		}		
	}

	public int getValork() {
		return valork;
	}

	public void setValork(int valork) {
		this.valork = valork;
	}

	public int getValorj() {
		return valorj;
	}

	public void setValorj(int valorj) {
		this.valorj = valorj;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
