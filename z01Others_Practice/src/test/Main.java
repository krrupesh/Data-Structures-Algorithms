package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

	private static String softwareName;

	private String serverName;
	private String softwareVersion;

	public Main(String serverName, String softwareVersion) {
		super();
		this.serverName = serverName;
		this.softwareVersion = softwareVersion;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	private static Map<String, ArrayList<Main>> mapSoft = new HashMap<String, ArrayList<Main>>();
	private static int totlaNoOfSoftwaresOutdated = 0;

	public static void main(String[] args) {

		/*
		 * reading from file and store's software name as key and list of objects containing servername and 
		 * version as value (object) in Map.
		 */
		try {
			Scanner scanner = new Scanner(new File("input.txt"));

			while (scanner.hasNext()) {
				String str = scanner.nextLine();
				String strArr[] = str.split(",");
				softwareName = strArr[2].trim();
				if (mapSoft.containsKey(softwareName)) {
					ArrayList<Main> al = mapSoft.get(softwareName);
					al.add(new Main(strArr[0].trim(), strArr[3].trim()));

					mapSoft.put(softwareName, al);
				} else {
					ArrayList<Main> al = new ArrayList<Main>();
					al.add(new Main(strArr[0].trim(), strArr[3].trim()));
					mapSoft.put(softwareName, al);
				}

			}
			
			scanner.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		getSoftwareCount(mapSoft);

		FileWriter fw;
		try {
			fw = new FileWriter("output.txt");
			fw.write(""+totlaNoOfSoftwaresOutdated);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * this method gets the arraylist of size > 2 and passes it for further processing
	 * 
	 * @param mapSoft
	 */
	private static void getSoftwareCount(Map<String, ArrayList<Main>> mapSoft) {
		for (Map.Entry<String, ArrayList<Main>> entry : mapSoft.entrySet()) {
			ArrayList<Main> al = entry.getValue();

			if (al.size() > 2) {
				getCount(al);
			}
		}
	}

	/**
	 * this method gets the arraylist find the max version then finds how 
	 * many versions are lower than this if its > 1 then at 2 or more tha 2 servers
	 * lower versions are running
	 * 
	 * @param al
	 */
	private static void getCount(ArrayList<Main> al) {
		String maxVersion = "";
		String maxVersionServer = "";

		for (Main main : al) {
			if (main.getSoftwareVersion().compareTo(maxVersion) > 0) {
				maxVersion = main.getSoftwareVersion();
				maxVersionServer = main.getServerName();
			}
		}

		int count = 0;
		for (Main main : al) {
			if (!main.getServerName().equals(maxVersionServer)) {
				if (main.getSoftwareVersion().compareTo(maxVersion) < 0) {
					count++;

					if (count > 1) {
						totlaNoOfSoftwaresOutdated++;
						break;
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return "serverName=" + serverName + ", softwareVersion="
				+ softwareVersion;
	}

}
