package com.wenwei.latticeencryption.Utils;

import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
    public static void writeandreturn(String content1, String content2, String filename, HttpServletResponse response) throws IOException {
        FileWriter fw = null;
        String directory="D:/test/"+filename;
        String path1 = directory+"/public.txt";
        String path2 = directory+"/secret.txt";
        try
        {
            File d=new File(directory);
            if(!d.exists()){
                d.mkdirs();
            }//创建专属目录
            File file = new File(path1);
            if (!file.exists())
            {
                file.createNewFile();
            }
            fw = new FileWriter(path1);
            fw.write(content1);
            fw.flush();

            file = new File(path2);
            if (!file.exists())
            {
                file.createNewFile();
            }
            fw = new FileWriter(path2);
            fw.write(content2);
            fw.flush();//写完文件
            FileZipUtil.exportZip(response, directory, "加密结果", ".zip");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            fw.close();
        }

    }
}
