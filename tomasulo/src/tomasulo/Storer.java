package tomasulo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Storer  extends Operator{
	private RandomAccessFile file;
    private String fName = "memory.txt";
    private final int fileLength = 100;

	public Storer(int delay) {
		super(delay);
		try {
            file = new RandomAccessFile(fName, "rw");
        } catch (FileNotFoundException ex) {
        	System.out.println("error en file: " + ex);
        }
	}
	//valork destino
	//valorj es el valor a guardar
	public int operate(){
		int direction = 0;
		if(timeStamp+delayTime > System.currentTimeMillis()){
			setBusy(false);
			timeStamp = 0;
			return (int) setResult(valorj, valork);
		}
		else{
		return -1;
		}
	}

    public float setResult(int value , int direction) {
        float result = -1;
        //mutex.acquire();
        //result = storeBuffer[slot].value;
        try {
            file.seek(0);
        } catch (IOException ex) {
        	System.out.println("error seek file: " + ex);
        }
        for (int i = 0; i < direction && i < fileLength; i++) {
            try {
                file.readLine();
            } catch (IOException ex) {
            	System.out.println("error leyendo file: " + ex);
            }
        }
        try {
            String stValue = String.format("%03d", value);
            file.write(stValue.getBytes());
            result = 1;
        } catch (IOException ex) {
        	System.out.println("error escribiendo file: " + ex);
        }
        //storeBuffer[slot].remainingClock = -1;
        //mutex.release();
        return result;
    }

}