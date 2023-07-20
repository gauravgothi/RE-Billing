package in.co.mpwin.rebilling.miscellanious;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadPath {
    public static String uploadedPath="D:/REBilling/Upload/File/";//change drive f/d/c
    public static String createUploadedPath() {
        String currentMonthFolder=new SimpleDateFormat("MMM_yyyy").format(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()))+"/";
        UploadPath.uploadedPath=UploadPath.uploadedPath+currentMonthFolder;
        if (!(new File(uploadedPath)).exists()) {
            (new File(uploadedPath)).mkdirs();    // creates the directory if it does not exist
        }
        return UploadPath.uploadedPath;
    }//method end
    public static void main(String args[])
    {
        //System.out.println(new SimpleDateFormat("MMM_yyyy").format(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())));
        UploadPath.createUploadedPath();

    }
}
