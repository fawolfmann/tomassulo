package tomasulo;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Registros_FP {
	private ArrayList<Registro> lista_registros;
	private int tamano_cola;
	
	//Solo utilizo los valor Qj y Valorj, el resto para esta estacion no se utilizan
	
	public Registros_FP(int tamano_cola){
		//mutex_multi = new Semaphore(1);
		lista_registros = new ArrayList<Registro>(tamano_cola);
		
		for(int i = 0 ; i<tamano_cola ; i++ ){
			Registro r = new Registro();
			r.setTag("R" + i);
			lista_registros.add(r);
		}
	}
	
	public void set_registroFP(int index , String tag, int value){
		Registro reg = lista_registros.get(index);
		reg.setQj(tag);
		reg.setValorj(value);
	}
	
	public Registro get_registro(int index ){
		return lista_registros.get(index);
	}
	
	public ArrayList<Registro> get_ER(){
		return lista_registros;
	}


}
