package nz.co.dhaks.mqtt.qr;

 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExternalProcesses {

 
		
		private BufferedReader f;
		private InputStream stdIn;
		
		public static final String PROCESS_NAME="zbarcam.exe";
		public static final String PROCESS_ARGS_NODISPLAY="--nodisplay";
		public static final String PROCESS_ARGS_XML="--nodisplay";
		public ExternalProcesses() {
			super();
			 
		 
		//	String[] commands = { Constants.PROCESS_NAME};
			String[] commands = { PROCESS_NAME,PROCESS_ARGS_NODISPLAY,PROCESS_ARGS_XML};
			Runtime rt = Runtime.getRuntime();
			Process proc;
			try {
				proc = rt.exec(commands);
				stdIn  = proc.getInputStream();
			} catch (IOException e) {
			 
				e.printStackTrace();
			}

			
			     InputStreamReader isr = new InputStreamReader( this.stdIn);
	     this.f = new BufferedReader( isr);
	 
	 }
		
		public InputStream getStdIn() {
			return stdIn;
		}

		public BufferedReader getBufferedReader() {
			return f;
		}
	}