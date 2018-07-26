package tomasulo;
import tomasulo.Registro;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Mult_ER {
	private int tamano_cola;
	private ArrayList<Registro> lista_multi;
	public Semaphore mutex_multi;
	public Registro registro_var;
	
	Mult_ER(int tamano_cola){
		Semaphore mutex_multi = new Semaphore(1);
		lista_multi = new ArrayList<Registro>(tamano_cola);
		
		for(int i = 0 ; i<tamano_cola ; i++ ){
			Registro r = new Registro();
			r.setTag("multi" + i);
			lista_multi.add(r);
		}
	}
	
//	determina si hay posicion en la ER para cargar una nueva operacion
//	@return posicion de la ER disponible
			
	public int is_space(){
		int index = -1;
		for(int i = 0 ; i<tamano_cola ; i++ ){
			if (!lista_multi.get(i).isBusy()){
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
		lista_multi.set(index, reg);
		registro = get_registro(index);		
		return registro.getTag();
	}
	
	public Registro get_registro(int index){		
		return lista_multi.get(index);
	}
	
	public ArrayList<Registro> get_ER(){
		return lista_multi;
	}
	
	public int operar(){
		int resultado = 0;
		
		
		
		return resultado;		
	}

}
