package tomasulo;
import tomasulo.Registro;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Estacion_reserva {
	private int tamano_cola;
	private ArrayList<Registro> lista;
	public Semaphore mutex;
	public Registro registro_var;
	
	Estacion_reserva(int tamano_cola, String tag){
		mutex = new Semaphore(1);
		lista = new ArrayList<Registro>(tamano_cola);
		this.tamano_cola = tamano_cola;
		for(int i = 0 ; i<tamano_cola ; i++ ){
			Registro r = new Registro();
			r.setTag(tag + i);
			lista.add(r);
		}
	}
	
//	determina si hay posicion en la ER para cargar una nueva operacion
//	@return posicion de la ER disponible
			
	public int is_space(){
		//System.out.println("[er] is_space ");
		int index = -1;
		for(int i = 0 ; i<tamano_cola ; i++ ){
			//System.out.println("[er] is_space busy" + !lista.get(i).isBusy());
			if (!lista.get(i).isBusy()){
				index = i;
				break;
			}
		}		
		return index;
	}
//	Carga el registro en la lista de registros
//	@param index posicion donde almacenar el registro
//	@param reg el registro a carger
//	@return devuelve el tag del registro almacenado
			
	public String set_registro(int index, Registro reg){
		Registro registro;
		lista.set(index, reg);
		registro = get_registro(index);		
		return registro.getTag();
	}
	
	public Registro get_registro(int index){		
		return lista.get(index);
	}
	
	public ArrayList<Registro> get_ER(){
		return lista;
	}
}
