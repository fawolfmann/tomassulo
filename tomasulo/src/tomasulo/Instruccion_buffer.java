package tomasulo;

import java.util.ArrayList;

import javafx.util.Pair;
import tomasulo.Registro;
import tomasulo.Registros_FP;


public class Instruccion_buffer implements Runnable  {
	private Estacion_reserva mer;
	private Estacion_reserva aer;
	private Estacion_reserva ler;
	private Registros_FP regs;
	private Registro register_ER;
	//private Registro register_regFP;
	private int plus;
	private ArrayList<Intruction> instructions;
	private int todispatch;
	private Estacion_reserva ser;
	private String[] stringCommand;
	
	public Instruccion_buffer(Estacion_reserva aer_in,Estacion_reserva mer_in ,Estacion_reserva ler_in ,Registros_FP regs_in, Estacion_reserva ser, String[] stringCommand  ){
		this.regs = regs_in;
		this.mer = mer_in;
		this.aer = aer_in;
		this.ler = ler_in;
		this.ser = ser;
		register_ER = new Registro();
		//register_regFP = new Registro();
		instructions = new ArrayList<Intruction>();
		plus = 0;
		this.stringCommand = stringCommand;
		instruction_list();
	} 
	
	public void run(){
		System.out.println("[Instrction buffer] Comando " +get_instruction(plus) +" " +plus);
		intruccion_parser(get_instruction(plus));
	}
	
	public void instruction_list(){
		Intruction ins;
		for(int i =0 ; i < stringCommand.length ; i++){
			ins = new Intruction(stringCommand[i]);
			//System.out.println("[Instrction buffer] new command  " +ins.getIntruccion()+" " + i);
			instructions.add(ins);
		}
	}
	
	public String get_instruction(int position){
		for(int i = 0; i < instructions.size() ; i++ ){
			//System.out.println("[Instrction buffer] position " + position );
			if(!instructions.get(i).isDispatch()){
				//System.out.println("[Instrction buffer] hay mas instrucciones");
				if (position <= 0 ){
					//System.out.println("[Instrction buffer] to dispatch " + i );
					todispatch = i;
					return instructions.get(i).getIntruccion() ;
				}
				else{
					position--;
				}
			}
//			else{
//				// no hay mas instrucciones
//				System.out.println("[Instrction buffer] instrucciones tomado");
//			}
		}
		return null;
	}

	private void Dispatched() {
		instructions.get(todispatch).setDispatch(true);
	}

	// toma instrucciones y las parsea para cada tipo de ER
	public void intruccion_parser(String instruccion){
		int index;
		String op ;
		String[] registrosString;
		String[] parser_values = null;
		int[] registrosInt;
		int regist;
		int inmediate;
		int regist_source;
		//int op = Integer.parseInt(instruccion.substring(0,6),2);
		//System.out.println("[Instrction buffer] instruccion " +instruccion);

		if(instruccion.contains(" ")){
			parser_values = instruccion.split(" ");		
			op = parser_values[0];
		}else{
			op = instruccion;
		}
		//System.out.println("[Instrction buffer] Op code " +op);

		switch(op){
			case "ADDD":
				index = check_space(aer);
				if(index > -1){
					Dispatched();
					registrosString = parser_values[1].split(",");
					registrosInt = RegisterToint(registrosString);
					/*for (int i = 0 ; i< 3 ; i++){
						//System.out.println("[Instrction buffer]numero " +registrosInt[i]);
					}*/
					charge_registers(registrosInt , "ADD", aer , index);
				}
				else{
					run();
				}
				break;

			case "MULD":
				index = check_space(mer);
				if(index > -1){
					Dispatched();
					registrosString = parser_values[1].split(",");
					registrosInt = RegisterToint(registrosString);
					charge_registers(registrosInt , "MUL", mer , index);
				}
				else{
					run();
				}
				break;
			case "LDD":
				index = check_space(ler);
				if(index > -1){
					Dispatched();
					//System.out.println("[Instrction buffer] Entre LDD");
					registrosString = parser_values[1].split(",");
					regist = Integer.parseInt(registrosString[0].replace("R", ""));
					regist_source = Integer.parseInt(registrosString[1].replaceAll("\\D+",""));
					//System.out.println("[Instrction buffer] LDD registro: "+ regist + " origen " +regist_source);
					load_direct(regist , regist_source , index);
				}
				else{
					run();
				}
				break;
				
			case "MOV":
				index = check_space(ler);
				if(index > -1){
					Dispatched();
					//System.out.println("[Instrction buffer] Entre LDI");
					registrosString = parser_values[1].split(",");
					regist = Integer.parseInt(registrosString[0].replace("R",""));
					inmediate = Integer.parseInt(registrosString[1].replace("#", ""));
					//System.out.println("[Instrction buffer] LDI registro: "+ regist + " valor " +inmediate);
					load_inmediate(regist, inmediate);
				}
				else{
					run();
				}
				break;
				
			case "STD":
				index = check_space(ser);
				if(index > -1){
					Dispatched();
					//System.out.println("[Instrction buffer] Entre LDI");
					registrosString = parser_values[1].split(",");
					regist = Integer.parseInt(registrosString[1].replace("R",""));
					inmediate = Integer.parseInt(registrosString[0].replaceAll("\\D+", ""));
					//regist = el valor a guardar o el valor del registro a guardar
					//inmediate = la direccion donde guardar
					store_direct( regist,inmediate , index);
				}
				else{
					run();
				}
				break;
			
			case "NOP":
				//System.out.println("[Instrction buffer] NOP");
				Dispatched();
				try {
					Thread.sleep(test1.clock);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}					
				break;
				
			default:
				System.out.println("[Instrction buffer] instruccion no reconocida");
				break;
		}
	}
	private void store_direct(int regist_source, int inmediate , int index) {
		Pair<String, Integer> parj;
		parj = check_register(regist_source);
		ser.get_registro(index).setBusy(true);
		ser.get_registro(index).setQj((String) parj.getKey());
		ser.get_registro(index).setValorj((int) parj.getValue());
		
		ser.get_registro(index).setValork(inmediate);
		//System.out.println("[Instrction buffer] STD Cargo " + (int) parj.getValue() );

	}

	private int check_space(Estacion_reserva er){
		int index = er.is_space();
		if(index > -1){
			plus = 0 ;
		}else{
			plus++;
		}
		return index;
	}
	
	private void load_direct(int regist, int regist_source , int index) {
		Pair<String, Integer> parj;
		String tag_reg;
		
		parj = check_register(regist_source);
		ler.get_registro(index).setBusy(true);
		ler.get_registro(index).setQj((String) parj.getKey());
		ler.get_registro(index).setValorj((int) parj.getValue());
		tag_reg = ("LDD" + index);
		
		regs.set_registroFP(regist, tag_reg , -1);		
		System.out.println("[Instrction buffer] LDD Cargo " + (int) parj.getValue() );
	}

//	Cargo en el registro el valor dado, transaccion atomica no tiene demora.
//	@param regist el registro destino
//	@param inmediate el valor a cargar
	private void load_inmediate(int regist, int inmediate) {
		regs.set_registroFP(regist, "", inmediate);
		//System.out.println("[Instrction buffer] LDI Cargo "+ regs.get_registro(regist).getValorj() );
	}

	private int[] RegisterToint(String[] registrosString) {
		int[] registrosInt = new int[3];
		
		for(int i = 0; i<3 ; i++){
			registrosInt[i] = Integer.parseInt(registrosString[i].replace("R", ""));
		}		
		return registrosInt;
	}

	public void charge_registers(int[] registrosInt, String operation, Estacion_reserva er, int index){
		Pair<String, Integer> parj;
		Pair<String, Integer> park;
		//register_ER = null;
		//System.out.println("[Instrction buffer] cargando");					
		try {
			er.mutex.acquire();
			//System.out.println("[Instrction buffer] mutex");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		//System.out.println("[Instrction buffer]  En la posicion" +index +"de la ER multiplicador");
		//Determinar si los rs y rt tienen el valor disponible o un tag.
		
		//System.out.println("[Instrction buffer] Register a cargar " +register_ER.getQj() + register_ER.getQk());
		//Cargo el registro en la posicion determinada anteriormente.
		//register_ER.setTag( operation+ index);
		//String tag_reg = er.set_registro(index, register_ER);
		
		//System.out.println("[Instrction buffer] release" );

		//tomo de los registros FP el valor o el tag que me indica el valor
		parj = check_register(registrosInt[1]);
		park = check_register(registrosInt[2]);
		er.get_registro(index).setBusy(true);
		er.get_registro(index).setOp(operation);
		er.get_registro(index).setQj((String) parj.getKey());
		er.get_registro(index).setValorj((int) parj.getValue());
		er.get_registro(index).setQk((String) park.getKey());
		er.get_registro(index).setValork((int) park.getValue());
		er.mutex.release();
		//En Registros_FP le pongo el tag donde va a estar ese valor en la posicion rd.
		regs.set_registroFP(registrosInt[0], operation+ index , -1);
		//System.out.println("[Instrction buffer] Register a cargar " + regs.get_registro(registrosInt[0]).getQj());

	}
	
	private Pair<String, Integer> check_register(int regis_number ){
		//System.out.println("[Instrction buffer] position_1 " + regis_number);
		Pair<String, Integer> par;
		if(regs.get_registro(regis_number).getValorj() != -1){
			//entonces el valor esta en el registro
			par = new Pair<String, Integer>("" , regs.get_registro(regis_number).getValorj());
		}else{
			//entonces el valor no esta y tienen un tag
			par = new Pair<String, Integer>( regs.get_registro(regis_number).getQj() , -1);
		} 
		return par;
	}
	
	//Devuelve un array de enteros de las posiciones de registros que necesita
	//@param "R1,R2,R3"
	//@return [1,2,3]
	public int[] parse_register(String registros){
		String[] register_divide = registros.split(",");
		int[] registers_int ;
		registers_int = new int[3];
		
		for(int i = 0; i < 3 ; i++){
			if (!register_divide[i].isEmpty()){
				registers_int[i] = Integer.parseInt(register_divide[i].replace("R",""));
			}
		}
		return registers_int;		
	}
	//Parsea los registros y devuelve un valor o el registro a utilizar.
	//@param "R1,R2,R3"
	//@return [1,2,3]
	public int[] parse_register_inmediat(String registros){
		String[] register_divide = registros.split(",");
		int[] registers_int ;
		registers_int = new int[3];
		
		for(int i = 0; i < 3 ; i++){
			if (!register_divide[i].isEmpty()){
				
				registers_int[i] = Integer.parseInt(register_divide[i].replace("R",""));
			}
		}
		return registers_int;		
	}
}
