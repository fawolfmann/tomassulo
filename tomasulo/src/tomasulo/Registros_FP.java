package tomasulo;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Registros_FP {
	private ArrayList<Registro> lista_registros;
	private int tamano_cola;
	
	//Solo utilizo los valor Qj y Valorj, el resto para esta estacion no se utilizan
	
	public Registros_FP(int tamano_cola){
		Semaphore mutex_multi = new Semaphore(1);
		lista_registros = new ArrayList<Registro>(tamano_cola);
	}
	
	public void set_registroFP(int index , Registro reg){
		lista_registros.set(index, reg);
	}
	
	public Registro get_registro(int index ){
		return lista_registros.get(index);
	}
	
	public ArrayList<Registro> get_ER(){
		return lista_registros;
	}


}
