package in.co.mpwin.rebilling.miscellanious;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadPath {
    public static String uploadedPath="C:/REBilling/Upload/File/";//change drive f/d/c
    public static String createUploadedPath() {
        String currentMonthFolder=new SimpleDateFormat("MMM_yyyy").format(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()))+"/";
        //UploadPath.uploadedPath=UploadPath.uploadedPath+currentMonthFolder;
        if (!(new File(UploadPath.uploadedPath+currentMonthFolder)).exists()) {
            (new File(UploadPath.uploadedPath+currentMonthFolder)).mkdirs();    // creates the directory if it does not exist
        }
        return UploadPath.uploadedPath+currentMonthFolder;
    }//method end
    public static void main(String args[])
    {
        //System.out.println(new SimpleDateFormat("MMM_yyyy").format(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())));
        UploadPath.createUploadedPath();

    }
}
