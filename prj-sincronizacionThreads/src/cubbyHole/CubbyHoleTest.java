package cubbyHole;

import java.io.*;

class CubbyHole {
    private int contents;         // this is the condition variable.
    private boolean available = false;

    public synchronized int get() {
        while (available == false) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        available = false;
        notifyAll();
        return contents;
    }

public synchronized void put(int value) {
        while (available == true) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        contents = value;
        available = true;
        notifyAll();
    }
}

class Producer extends Thread {
    private CubbyHole cubbyhole;
    private int number;

    public Producer(CubbyHole c, int number) {
        cubbyhole = c;
        this.number = number;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            cubbyhole.put(i);
            System.out.println("Producer #" + this.number
                                       + " put: " + i);
            try {
                sleep((int)(Math.random() * 100));
            } catch (InterruptedException e) {
            }
        }
    }
}


class Consumer extends Thread {
    private CubbyHole cubbyhole;
    private int number;

    public Consumer(CubbyHole c, int number) {
        cubbyhole = c;
        this.number = number;
    }

    public void run() {
        int value = 0;
        for (int i = 0; i < 10; i++) {
            value = cubbyhole.get();
            System.out.println("Consumer #" + this.number 
                                       + " got: " + value);
        }
    }
}



public class CubbyHoleTest{

      public static void main(String[] args) throws IOException{
	  
	  int numProducersConsumers = 1;
	  if (args.length > 0) numProducersConsumers = Integer.valueOf(args[0]).intValue();
                            
        CubbyHole cubbyhole = new CubbyHole();
		
		Producer[] p = new Producer[numProducersConsumers];
		Consumer[] c = new Consumer[numProducersConsumers];
        
		for(int j=0; j < numProducersConsumers; j++)
		 {
		   p[j] = new Producer(cubbyhole, j);
		   c[j] = new Consumer(cubbyhole, j);
		 }
        for(int j=0; j < numProducersConsumers; j++)
		 {
		   p[j].start();
		 }
        for(int j=0; j < numProducersConsumers; j++)
		 {
           c[j].start();
         }

}
}
