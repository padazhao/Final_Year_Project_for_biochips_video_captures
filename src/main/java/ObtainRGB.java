import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_core.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import java.net.URL;

import static org.bytedeco.opencv.global.opencv_highgui.imshow;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

import static org.bytedeco.opencv.global.opencv_highgui.waitKey;


public class ObtainRGB {

    //set the background color to display check area
    static Color blank = new Color(0, 0, 0);
    static int BLANK = blank.getRGB();

    public static void main(String[] args) throws IOException {
        // this for testing running time
        // long startTime = System.currentTimeMillis();

        FileWriter fw = null;
        try {
            //open file to write r-g-b resluts
            // if you rerun please name a new file e.g. E:/FYP_Project/AviProcess/saved1.txt
            File f = new File("E:/FYP_Project/AviProcess/saved1.txt");
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        //print the column and row
        pw.println("T\\Well");

        for (int m = 65; m < 73; m++) {

            for (int n = 1; n < 13; n++) {
                pw.printf("%20s", "l-" + (char) (m) + n);

            }
        }
        for (int m = 65; m < 73; m++) {

            for (int n = 1; n < 13; n++) {
                pw.printf("%20s", "r-" + (char) (m) + n);
            }

        }
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // set the frames file path e.g. E:/FYP_Project/AviProcess/src/main/resources/AviFrameJpg/20191122_1TWA_2MGTWA.avi
        String FilePath = "E:/FYP_Project/AviProcess/src/main/resources/AviFrameJpg/20191122_1TWA_2MGTWA.avi";
        File file = new File(FilePath);
        //get the file list in the folder
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                // traverse all the images path e.g. file:///E:/FYP_Project/AviProcess/src/main/resources/AviFrameJpg/20191122_1TWA_2MGTWA.avi/
                String ImagePath = "file:///E:/FYP_Project/AviProcess/src/main/resources/AviFrameJpg/20191122_1TWA_2MGTWA.avi/" + files[i].getName();
                GetAverageRGB(ImagePath, i);
            }
        }

        // this for testing running time
       // long endTime = System.currentTimeMillis();
       // System.out.println("Running time: " + (endTime - startTime) + "ms");

    }

    public static void GetAverageRGB(String ImagePath, int s) throws IOException {


        FileWriter fw = null;
        try {
            File f = new File("E:/FYP_Project/AviProcess/saved1.txt");
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);


        pw.println();
        pw.printf("%3s", s + 1);
        BufferedImage bufferedImage = readImageFile(new URL(ImagePath));


        //first filter to eliminate non-wells areas
        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                Color mycolor = new Color(bufferedImage.getRGB(i, j));

                if (mycolor.getGreen() > 130) {
                    bufferedImage.setRGB(i, j, BLANK);
                }

            }
        }
        // this is the Temporary picture for the check and calculation
        // this path use the url e.g. file:///E:/FYP_Project/AviProcess/saved.jpg
        String ProcessImgPath = "file:///E:/FYP_Project/AviProcess/saved.jpg";
        writeImageFile(bufferedImage);
        BufferedImage bufferedImage2 = readImageFile(new URL(ProcessImgPath));

        //second filter to eliminate the small noise for wells check
        for (int i = 0; i < bufferedImage2.getWidth(); i++) {
            for (int j = 0; j < bufferedImage2.getHeight(); j++) {
                Color mycolor = new Color(bufferedImage2.getRGB(i, j));

                if (mycolor.getBlue() < 130) {
                    bufferedImage2.setRGB(i, j, BLANK);
                }
            }
        }


        writeImageFile(bufferedImage2);
        // this is for check ponit in the wells by JavaCV mat attribute
//        Mat OriImg;
//        OriImg = imread("E:/FYP_Project/AviProcess/src/main/resources/AviFrameJpg/20191122_1TWA_2MGTWA.avi/img_1.jpg");
//
//        String ImagPath = "E:/FYP_Project/AviProcess/saved.jpg";
//        Mat srcimage;
//        srcimage = imread(ImagPath);
//        if (srcimage.empty()) {
//            System.out.println("Image loading errors");
//            return;
//        }

        // calculate the average R G B value of every single well
        BufferedImage bufferedImage1 = readImageFile(new URL(ProcessImgPath));
        //    Mat dstimg = srcimage.clone();


        //set the i and j according to the image width and height

        //1050
//        for (int i = 374; i < bufferedImage1.getWidth() / 2; i = i + 60) {
//            for (int j = 159; j < 912; j = j + 65) {
        if (bufferedImage1.getHeight() == 1050) {
            for (int i = 374; i < bufferedImage1.getWidth() / 2; i = i + 60) {
                for (int j = 159; j < 912; j = j + 65) {
                    // this is for check ponit in the wells by JavaCV circle function
                    //get the red circle to check the area
                    //   Point p = new Point(i, j);
                    //  circle(dstimg, p, 25, new Scalar(0, 0, 255, 0));

                    int r = 0;
                    while (new Color((bufferedImage1.getRGB(i + r, j))).getBlue() != 0) {
                        r++;
                    }
                    int l = 0;
                    while (new Color((bufferedImage1.getRGB(i + l, j))).getBlue() != 0) {
                        l--;
                    }
                    int u = 0;
                    while (new Color((bufferedImage1.getRGB(i, j + u))).getBlue() != 0) {
                        u--;
                    }
                    int d = 0;
                    while (new Color((bufferedImage1.getRGB(i, j + d))).getBlue() != 0) {
                        d++;
                    }

                    //limit the area for the over area
                    if (r - l > 50) {
                        if (r > -l) {
                            r = 50 + l;
                        }
                        l = r - 50;
                    }
                    if (d - u > 50) {
                        if (d > -u) {
                            d = 50 + u;
                        }
                        l = r - 50;
                    }


                    int TBValue = 0;
                    int TGValue = 0;
                    int TRValue = 0;
                    int count = 1;

                    for (int m = i + l; m < i + r; m++) {
                        for (int n = j + u; n < j + d; n++) {
                            Point point = new Point(m, n);

                            Color mycolor = new Color(bufferedImage1.getRGB(point.x(), point.y()));
                            if (mycolor.getBlue() > 130) {
                                TBValue = TBValue + mycolor.getBlue();
                                TGValue = TGValue + mycolor.getGreen();
                                TRValue = TRValue + mycolor.getRed();
                                count = count + 1;

                                bufferedImage1.setRGB(point.x(), point.y(), BLANK);
                            }
                        }
                    }


                    double averB = TBValue / count;
                    double averG = TGValue / count;
                    double averR = TRValue / count;
                    pw.printf("%20s", averB + "-" + averG + "-" + averR);


                }
            }
        }
        //1050
//        for (int i = 374; i < bufferedImage1.getWidth() / 2; i = i + 60) {
//            for (int j = 159; j < 912; j = j + 65) {

        //1080
        else {
            for (int i = 420; i < (bufferedImage1.getWidth() - 120) / 2; i = i + 67) {
                for (int j = 159; j < 912; j = j + 67) {
                    // this is for check ponit in the wells by JavaCV circle function
                    //get the red circle to check the area
                    // Point p = new Point(i, j);
                    //  circle(dstimg, p, 25, new Scalar(0, 0, 255, 0));

                    int r = 0;
                    while (new Color((bufferedImage1.getRGB(i + r, j))).getBlue() != 0) {
                        r++;
                    }
                    int l = 0;
                    while (new Color((bufferedImage1.getRGB(i + l, j))).getBlue() != 0) {
                        l--;
                    }
                    int u = 0;
                    while (new Color((bufferedImage1.getRGB(i, j + u))).getBlue() != 0) {
                        u--;
                    }
                    int d = 0;
                    while (new Color((bufferedImage1.getRGB(i, j + d))).getBlue() != 0) {
                        d++;
                    }

                    //limit the area for the over area
                    if (r - l > 50) {
                        if (r > -l) {
                            r = 50 + l;
                        }
                        l = r - 50;
                    }
                    if (d - u > 50) {
                        if (d > -u) {
                            d = 50 + u;
                        }
                        l = r - 50;
                    }



                    int TBValue = 0;
                    int TGValue = 0;
                    int TRValue = 0;
                    int count = 1;

                    for (int m = i + l; m < i + r; m++) {
                        for (int n = j + u; n < j + d; n++) {
                            Point point = new Point(m, n);

                            Color mycolor = new Color(bufferedImage1.getRGB(point.x(), point.y()));
                            if (mycolor.getBlue() > 130) {
                                TBValue = TBValue + mycolor.getBlue();
                                TGValue = TGValue + mycolor.getGreen();
                                TRValue = TRValue + mycolor.getRed();
                                count = count + 1;

                                bufferedImage1.setRGB(point.x(), point.y(), BLANK);
                            }
                        }
                    }


                    double averB = TBValue / count;
                    double averG = TGValue / count;
                    double averR = TRValue / count;
                    pw.printf("%20s", averB + "-" + averG + "-" + averR);


                }
            }
        }
        //1050
//        for (int i = 925; i < 1376; i = i + 60) {
//            for (int j = 155; j < 912; j = j + 65) {

        if (bufferedImage1.getHeight() == 1050) {
            for (int i = 925; i < 1376; i = i + 60) {
                for (int j = 155; j < 912; j = j + 65) {
                    // this is for check ponit in the wells by JavaCV circle function
                    //  Point p = new Point(i, j);
                    //circle(dstimg, p, 25, new Scalar(0, 0, 255, 0));

                    int r = 0;
                    while (new Color((bufferedImage1.getRGB(i + r, j))).getBlue() != 0) {
                        r++;
                    }
                    int l = 0;
                    while (new Color((bufferedImage1.getRGB(i + l, j))).getBlue() != 0) {
                        l--;
                    }
                    int u = 0;
                    while (new Color((bufferedImage1.getRGB(i, j + u))).getBlue() != 0) {
                        u--;
                    }
                    int d = 0;
                    while (new Color((bufferedImage1.getRGB(i, j + d))).getBlue() != 0) {
                        d++;
                    }

                    //limit the area for the over area
                    if (r - l > 50) {
                        if (r > -l) {
                            r = 50 + l;
                        }
                        l = r - 50;
                    }
                    if (d - u > 50) {
                        if (d > -u) {
                            d = 50 + u;
                        }
                        l = r - 50;
                    }


                    int TBValue = 0;
                    int TGValue = 0;
                    int TRValue = 0;
                    int count = 1;

                    for (int m = i + l; m < i + r; m++) {
                        for (int n = j + u; n < j + d; n++) {
                            Point point = new Point(m, n);

                            Color mycolor = new Color(bufferedImage1.getRGB(point.x(), point.y()));
                            if (mycolor.getBlue() > 130) {
                                TBValue = TBValue + mycolor.getBlue();
                                TGValue = TGValue + mycolor.getGreen();
                                TRValue = TRValue + mycolor.getRed();
                                count = count + 1;

                                bufferedImage1.setRGB(point.x(), point.y(), BLANK);
                            }
                        }
                    }


                    double averB = TBValue / count;
                    double averG = TGValue / count;
                    double averR = TRValue / count;
                    pw.printf("%20s", averB + "-" + averG + "-" + averR);
                }
            }
        }
        //1080
        else {
            for (int i = 1035; i < 1520; i = i + 69) {
                for (int j = 155; j < 912; j = j + 67) {
                    // this is for check ponit in the wells by JavaCV circle function
                    //         Point p = new Point(i, j);
                    //circle(dstimg, p, 25, new Scalar(0, 0, 255, 0));

                    int r = 0;
                    while (new Color((bufferedImage1.getRGB(i + r, j))).getBlue() != 0) {
                        r++;
                    }
                    int l = 0;
                    while (new Color((bufferedImage1.getRGB(i + l, j))).getBlue() != 0) {
                        l--;
                    }
                    int u = 0;
                    while (new Color((bufferedImage1.getRGB(i, j + u))).getBlue() != 0) {
                        u--;
                    }
                    int d = 0;
                    while (new Color((bufferedImage1.getRGB(i, j + d))).getBlue() != 0) {
                        d++;
                    }

                    //limit the area for the over area
                    if (r - l > 50) {
                        if (r > -l) {
                            r = 50 + l;
                        }
                        l = r - 50;
                    }
                    if (d - u > 50) {
                        if (d > -u) {
                            d = 50 + u;
                        }
                        l = r - 50;
                    }


                    int TBValue = 0;
                    int TGValue = 0;
                    int TRValue = 0;
                    int count = 1;

                    for (int m = i + l; m < i + r; m++) {
                        for (int n = j + u; n < j + d; n++) {
                            Point point = new Point(m, n);

                            Color mycolor = new Color(bufferedImage1.getRGB(point.x(), point.y()));
                            if (mycolor.getBlue() > 120) {
                                TBValue = TBValue + mycolor.getBlue();
                                TGValue = TGValue + mycolor.getGreen();
                                TRValue = TRValue + mycolor.getRed();
                                count = count + 1;

                                bufferedImage1.setRGB(point.x(), point.y(), BLANK);
                            }
                        }
                    }


                    double averB = TBValue / count;
                    double averG = TGValue / count;
                    double averR = TRValue / count;
                    pw.printf("%20s", averB + "-" + averG + "-" + averR);
                }
            }
        }
        pw.flush();

        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeImageFile(bufferedImage1);
//        this is use the JavaCV to check the point location in the wells
        //by draw a circle with the center of the point
//        circle(dstimg, new Point(374,159 ), 22, new Scalar(0,0,255,0));
//        circle(dstimg, new Point(431,159 ), 22, new Scalar(0,0,255,0));
//        circle(dstimg, new Point(374,224 ), 22, new Scalar(0,0,255,0));
//        imshow("Ori", OriImg);
//        imshow("2", dstimg);
//        waitKey(0);


    }


    public static BufferedImage readImageFile(URL file) {
        try {
            BufferedImage image = ImageIO.read(file);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeImageFile(BufferedImage bi) throws IOException {
        File outputFile = new File("saved.jpg");
        ImageIO.write(bi, "jpg", outputFile);
    }


}
