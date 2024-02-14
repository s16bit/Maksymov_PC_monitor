
public class UpdateThread implements Runnable {
	
	private Thread manipulationDataThread;
	
	private volatile boolean running;
	private volatile boolean suspended;
	
	private int delay = 500;
	
	@Override
	public void run() {
		running = true;
		
		//long beforeTime, timeDifference, sleepTime;
		
		//beforeTime = System.nanoTime();
		
		while(running && !( manipulationDataThread.isInterrupted()) ) {
			
			try {
			VirtualRS232.sendTo();
			} catch (jssc.SerialPortException e) {
				VirtualRS232.error = true;
				stop();
			}
			
			try {
				Thread.yield();
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
			
			while(suspended) {
				synchronized(manipulationDataThread) {
					try {
						Thread.yield();
						manipulationDataThread.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
			}
			
		}
	}
	
	public synchronized void start() {
		if(manipulationDataThread == null || !(running) ) {
			manipulationDataThread = new Thread(this, "RS232 Comunication thread");
			manipulationDataThread.start();
		}
	}
	
	public synchronized void stop() {
		running = false;
		manipulationDataThread.interrupt();
		//manipulationDataThread = null;
	}
	
	public synchronized void pause() {
		suspended = !(suspended);
		synchronized(manipulationDataThread) {
			if(suspended == false) manipulationDataThread.notify();
		}
	
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public byte getCurrentCPUload() {
		return VirtualRS232.cpu_load;
	}

}