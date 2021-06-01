import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.bytedeco.javacv.*;


import javax.imageio.ImageIO;


//process the video and take the frames as jpg picture file
public class VideoProcess {

    //the video file path e.g. "E:/FYP_Project/AviProcess/src/main/resources/AviTxt";
    private static String VideoPath = "E:/FYP_Project/AviProcess/src/main/resources/AviTxt";
    //the frames jpg store path e.g. "E:/FYP_Project/AviProcess/src/main/resources/AviFrameJpg"
    private static String VideoFrameJpgPath = "E:/FYP_Project/AviProcess/src/main/resources/AviFrameJpg";

    public static void GainVideoFrame(String VideoFileName) {
        //create the frame
        Frame frame = null;
        //set the sign
        int flag = 0;
        //gain the video file
        FFmpegFrameGrabber FrameGrabber = new FFmpegFrameGrabber(VideoPath + "/" + VideoFileName);
        try {
            FrameGrabber.start();
            //get the video infor total frame
            int TotalFrames = FrameGrabber.getLengthInFrames();
            System.out.println(FrameGrabber.grabKeyFrame());
            System.out.println("Time: " + TotalFrames / FrameGrabber.getFrameRate() / 60);

            BufferedImage Bimage = null;
            System.out.println("Start grab frame");

            while (flag <= TotalFrames) {
                //set the file path and name
                String FileName = VideoFrameJpgPath + "/img_" + String.valueOf(flag) + ".jpg";

                File OutPut = new File(FileName);
                frame = FrameGrabber.grabImage();
                System.out.println(frame);

                if (frame != null) {
                    ImageIO.write(FrameToBufferedImage(frame), "jpg", OutPut);
                }
                flag++;
            }
            System.out.println(" =======End======");
            FrameGrabber.stop();
        } catch (IOException E) {

        }
    }

    public static BufferedImage FrameToBufferedImage(Frame frame) {
        //creat the BufferdImage
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }

    public static void main(String[] args) {
        //test run time
        //long startTime = System.currentTimeMillis();

        //make sure the video is in the video path folder
        String VideoFileName = "20191114_1BTWA_2MGMQ.avi";
        GainVideoFrame(VideoFileName);

        //test run time
       // long endTime = System.currentTimeMillis();
       // System.out.println("Running time: " + (endTime - startTime) + "ms");

    }

    public static String getVideoPath() {
        return VideoPath;
    }

    public static void setVideoPath(String VideoPath) {
        VideoProcess.VideoPath = VideoPath;
    }

}
