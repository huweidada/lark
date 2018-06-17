package lark.tools;

import java.io.IOException;
import java.util.Scanner;

public class Hardware {
	//从网上找的一段代码，没有经过认真研究和测试
	public static String getCpuNumber(){  
        Process process;
		try {
			process = Runtime.getRuntime().exec(new String[] { "wmic", "cpu", "get", "ProcessorId" });
		} catch (IOException e) {
			return "";
		}
        try {
			process.getOutputStream().close();
		} catch (IOException e) {
			return "";
		}  
        Scanner sc = new Scanner(process.getInputStream());  
        sc.next();  
        String cpuNumber = sc.next();  
        sc.close();
        return cpuNumber;
	 }
}
