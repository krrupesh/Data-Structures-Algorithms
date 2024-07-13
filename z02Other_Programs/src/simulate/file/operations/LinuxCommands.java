package simulate.file.operations;

import java.io.File;

public class LinuxCommands {

	public static String absolutePath = "";
	
	/*
	 * check that folder or directory is available, if available then only go to it else show error
	 */
	public String cd(String command, String path){
		System.out.println("command "+command+" path "+path);

		if(path.equals("..")){
			String strArray[] = absolutePath.split("/");			
			int endLength = absolutePath.length() - strArray[strArray.length-1].length() -1;
			absolutePath = absolutePath.substring(0,endLength );
		}else{
			if(absolutePath.equals("/")){
				if(path.startsWith("/")){
					path = path.substring(1,path.length());
				}			
		    }
			absolutePath = absolutePath + path + "/";	
		
		System.out.println(absolutePath);
		
	    } 
		
		return absolutePath;
	}
	
	public void  ls(String command, String value){
		if(value == null){
			dispalyAllFolders();
		}
	}

	private void dispalyAllFolders() {

		String path = "";
		if(absolutePath.startsWith("/")){
			path = absolutePath.substring(0,1);
		}else{
			path = absolutePath;
		}
		File file = new File(path);

		String fileNames[] = file.list();
		
		System.out.println();
		for(int i=0; i<fileNames.length;i++){
			System.out.print(fileNames[i]+" ");
		}
	}
}
