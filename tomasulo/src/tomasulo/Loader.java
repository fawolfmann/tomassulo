package tomasulo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.ParseException;

public class Loader extends Operator{
	private final String file = "memory.txt";
    private BufferedReader reader;
    private final int fileLength = 100;
    private File f;
    
	public Loader(int delay) {
		super(delay);
		f = new File(file);
		 try {
		            reader = new BufferedReader(new FileReader(f));
		            reader.mark(100);
		        } catch (IOException ex) {
		        	System.out.println("error en file: " + ex);
		        }
		 initializeFile();
		 
	}
	private void initializeFile() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(f);
        } catch (IOException ex) {
        	System.out.println("error en file: " + ex);
        }
        if (writer != null) {
            for (int i = 0; i < 100; i++) {
                writer.println(111);
            }
        }
        writer.close();
    }
	
	@Override
	public int operate(){
		//System.out.println("[Loader] busy " +this.timeStamp+" " + System.currentTimeMillis() +" "+ (long)(this.timeStamp+delayTime) );
		if(timeStamp+delayTime < System.currentTimeMillis()){
			//System.out.println("[Loader] completed " );
			setBusy(false);
			timeStamp = 0;
			return (int) getResult(valorj%fileLength);
		}
		else{
		return -1;
		}
	}
	
	public void values_in(int orig, String tag){
		//System.out.println("[Loader] values in " + orig );
		super.values_in(orig, tag);
	
	}
	
	public float getResult(int slot) {
        //mutex.acquire();
        float result = 0;
        String line = null;
        try {
            reader.reset();
        } catch (IOException ex) {
        	System.out.println("error en resetear file: " + ex);
        }
        for (int i = 0; i <= slot && i < fileLength; i++) {
            try {
                line = reader.readLine();
            } catch (IOException ex) {
            	System.out.println("error en leer file: " + ex);
            }
        }
        
        NumberFormat f = NumberFormat.getInstance();
        try {
            result = f.parse(line).floatValue();
        } catch (ParseException ex) {
        	System.out.println("error en parse: " + ex);
        }
        //loadBuffer[slot].remainingClock = -1;
        //mutex.release();
        return result;
    }

}
