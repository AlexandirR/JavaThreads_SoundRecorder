import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        String ACCESS_TOKEN = "sl.Al6WnnfWxTy0jW-CAFQC4aZpNAwntxqp4XLuKgSefK4Acm0TVKnPjWd275VQk2SoSaYMpfus6FNyEOzQeJgvifviofjsE_e9V4ffpcaa7jiNA1yGUP_IptK-hSvUm5CJEaxsxN-8gJw";
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        JavaSoundRecorder recorder = new JavaSoundRecorder(client);
        Scanner inPut = new Scanner(System.in);
        System.out.println("Введите количество записей:");
        int counterOfRecordFile = inPut.nextInt();
        for(int i = 0; i<counterOfRecordFile; ++i) {
            recorder.recordAudio(10000);
        }
    }
}
