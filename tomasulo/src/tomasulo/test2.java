/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomasulo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.junit.Test;

import dnl.utils.text.table.TextTable;

import static org.junit.Assert.*;


public class test2 {

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
	static final int clock = 50;
	
	
    public test2() {
    	//pasar las instrucciones
    	String[] stringCommand = { "MOV R1,#25", "MOV R2,#36","LDD R0,(R1)", "ADDD R4,R0,R2", "ADDD R6,R2,R4", "ADDD R0,R1,R4","MULD R1,R0,R4","STD (0),R4","STD (1),R6","STD (2),R0","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP", "END"};
    	mer = new Estacion_reserva(3 , "MUL");
		aer = new Estacion_reserva(3 , "ADD");
		ler = new Estacion_reserva(2 , "LDD");
		ser = new Estacion_reserva(2 , "STD");
		regFP = new Registros_FP(8);
		multi = new Multiplicador(clock*3);
		load = new Loader(clock*2);
		store = new Storer(clock*3);
		add = new Adder(clock*2);
		ib = new Instruccion_buffer(aer, mer , ler,regFP , ser, stringCommand);	
		cdb = new CDBus(aer, mer, ler, ser,regFP , multi , load, store , add);
		int i = 0;
		while(!stringCommand[i].equals("END") ){
			try {
				System.out.println("[Main] PC: " + i);
				Printer.creatDataOper(aer, "Adder");
				Printer.creatDataOper(mer , "Multiply");
				Printer.creatDataLD(ler , "Load");
				Printer.creatDataSTD(ser , "Store");
				Printer.creatDataReg(regFP);
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
//    @Test
//    public void testRegisters() {
//        assertEquals(172, regFP.get_registro(0).getValorj());
//        assertEquals(25284, regFP.get_registro(1).getValorj());
//        assertEquals(36, regFP.get_registro(2).getValorj());
//        assertEquals(-1, regFP.get_registro(3).getValorj());
//        assertEquals(147, regFP.get_registro(4).getValorj());
//        assertEquals(-1, regFP.get_registro(5).getValorj());
//        assertEquals(183, regFP.get_registro(6).getValorj());
//        assertEquals(-1, regFP.get_registro(7).getValorj());
//    }
    
    @Test
    public void testMemory() {
        RandomAccessFile file = null;
        String fName = "memory.txt";
        try {
            file = new RandomAccessFile(fName, "r");
        } catch (FileNotFoundException ex) {
            System.out.println("error en file: " + ex);
        }
        try {
            assertEquals(file.readLine(),"147");
            assertEquals(file.readLine(),"183");
            assertEquals(file.readLine(),"172");
        } catch (IOException ex) {
            fail();
        }
    }
}