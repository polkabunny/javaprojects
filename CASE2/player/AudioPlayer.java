import javax.sound.sampled.*;
import java.io.*;
import java.lang.*;
import java.util.*;

class AudioPlayer {
		private static AudioInputStream validate(String[] args) throws UnsupportedAudioFileException, IOException {
    	if (args.length != 1) {
    		System.out.println("Please supply an audio file");
    		System.exit(1);
    	}
    	return (AudioSystem.getAudioInputStream(new File(args[0])));
    }
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		AudioInputStream audio = validate(args);
		AudioFormat format = audio.getFormat();	    
		System.out.println("Audio format: " + format.toString());
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		if (!AudioSystem.isLineSupported(info)) {
			System.out.println("Cannot handle that audio format");
			System.exit(1);
		}
		int oneSecond = (int) (format.getChannels() * format.getSampleRate() * 
		format.getSampleSizeInBits() / 8);
		byte[] audioData = new byte[oneSecond];
		char turn='p';
		boolean quit=false;
		BoundedBuffer b=new BoundedBuffer(oneSecond*10);
		Producer p=new Producer();
		Consumer c=new Consumer();
		p.start();
		c.start();
		while(!quit){
			try{
				p.run(turn,info,format,audio,audioData,b);
				turn='c';
				p.sleep(10);
				c.run(turn,info,format,audio,audioData,b);
				c.sleep(10);
				turn='p';
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		p.quit();
		c.quit();
		try{
			p.join();
			c.join();
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		System.exit(0);
	}
	static boolean checkForInput(boolean quit){
		Scanner scanIn = new Scanner(System.in);
		if(scanIn.hasNext()){
			String check=scanIn.next();
			if(check=="x"){
				quit=true;
			}
		}
		return quit;
	}
}
