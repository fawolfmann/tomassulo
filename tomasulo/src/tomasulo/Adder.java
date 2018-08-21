package tomasulo;

public class Adder extends Operator{

	public Adder(int delay) {
		super(delay);
	}
	
	@Override
	public int operate(){
		//System.out.println("[Adder] add busy " +this.timeStamp+" " + System.currentTimeMillis() );
		if(this.timeStamp+delayTime < System.currentTimeMillis()){
			//System.out.println("[Adder] completed " );
			setBusy(false);
			timeStamp = 0;
			return valorj + valork;
		}
		else{
		return -1;
		}
	}
	
	public void values_in(int valor1 , int valor2 , String tag1){
		super.values_in(valor1, valor2, tag1);
	}

}
