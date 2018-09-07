package tomasulo;

import org.apache.commons.lang.StringUtils;

public class Printer {
	public Printer(){
		
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
