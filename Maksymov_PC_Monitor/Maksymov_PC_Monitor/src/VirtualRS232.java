
import java.util.List;

import jssc.SerialPort;
import jssc.SerialPortException;

//import com.sun.jna.*;




import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.*;
import com.profesorfalken.jsensors.model.sensors.*;


public class VirtualRS232 {
	
public static String nCOM = "COM9";
	
public static int  baudRate = 9600;

public static byte cpu_load = 0;

public static boolean error = false;
    
  static void test() {
        Components components = JSensors.get.components();

        List<Cpu> cpus = components.cpus;
        if (cpus != null) {
            for (final Cpu cpu : cpus) {
                System.out.println("Found CPU component: " + cpu.name);
                if (cpu.sensors != null) {
                  System.out.println("Sensors: ");
      
                  //Print temperatures
                  List<Temperature> temps = cpu.sensors.temperatures;
                  for (final Temperature temp : temps) {
                      System.out.println(temp.name + ": " + temp.value + " C");
                  }
                  
                  List<Load> loads = cpu.sensors.loads;
                  for (final Load load : loads) {
                      System.out.println(load.name + ": " + load.value + " %");
                  }
      
                  //Print fan speed
                  List<Fan> fans = cpu.sensors.fans;
                  for (final Fan fan : fans) {
                      System.out.println(fan.name + ": " + fan.value + " RPM");
                  }
                }
            }
        }
    }
	
	
	public static void sendTo() throws SerialPortException{
		error = false;
		test();
		SerialPort serialPort = new SerialPort(nCOM);
		//try {
			serialPort.openPort();
			serialPort.setParams(baudRate, 8, 1, SerialPort.PARITY_NONE, false, false);
		//} catch (SerialPortException e1) {
			
			//e1.printStackTrace();
		//}
		while(true) {
		Components components = JSensors.get.components();
		
		List<Cpu> cpus = components.cpus;
        if (cpus != null) {
            for (final Cpu cpu : cpus) {
            	
                if (cpu.sensors != null) {
                	
                  List<Load> loads = cpu.sensors.loads;
                	  
	                  System.out.println("Cpu load: " + loads.get(2).value.byteValue());
	                  //try {
	                	  String cp = "" + loads.get(2).value.byteValue();
	                	  
	                	  cpu_load = loads.get(2).value.byteValue();
	                	  
	                	  System.out.println(cp);
						serialPort.writeBytes(cp.getBytes());
					//} catch (SerialPortException e1) {
						
						//e1.printStackTrace();
					//}
	                  /*
	                  try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
					*/
					
                }
            }
        }

		}
	
	}

}