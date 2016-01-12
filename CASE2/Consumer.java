import javax.sound.sampled.*;
import java.io.*;

public class Consumer extends Thread {
	public void run(char turn, DataLine.Info info,AudioFormat format,AudioInputStream audio,byte[] audioData,BoundedBuffer b){
		try{
			while(turn=='p'){
				sleep(100);
			}
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		turn='c';
		SourceDataLine data;
		try{
			int i=0;
			while(i<audioData.length){
				audioData[i]=b.removeChunk();
				i++;
			}
			//int bytesRead = audio.read(audioData);
			data = (SourceDataLine) AudioSystem.getLine(info);
			data.open(format);
			data.start();
			data.write(audioData, 0, audioData.length);
			data.drain();
			data.stop();
			data.close();
		}
		catch(LineUnavailableException l){
			l.printStackTrace();
		}
		//catch(IOException i){
		//	i.printStackTrace();
		//}
    }
	void quit(){
		System.out.println("Consumer thread is dead. RIP");
	}
}
