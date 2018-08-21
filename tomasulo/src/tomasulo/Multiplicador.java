package tomasulo;


public class Multiplicador extends Operator {
	public Multiplicador(int delay) {
		super(delay);
	}
	
	@Override
	public int operate(){
		//System.out.println("[Multi] busy " +this.timeStamp+" " + System.currentTimeMillis() +" "+ (long)(this.timeStamp+delayTime-System.currentTimeMillis()) );
		if(this.timeStamp+delayTime < System.currentTimeMillis()){
			//System.out.println("[Multi] completed " );
			setBusy(false);
			timeStamp = 0;
			return (int)valorj * valork;
		}
		else{
		return -1;
		}
	}
	
	public void values_in(int valor1 , int valor2 , String tag1){
		super.values_in(valor1, valor2, tag1);
	}
}
