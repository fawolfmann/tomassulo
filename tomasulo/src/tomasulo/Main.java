package tomasulo;

public class Main {
	Instruccion_buffer ib;
	Mult_ER mer;
	Registros_FP regFP;
	CDBus cdb;

	public static void main(String[] args) {
		Mult_ER mer = new Mult_ER(5);
		Registros_FP regFP = new Registros_FP(5);
		Multiplicador multi = new Multiplicador(5000);
		Instruccion_buffer ib = new Instruccion_buffer(mer ,regFP);	
		CDBus cdb = new CDBus(mer,regFP , multi);
		
		cdb.run();
		ib.run();
	}
}

