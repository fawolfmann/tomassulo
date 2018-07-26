package tomasulo;

import tomasulo.Registro;
import tomasulo.Registros_FP;


public class Instruccion_buffer implements Runnable  {
	private Mult_ER mer;
	private Registros_FP regs;
	private Registro register_ER;
	private Registro register_regFP;
	private Registro registerj;
	private Registro registerk;
	 
	
	public Instruccion_buffer(Mult_ER mer_in ,Registros_FP regs_in ){
		this.regs = regs_in;
		this.mer = mer_in;
		//Registro register_ER = new Registro();
		//Registro register_regFP = new Registro();
	} 
	
	public void run(){
		// toma instrucciones de un txt
		
		intruccion_parser("100000");
	}
	// toma instrucciones y las parsea para cada tipo de ER
	public void intruccion_parser(String instruccion){
		int op = Integer.parseInt(instruccion.substring(0,5));
		if(op == 100000){
			//Intruccion tipo R 'Ponele'
			//obtenemos rs, rt y rd
			int rs = Integer.parseInt(instruccion.substring(6,10));
			int rt = Integer.parseInt(instruccion.substring(11,15));
			int rd = Integer.parseInt(instruccion.substring(16,20));
			
			if(rs > regs.get_ER().size() && rt > regs.get_ER().size() && rd > regs.get_ER().size() ){
				// el registro seleccionado esta incorrecto, es mayor que la cantidad de registros disponibles
				
				return;
			}
			
			//suponemos que es multiplicacion
			register_ER.setOp(op);
			register_ER.setBusy(true);
			
			
			try {
				mer.mutex_multi.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// determino si hay lugar en la ER 
			int index = mer.is_space();
			if( index  != -1  ){
				//Determinar si los rs y rt tienen el valor disponible o un tag.
				registerj = regs.get_registro(rs);
				if(registerj.getValorj() != -1){
					//entonces el valor esta en el registro
					register_ER.setValorj(registerj.getValorj());
					register_ER.setQj(null);
					
				}else{
					//entonces el valor no esta y tienen un tag
					register_ER.setValorj(-1);
					register_ER.setQj(registerj.getQj());
				}
				registerk = regs.get_registro(rt);
				if(registerk.getValorj() != -1){
					//entonces el valor esta en el registro
					register_ER.setValork(registerk.getValorj());
					register_ER.setQk(null);
					
				}else{
					//entonces el valor no esta y tienen un tag
					register_ER.setValork(-1);
					register_ER.setQk(registerk.getQj());
				}
				//Cargo el registro en la posicion determinada anteriormente.
				String tag_reg = mer.set_registro(index, register_ER);
				mer.mutex_multi.release();
				
				//En Registros_FP le pongo el tag donde va a estar ese valor en la posicion rd.
				register_regFP.setQj(tag_reg);
				register_regFP.setValorj(-1);
				regs.set_registroFP(rd, register_regFP);
			}
			else{
				// no tengo mas espacio en esa estacion.
			}
		}
		else if(op == 100001){
			///insruccion tipo J
			//obtenemos rs, rt y rd
		}
		else{
			//Bad instruction
		}
			
	}
}
