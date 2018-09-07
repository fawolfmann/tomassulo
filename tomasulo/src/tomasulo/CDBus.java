package tomasulo;

public class CDBus extends Thread {
	private Estacion_reserva mer;
	private Estacion_reserva aer;
	private Estacion_reserva ler;
	private Estacion_reserva ser;
	private Registros_FP regs;
	private Multiplicador multi;
	private Loader load;
	private Adder add;
	private Storer store;

	public CDBus(Estacion_reserva aer_in, Estacion_reserva mer_in , Estacion_reserva ler_in ,Estacion_reserva ser_in, Registros_FP regs_in , Multiplicador multi_in, Loader load_in, Storer store_in, Adder add_in){
		this.mer = mer_in;
		this.aer = aer_in;
		this.ler = ler_in;
		this.ser = ser_in;
		this.regs = regs_in;
		this.multi = multi_in;
		this.load = load_in;
		this.store = store_in;
		this.add = add_in;
	}
	
	public void run(){
		set_operator();
		try_operate();
	}	
	//Ve si las estaciones se pueden ejecutar
	public void set_operator(){
		Registro reg_temp;
		
		for (int i=0; i<mer.get_ER().size(); i++){
			reg_temp = mer.get_registro(i);
			if(reg_temp.getValorj() > 0 && reg_temp.getValork() > 0  && mer.get_registro(i).isBusy()){
				//Tiene los operandos mando al multiplicador
				if(!multi.isBusy()){
					multi.values_in(reg_temp.getValorj(), reg_temp.getValork(),reg_temp.getTag() );
					mer.get_registro(i).setBusy(false);

				}
			}
		}
		for (int i=0; i<aer.get_ER().size(); i++){
			reg_temp = aer.get_registro(i);
			//System.out.println("[CDB] to dispatch 1 " +reg_temp.getTag()+  reg_temp.getValorj() +reg_temp.getValork() );
			if(reg_temp.getValorj() > 0 && reg_temp.getValork() > 0  && aer.get_registro(i).isBusy()){
				//Tiene los operandos mando al multiplicador
				if(!add.isBusy()){
					add.values_in(reg_temp.getValorj(), reg_temp.getValork(),reg_temp.getTag() );
					aer.get_registro(i).setBusy(false);

					System.out.println("[CDB] dispached " + reg_temp.getValorj() +reg_temp.getValork() );
				}
			}
		}
		for (int i=0; i<ler.get_ER().size(); i++){
			reg_temp = ler.get_registro(i);
			//System.out.println("[CDB] to dispatch ler " +reg_temp.getTag()+  reg_temp.getValorj());
			if(reg_temp.getValorj() > 0 && ler.get_registro(i).isBusy() ){
				//Tiene los operandos mando al multiplicador
				if(!load.isBusy()){
					load.values_in(reg_temp.getValorj(),reg_temp.getTag() );
					ler.get_registro(i).setBusy(false);

					//System.out.println("[CDB] dispached ler" + reg_temp.getValorj() );
				}
			}
		}
		for (int i=0; i<ser.get_ER().size(); i++){
			reg_temp = ser.get_registro(i);
			if(reg_temp.getValorj() > 0 && ser.get_registro(i).isBusy() ){
				//Tiene los operandos mando al multiplicador
				if(!store.isBusy()){
					store.values_in(reg_temp.getValorj(), reg_temp.getValork(),reg_temp.getTag() );
					ser.get_registro(i).setBusy(false);
				}
			}
		}
	}
	
	public void try_operate(){
		String returnTag = null;
		int retutnValue = -1;
		if(multi.isBusy()){
			retutnValue = multi.operate();
			if (retutnValue > -1){
				if(multi.getTag().contains("MUL")){
					//System.out.println("[CDB] multi --------" +multi.getTag() + " "+ retutnValue);
					returnTag = multi.getTag();
					//Con el resultado veo si alguno de las otras estaciones lo necesita a ese tag
					check_tag(returnTag, retutnValue);
				}
			}
		}
		if(load.isBusy()){
			//System.out.println("[CDB] load busy " +load.isBusy());
			retutnValue = load.operate();
			if (retutnValue > -1){
				//System.out.println("[CDB] load " +load.getTag() + " "+ load.getValorj());
				returnTag = load.getTag();
				//Con el resultado veo si alguno de las otras estaciones lo necesita a ese tag
				check_tag(returnTag, retutnValue);
			}
		}
		if(store.isBusy()){
			retutnValue = store.operate();
			if (retutnValue > -1){
				returnTag = store.getTag();
				//Con el resultado veo si alguno de las otras estaciones lo necesita a ese tag
				check_tag(returnTag, retutnValue);
			}
		}
		if(add.isBusy()){
			//System.out.println("[CDB] add busy " +add.isBusy());
			retutnValue = add.operate();
			//System.out.println("[CDB] add result " +retutnValue);
			if (retutnValue > -1){
				returnTag = add.getTag();
				//System.out.println("[CDB] tag " +add.getTag());
				check_tag(returnTag, retutnValue);
			}
		}
		//System.out.println("[CDB] operate " +returnTag + " "+ retutnValue );

		
	}
	
	//con el resultado de la estacion veo si alguien necesita ese tag
	public void check_tag(String tag, int value){
		//itero sobre todas las ER viendo si algun Qj o Qk es igual al tag, si es asi le doy el valor.
		Registro reg_temp = null;
		
		//en la ER multiplicacion 
		for (int i=0; i<mer.get_ER().size(); i++){
			reg_temp = mer.get_registro(i);
			if(reg_temp.getQj().equals(tag) ){
				reg_temp.setValorj(value);
				reg_temp.setQj("");
			}
			if(reg_temp.getQk().equals(tag) ){
				reg_temp.setValork(value);
				reg_temp.setQk("");
			}
		}
		for (int i=0; i<ler.get_ER().size(); i++){
			reg_temp = ler.get_registro(i);
			//System.out.println("[CDB] check tag Ler: " +tag + " "+ reg_temp.getQj() );
			if(reg_temp.getQj().equals(tag) ){
				reg_temp.setValorj(value);
				reg_temp.setQj("");
			}
		}
		for (int i=0; i<aer.get_ER().size(); i++){
			reg_temp = aer.get_registro(i);
			if(reg_temp.getQj().equals(tag) ){
				reg_temp.setValorj(value);
				reg_temp.setQj("");
			}
			if(reg_temp.getQk().equals(tag) ){
				reg_temp.setValork(value);
				reg_temp.setQk("");
			}
		}
		for (int i=0; i<ser.get_ER().size(); i++){
			reg_temp = ser.get_registro(i);
			if(reg_temp.getQj().equals(tag) ){
				reg_temp.setValorj(value);
				reg_temp.setQj("");
			}
		}
		// Los registros FP
		for (int i=0; i<regs.get_ER().size(); i++){
			reg_temp = regs.get_registro(i);
			//System.out.println("[CDB] registros " +reg_temp.getQj() + " " +  reg_temp.getValorj() );
			if(reg_temp.getQj().equals(tag) ){
				reg_temp.setValorj(value);
				reg_temp.setQj("");
			}
		}
		
	}
	

}
