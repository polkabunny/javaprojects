import java.io.*;
import java.util.*;

class BoundedBuffer {
	byte[] buffer;
	
	int nextIn = 0;
	int nextOut = 0;
	int size;
	int occupied;
	int ins;
	int outs;
	boolean dataAvailable = false;
	boolean roomAvailable = true;
	
	BoundedBuffer() {
		buffer = new byte[10];
		size  = buffer.length;
	}
	
	BoundedBuffer(int x) {
        if(x < 1) {
            System.out.println("Error: Size of buffer invalid.");
            System.exit(0);
        }
        size = x;
        buffer = new byte[x];
    }
	
	public synchronized void insertChunk(byte data) {
		while(!roomAvailable) {
			try {
				wait();
			}
			catch (InterruptedException e) {
				System.out.print("Herp derp");
			}
		}
		// Insert the element in the buffer.
		buffer[nextIn] = data;
		// Store time.
		occupied++;
		ins++;
		// Set data available to true
		dataAvailable = true;
		// Change the roomAvailable value if needed
		if(occupied == size) {
			roomAvailable = false;
		}
		notifyAll();
		nextIn = (nextIn + 1) % size;
	}
	
	public synchronized byte removeChunk() {
		while(!dataAvailable) {
			try {
				wait();
			}
			catch (InterruptedException e) {
				System.out.print("Herp derp");
			}
		}
		byte data = buffer[nextOut];
		occupied--;
		outs++;
		// Set room available to true
		roomAvailable = true;
		if(occupied == 0) {
			dataAvailable = false;
		}
		notifyAll();
		nextOut = (nextOut + 1)%size;
		// Returns the data item.
		return data;
    }
}
