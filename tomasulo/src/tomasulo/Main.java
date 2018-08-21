package tomasulo;

import dnl.utils.text.table.TextTable;
import org.apache.commons.lang.StringUtils;

public class Main {
	static Instruccion_buffer ib;
	static Estacion_reserva mer;
	static Estacion_reserva aer;
	static Estacion_reserva ler;
	static Estacion_reserva ser;
	static Registros_FP regFP;
	static CDBus cdb;
	static Multiplicador multi ;
	static Loader load;
	static Storer store;
	static Adder add;
	TextTable tt;
	//static int clock = 2000;
	static final int clock = 2000;
	public static String[] stringCommand = { "MOV R1,#10", "STD (4),R1","MOV R2,#12","MULD R3,R1,R2", "LDD R0,(R3)", "STD (2),R3","ADDD R4,R0,R1", "ADDD R5,R2,R0","MULD R6,R0,R5","MULD R0,R1,R0","MULD R7,R4,R5","MULD R5,R0,R2","MULD R1,R2,R4","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP", "END"};


	public static void main(String[] args) {
		mer = new Estacion_reserva(3 , "MUL");
		aer = new Estacion_reserva(3 , "ADD");
		ler = new Estacion_reserva(2 , "LDD");
		ser = new Estacion_reserva(2 , "STD");
		regFP = new Registros_FP(8);
		multi = new Multiplicador(clock*3);
		load = new Loader(clock*2);
		store = new Storer(clock*4);
		add = new Adder(clock*2);
		ib = new Instruccion_buffer(aer, mer , ler,regFP , ser, stringCommand);	
		cdb = new CDBus(aer, mer, ler, ser,regFP , multi , load, store , add);
		//System.out.println("[Main]  Creo todo");
		int i = 0;
		while(!stringCommand[i].equals("END") ){
			try {
				System.out.println("[Main] PC: " + i);
				creatDataOper(aer, "Adder");
				creatDataOper(mer , "Multiply");
				creatDataLD(ler , "Load");
				creatDataSTD(ser , "Store");
				creatDataReg(regFP);
				Thread.sleep(10);
				cdb.run();
				Thread.sleep(clock);
				ib.run();
				i++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
	
	public static void creatDataOper(Estacion_reserva er, String operacion){
		 Object[][] data = new Object[3][7];
		 String[] columnNames = {"Tag","OP","Qj","Valorj","Qk","Valork","Qk"};
			
		 for(int i=0; i< er.get_ER().size() ; i++){
			 data[i][0] = er.get_registro(i).getTag();
			 data[i][1] = er.get_registro(i).getOp();
			 data[i][2] = er.get_registro(i).getQj();
			 data[i][3] = er.get_registro(i).getValorj();
			 data[i][4] = er.get_registro(i).getQk();
			 data[i][5] = er.get_registro(i).getValork();
			 data[i][6] = er.get_registro(i).isBusy();
		 }
		 
		 printTable(operacion ,columnNames,data  );	
	}
	
	public static void creatDataLD(Estacion_reserva er, String operacion){
		 Object[][] data = new Object[2][4];
		 String[] columnNames = {"Tag","Qj","Valorj","Busy"};
			
		 for(int i=0; i< er.get_ER().size() ; i++){
			 data[i][0] = er.get_registro(i).getTag();
			 data[i][1] = er.get_registro(i).getQj();
			 data[i][2] = er.get_registro(i).getValorj();
			 data[i][3] = er.get_registro(i).isBusy();
		 }
		 
		 printTable(operacion ,columnNames,data  );	
	}
	
	public static void creatDataSTD(Estacion_reserva er, String operacion){
		 Object[][] data = new Object[2][5];
		 String[] columnNames = {"Tag","Qj","Valorj", "Direction","Busy"};
			
		 for(int i=0; i< er.get_ER().size() ; i++){
			 data[i][0] = er.get_registro(i).getTag();
			 data[i][1] = er.get_registro(i).getQj();
			 data[i][2] = er.get_registro(i).getValorj();
			 data[i][3] = er.get_registro(i).getValork();
			 data[i][4] = er.get_registro(i).isBusy();
		 }
		 
		 printTable(operacion ,columnNames,data  );	
	}
	
	public static void creatDataReg(Registros_FP er){
		 Object[][] data = new Object[8][3];
		 String[] columnNames = {"Tag","Qj","Valorj"};
			
		 for(int i=0; i< er.get_ER().size() ; i++){
			 data[i][0] = er.get_registro(i).getTag();
			 data[i][1] = er.get_registro(i).getQj();
			 data[i][2] = er.get_registro(i).getValorj();
		 }		 
		 printTable("Registros" ,columnNames,data  );	
	}
	
	
/*	@name el nombre de al table
	@columnNames array de nombre de columnas
	@data matriz de String
*/	
	public static void printTable(String name, String[] columnNames, Object[][] data) {
        /*
        tt = new TextTable(columnNames, data);
        tt.setAddRowNumbering(true);
        tt.printTable();
         */
        String head = "   |";
        String headDash = "   ";
        String bodyDash = "   ";
        for (int i = 0; i < columnNames.length; i++) {
            head += String.format("%10s|", StringUtils.center(columnNames[i], 10));

        }
        for (int i = 0; i < head.length() - 3; i++) {
            headDash += "=";
            bodyDash += "-";
        }
        System.out.println(StringUtils.center(name, head.length()));
        System.out.println(headDash);
        System.out.println(head);
        System.out.println(headDash);

        for (int i = 0; i < data.length; i++) {
            System.out.printf(i + ". |");
            for (int j = 0; j < data[0].length; j++) {
                System.out.printf("%10s|", StringUtils.center(data[i][j].toString(), 10));
            }
            System.out.println();
            System.out.println(bodyDash);
        }
        System.out.println();
    }
}

