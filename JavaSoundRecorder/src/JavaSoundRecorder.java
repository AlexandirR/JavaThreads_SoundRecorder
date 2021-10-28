import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import javax.sound.sampled.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import static java.lang.Thread.sleep;

public class JavaSoundRecorder
{
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    TargetDataLine line;
    DbxClientV2 client;
    AudioFormat format;

    public JavaSoundRecorder(DbxClientV2 client)
    {
        this.client = client;
        format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line not supported");
            System.exit(0);
        }
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian
        );
        return format;
    }

    public void recordAudio(int milliseconds)
    {
        //TODO: 20201120_201454.wav
        SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy_HHmmss");
        Date date = new Date();
        String dateInString = formatter.format(date) + ".wav";
        String fileName = "C:/Users/Александр/Desktop/" + dateInString;
        File file = new File(fileName);
        start(file);
        stop(milliseconds, file, dateInString, fileName);
        try {
            sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void start(File file)
    {
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    line.open(format);
                    line.start();   // start capturing
                    AudioInputStream ais = new AudioInputStream(line);
                    AudioSystem.write(ais, fileType, file);
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        };
        thread.start();
    }

    void stop(int milliseconds, File file, String fileName, String puthFile)
    {
        Thread thread = new Thread(){
            @Override
            public void run()
            {
                try {
                    sleep(milliseconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                line.stop();
                line.close();
                try (InputStream in = new FileInputStream(puthFile)) {
                    String pathInString = "/" + fileName;
                    FileMetadata metadata = client.files().uploadBuilder(pathInString)
                            .uploadAndFinish(in);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                if(!file.delete()) {
                    System.out.println("File is not exist!");
                }
                //TODO: upload file to Dropbox
                //TODO: delete file
            }
        };
        thread.start();
    }
}