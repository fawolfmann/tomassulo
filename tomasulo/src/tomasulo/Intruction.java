package tomasulo;

public class Intruction {
	private String intruccion;
	private boolean dispatch;
	
	 Intruction (String inst){
		 this.intruccion = inst;
		 this.dispatch = false;		 
	 }
	 
	public String getIntruccion() {
		return intruccion;
	}
	public void setIntruccion(String intruccion) {
		this.intruccion = intruccion;
	}
	public boolean isDispatch() {
		return dispatch;
	}
	public void setDispatch(boolean dispatch) {
		this.dispatch = dispatch;
	}

}
