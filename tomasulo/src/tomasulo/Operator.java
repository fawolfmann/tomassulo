package tomasulo;

public class Operator {
	protected int valorj;
	protected int valork;
	protected boolean busy;
	protected long timeStamp;
	protected int returnValue;
	protected int delayTime;
	protected String tag;
	
	public Operator(int delay){
		valorj = 0;
		valork = 0 ;
		setBusy(false);
		this.delayTime = (int) (delay*0.9);
	}
	
	//ingresan los valores al multiplicador
	protected void values_in(int valor1 , int valor2 , String tag1){
		setBusy(true);
		setValorj(valor1);
		setValork(valor2);
		setTag(tag1);
		//inicia timestamp
		timeStamp=System.currentTimeMillis();	
		//System.out.println("[Operator] values in " +timeStamp );
	}
	public void values_in(int valor1, String tag1) {
		setBusy(true);
		setValorj(valor1);
		setTag(tag1);
		//inicia timestamp
		timeStamp=System.currentTimeMillis();	
		//System.out.println("[Operator] values in " +timeStamp );		
	}
//	Si paso el tiempo definido puedo operar
//	IMPORTANTE luego de operar, buscar el tag, con getTag()
	public int operate(){
		int return_value = 0;
		return return_value;
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
