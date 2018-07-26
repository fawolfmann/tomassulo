package tomasulo;

public class CDBus extends Thread {
	private Mult_ER mer;
	private Registros_FP regs;
	private Multiplicador multi;

	public CDBus(Mult_ER mer_in ,Registros_FP regs_in , Multiplicador multi_in){
		this.mer = mer_in;
		this.regs = regs_in;
		this.multi = multi_in;
		
	}
	public void run(){
		set_operator();
		try_multiply();
	}	
	//Ve si las estaciones se pueden ejecutar
	public void set_operator(){
		Registro reg_temp;
		int value;
		for (int i=0; i<mer.get_ER().size(); i++){
			reg_temp = mer.get_registro(i);
			if(reg_temp.getValorj() > 0 && reg_temp.getValork() > 0  ){
				//Tiene los operandos mando al multiplicador
				if(!multi.isBusy()){
					multi.values_in(reg_temp.getValorj(), reg_temp.getValorj(),reg_temp.getTag() );
				}
			}
		}
	}
	
	public void try_multiply(){
		String returnTag;
		int retutnValue;
		if(multi.isBusy()){
			retutnValue = multi.multiply();
			if (retutnValue > -1){
				returnTag = multi.getTag();
				//Con el resultado veo si alguno de las otras estaciones lo necesita a ese tag
				check_tag(returnTag, retutnValue);
			}
			else{
				//Tengo que seguir esperando.
				
			}
		}
		else{
			//Estacion no ocupada.
		}
	}
	
	//con el resultado de la estacion veo si alguien necesita ese tag
	public void check_tag(String tag, int value){
		//itero sobre todas las ER viendo si algun Qj o Qk es igual al tag, si es asi le doy el valor.
		Registro reg_temp;
		
		//en la ER multiplicacion 
		for (int i=0; i<mer.get_ER().size(); i++){
			reg_temp = mer.get_registro(i);
			if(reg_temp.getQj().equals(tag) ){
				reg_temp.setValorj(value);
			}
			if(reg_temp.getQk().equals(tag) ){
				reg_temp.setValork(value);
			}
		}
		// Los registros FP
		for (int i=0; i<regs.get_ER().size(); i++){
			reg_temp = regs.get_registro(i);
			if(reg_temp.getQj().equals(tag) ){
				reg_temp.setValorj(value);
			}
		}
		
	}
	

}
