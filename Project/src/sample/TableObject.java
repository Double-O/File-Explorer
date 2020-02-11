package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by Dhruboo on 4/25/2017.
 */
public class TableObject {


    private ImageView Img;
    private String Name;
    private long Size;
    private String DateModified;
    private String RealName;



    public TableObject(ImageView ss, File some){
        Img = ss;
        Name = some.toString();
        Size = 0;
        DateModified = null;
        RealName = some.toString();
    }

    public TableObject(File some){

        boolean flg = false;

        for(File f : Controller.ini_pths){

            if(f.toString() == some.toString()) {
                flg = true;
            }

        }

        ImageIcon icon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(some);
        BufferedImage bufferedImage = (BufferedImage)icon.getImage();
        WritableImage fxImage = new WritableImage(icon.getIconWidth(), icon.getIconHeight());
        Image imgg = SwingFXUtils.toFXImage(bufferedImage, fxImage);

        Img = new ImageView(imgg);
        Name = some.toString();
        Size = some.length();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        DateModified = sdf.format(some.lastModified());

        //System.out.println("oi class e" + some.toString());

        if(flg == false)RealName = some.toPath().getFileName().toString();
        else{
            RealName = Name.substring(0, 1) + " Drive";

        }


    }

    public void setImg(Image im){
        Img.setImage(im);
    }
    public void setName(String nm){
        Name = nm;
    }
    public void setSize(int sz){
        Size = sz;
    }
    public void setDateModified(String dt){
        DateModified = dt;
    }

    public void setRealName(String nm){
        RealName = nm;
    }


    public ImageView getImg(){
        return Img;
    }
    public String getName(){
        return  Name;
    }
    public long getSize(){
        return Size;
    }
    public String getDateModified(){
        return DateModified;
    }
    public String getRealName(){
        return  RealName;
    }


    public  String toString(){
        return RealName;
    }

}
