package com.wenwei.latticeencryption.Utils;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileZipUtil {

    /**
     * 将指定路径下的所有文件打包zip导出
     * @param response HttpServletResponse
     * @param sourceFilePath 需要打包的文件夹路径
     * @param fileName 下载时的文件名称
     * @param postfix 下载时的文件后缀 .zip/.rar
     */
    public static void exportZip(HttpServletResponse response, String sourceFilePath, String fileName, String postfix) {
        // 默认文件名以时间戳作为前缀
        if(StringUtils.isBlank(fileName)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            fileName = sdf.format(new Date());
        }
        String downloadName = fileName + postfix;
        // 将文件进行打包下载
        try {
            OutputStream os = response.getOutputStream();
            // 接收压缩包字节
            byte[] data = createZip(sourceFilePath);
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Expose-Headers", "*");
            // 下载文件名乱码问题
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadName, "UTF-8"));
            //response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + downloadName);
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream;charset=UTF-8");
            IOUtils.write(data, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建zip文件
     * @param sourceFilePath
     * @return byte[]
     * @throws Exception
     */
    private static byte[] createZip(String sourceFilePath) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        // 将目标文件打包成zip导出
        File file = new File(sourceFilePath);
        handlerFile(zip, file,"");
        // 无异常关闭流，它将无条件的关闭一个可被关闭的对象而不抛出任何异常。
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 打包处理
     * @param zip
     * @param file
     * @param dir
     * @throws Exception
     */
    private static void handlerFile(ZipOutputStream zip, File file, String dir) throws Exception {
        // 如果当前的是文件夹，则循环里面的内容继续处理
        if (file.isDirectory()) {
            //得到文件列表信息
            File[] fileArray = file.listFiles();
            if (fileArray == null) {
                return;
            }
            //将文件夹添加到下一级打包目录
            zip.putNextEntry(new ZipEntry(dir + "/"));
            dir = dir.length() == 0 ? "" : dir + "/";
            // 递归将文件夹中的文件打包
            for (File f : fileArray) {
                handlerFile(zip, f, dir + f.getName());
            }
        } else {
            // 如果当前的是文件，打包处理
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(dir);
            zip.putNextEntry(entry);
            zip.write(FileUtils.readFileToByteArray(file));
            IOUtils.closeQuietly(bis);
            zip.flush();
            zip.closeEntry();
        }
    }


}
