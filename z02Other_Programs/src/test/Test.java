package test;

import java.io.File;

public class Test {


	public static void main(String[] args) {
		
	/*	File[] paths;
		FileSystemView fsv = FileSystemView.getFileSystemView();

		// returns pathnames for files and directory
		paths = File.listRoots();

		// for each pathname in pathname array
		for(File path:paths)
		{
		    // prints file and directory paths
		    System.out.println("Drive Name: "+path);
		    System.out.println("Description: "+fsv.getSystemTypeDescription(path));
		}*/
		
		/*
		File[] roots = File.listRoots();
		for(int i=0;i<roots.length;i++)
		    if(roots[i].getFreeSpace() != 0){
		    	System.out.println(roots[i]);
		    }*/
		
		
		String path = "D:/Back_ups";
		String strArray[] = path.split("/");
		
		File file = new File(path);
		String fileNames[] = file.list();
		
		for(int i=0; i<fileNames.length;i++){
			System.out.println(fileNames[i]+" ");
		}
	}

}
