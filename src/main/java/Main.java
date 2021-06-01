import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

//this the connection work for the whole project, need more future work in this class.
        // please do not run this class
        String Path = "E:/FYP_Project/AviProcess/src/main/resources/AviFrameJpg/20191114_1BTWA_2MGMQ.avi";
        File file = new File(Path);
        //get the file list in the folder
        File[] files = file .listFiles();
        for(int i =0; i<files.length;i++){
            if(files[i].isFile()){
                //System.out.println(files[i].getName()+"+"+files[i].getPath());
                String ImagePath = "file:///E:/FYP_Project/AviProcess/src/main/resources/AviFrameJpg/20191114_1BTWA_2MGMQ.avi/"+files[i].getName();
                ObtainRGB.GetAverageRGB(ImagePath,i);
            }
        }

    }
}
