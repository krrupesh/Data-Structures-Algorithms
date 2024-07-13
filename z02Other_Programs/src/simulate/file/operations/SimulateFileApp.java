package simulate.file.operations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimulateFileApp {
	
	static LinuxCommands linuxCommands = new LinuxCommands();

	public static void main(String[] args) throws IOException {
		BufferedReader br = null;		
		
		while(true){
			br = new BufferedReader(new InputStreamReader(System.in));
			String value = br.readLine();
			manageCommands(value);
		}		
	}
	
	public static void manageCommands(String value){
		
		if(value.startsWith("cd")){
			String str[] = value.split(" ");
			if(str.length > 1){				
				linuxCommands.cd("cd", str[1]);
			}
			
		}else if(value.startsWith("ls")){
			String str[] = value.split(" ");
			if(str.length > 1){				
				linuxCommands.ls("ls", str[1]);
			}else{
				linuxCommands.ls("ls", null);
			}
		}
	}
}
