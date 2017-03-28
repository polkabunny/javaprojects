// Tríona Barrow - 11319851
// Freddie Hayden - 12330726
// Edward Moriarty - 12405042

import java.util.*;
import java.io.*;
import java.net.*;

// A BoundedBuffer is of fixed size and allows the insertion and removal of
// Strings as necessary. Obvious constraints are included: a full buffer cannot
// take in more Strings, and an empty buffer cannot have any Strings removed
// from it.

class BoundedBuffer {
	// Hold the data passed through by the Producer.
	private String [] buffer;
	
	// Integer variables to hold the indexes for references to specific 
	// data within the buffer - for both insertion and removal, as well as
	// keeping track of the size, number of variables removed and added, as
	// well as how much is currently occupied.
	private int nextIn = 0;
	private int nextOut = 0;
	private int size;
	private int occupied;
	private int ins;
	private int outs;
	// Boolean variables to reference to see the current condition of the
	// buffer. dataAvailable is false by default as there is no data
	// available for removal at the start, while roomAvailable is true for
	// the same reason.
	private boolean dataAvailable = false;
	private boolean roomAvailable = true;
	
	// No-args constructer - sets up the BoundedBuffer to be a String array
	// of length 10 and sets variable to that length.
	public BoundedBuffer() {
		buffer = new String[10];
		size = buffer.length;
	}
	
	// All-args constructor - allows you to set up a BoundedBuffer of x
	// length. Exits the program if you enter one less than 1 (ie 0)
	public BoundedBuffer(int x) {
		if(x < 1) {
		    System.out.println("Buffer too small!");
		    System.exit(0);
		}
		size = x;
		buffer = new String[x];
	}
	
	// This method adds the String passed as a parameter to the buffer, 
	// increments occupied and ins, and modifies the roomAvailable value if 
	// required.
	public synchronized void insertLine(String data) {
		while(!roomAvailable) {
			try {
				wait();
			}
			catch (InterruptedException e) {
				System.out.print("Error caused by interrupt.");
			}
		}
		buffer[nextIn] = data;
		occupied++;
		ins++;
		dataAvailable = true;

		if(occupied == size) {
			roomAvailable = false;
		}
		notifyAll();
		nextIn = (nextIn + 1) % size;
	}
	
	// This method returns the data from the buffer at nextOut, decrements 
	// occupied and increments outs, and modifies the dataAvailable value if 
	// needed.
	public synchronized String removeLine() {
		while(!dataAvailable) {
			try {
				wait();
			}
			catch (InterruptedException e) {
				System.out.print("Herp derp");
			}
		}
		String data = buffer[nextOut];
		occupied--;
		outs++;
		roomAvailable = true;
		if(occupied == 0) {
			dataAvailable = false;
		}
		notifyAll();
		nextOut = (nextOut + 1)%size;
		return data;
    }
}

// Producer class works with the BoundedBuffer class and uses the socket to find
// the port required to interact with the server as passed through.
class Producer extends Thread {
	// BoundedBuffer to hold user input
	private BoundedBuffer buffer;
	// Variables used to hold variables, such as username, message, the
	// consumer, the socket used, etc.
	private String message = null;
	private Consumer consumer;
	private Scanner socketIn = null;
	private PrintWriter socketOut = null;
	private Socket socket = null;
	private String username;
	private int count = 0;
	
	// No-args constructor
	public Producer() {
	}
	
	// All-args constructor
	public Producer(BoundedBuffer buffer, Consumer consumer, Socket socket)
	{
		this.buffer = buffer;
		this.consumer = consumer;
		this.socket = socket;
	}
	
	// Method to run, allows you to get input for as long as there is input
	// from the Scanner variable - socketIn. It also allows you to get the
	// username and adds the username along with the message to the
	// BoundedBuffer.
	public void run() {
		try {
			socketIn = new Scanner(new InputStreamReader
				(socket.getInputStream()));
			socketOut = new PrintWriter(socket.getOutputStream(),
				true);
			if(count == 0) {
				username = socketIn.nextLine();
				addToConsumer();
				count = 1;
			}

			while(socketIn.hasNextLine()) {
				buffer.insertLine(username + " says: " +
					socketIn.nextLine());
			}
			removeFromConsumer();
			socket.close();
		}
		catch(IOException i) {
			i.printStackTrace();
		}
	}
	
	// Add another producer to the ArrayList held in the Consumer class if
	// another joins.
	public void addToConsumer() {
		consumer.add(this);
	}
	
	// Remove the producer from the ArrayList held in the Consumer class if
	// it leaves.
	public void removeFromConsumer() {
		consumer.remove(this);
	}
	
	// Fetch the username and return as a String.
	public String getUsername() {
		return username;
	}
	
	// Print method using the PrintWriter instance - socketOut.
	public void printEach(String s) {
		socketOut.println(s);
	}
}

// Consumer class works with the BoundedBuffer class and the Producer class in
// order to keep track of the connected clients and print messages from the
// BoundedBuffer to all connected clients.
class Consumer extends Thread {
	// BoundedBuffer for the class to interact with
	private BoundedBuffer buffer;
	// Variables to hold information relevant for the users
	private ArrayList<Producer> producers = new ArrayList<Producer>();
	private String message = null;
	private ArrayList<String> usernames = new ArrayList<String>();
	private Consumer consumer = null;
	private PrintWriter socketOut = null;
	
	// No-args constructor
	public Consumer() {
	}
	
	// All-args constructor
	public Consumer(BoundedBuffer buffer) {
		this.buffer = buffer;
	}
	
	// Method to run - works off the assumption that as long as it is
	// running it will be able to remove a line from the BoundedBuffer.
	public void run() {
		while(true) {
			message = buffer.removeLine();
			printMsg(message);
		}
	}
	
	// Add a client to an ArrayList of Producers and the username from the
	// client to an ArrayList of Strings - used for printing.
	public void add(Producer p) {
		producers.add(p);
		printMsg(p.getUsername() + " has joined.");
		usernames.add(p.getUsername());
		System.out.println("Client: " + producers.size());
	}
	
	// Removes a client from the ArrayList of Producers and removes the
	// username from the ArrayList of Strings after printing a leave message
	public void remove(Producer p) {
		int index = producers.indexOf(p);
		producers.remove(index);
		printMsg(usernames.get(index) + " has left.");
		usernames.remove(index);
		System.out.println("Client: " + producers.size());
	}
	
	// Set up iterator variables to run through both ArrayLists and print to
	// each currently connected client.
	public void printMsg(String message) {
		Iterator<Producer> itr = producers.iterator();
		Iterator<String> itr2 = usernames.iterator();
		while(itr.hasNext()) {
			Producer temp = itr.next();
			temp.printEach(message);
		}
	}
}

// ChatServer class is the main class of the program, setting up all the
// variables in order to interact and start the threads for both Consumer and
// Producer.
class ChatServer {
	public static void main(String [] args) throws IOException {
		BoundedBuffer buffer = new BoundedBuffer(25);
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(7777);
		}
		catch (IOException i) {
			System.err.println("Could not listen on port 7777");
			System.exit(-1);
		}
		Consumer c = new Consumer(buffer);
		c.start();
		while(true) {
			try {
				Producer p = new Producer(buffer, c,
					serverSocket.accept());
				p.start();
			}
			catch (IOException i) {
				i.printStackTrace();
			}
		}
	}
}
