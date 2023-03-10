package com.wenwei.latticeencryption.Controller;

import com.wenwei.latticeencryption.Components.Parameters;
import com.wenwei.latticeencryption.LWE;
import com.wenwei.latticeencryption.Utils.Matrix;
import com.wenwei.latticeencryption.Utils.WriteFile;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.ZipOutputStream;

@Controller
public class LWEController {
    @PostMapping("lweEncrypt")
    public String lweEncrypt(HttpServletResponse response,
                             HttpServletRequest request,
                             @RequestParam(value = "messagebit",required = false)boolean messagebit,
                             @RequestParam("mode")boolean en,
                             @RequestParam(value = "PF",required = false)MultipartFile PublicFile,
                             @RequestParam(value="SK",required = false)MultipartFile SecretFile,
                             Model model) throws Exception {
        if (en) {
            String ip = getIpAddr(request);
            String fileName = DigestUtils.md5DigestAsHex((ip+System.currentTimeMillis()).getBytes());
            LWE lwe = new LWE();
            lwe.setMessage(messagebit);
            lwe.Encrypt();
            //System.out.println(messagebit+" "+lwe.Decrypt());
            WriteFile.writeandreturn(lwe.toString(), lwe.getSK().toString(), fileName, response);
        }else{
            if(PublicFile==null||SecretFile==null||PublicFile.isEmpty()||SecretFile.isEmpty()){
                model.addAttribute("error","???????????????");
                return "index";
            }
            Parameters params=new Parameters();
            LWE lwe=new LWE();
            setParameterandLwe(params,lwe,PublicFile,SecretFile);
            model.addAttribute("result","??????????????????"+(lwe.Decrypt()?1:0));
        }
        return "index";
    }
    public String getIpAddr(HttpServletRequest request) {
        //???????????????"x-forwarded-for"?????????value
        String ip = request.getHeader("x-forwarded-for");
        //???????????????ip?????????
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //??????????????????"Proxy-Client-IP"?????????value
            ip = request.getHeader("Proxy-Client-IP");
        }
        //???????????????ip????????????
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //??????????????????"WL-Proxy-Client-IP"?????????value
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        //???????????????????????????ip????????????
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //???????????????ip??????
            ip = request.getRemoteAddr();
        }
        //??????ip??????
        return ip;
    }
    public void setParameterandLwe(Parameters params, LWE lwe, MultipartFile PF, MultipartFile SF) throws IOException {
        Scanner in=new Scanner(PF.getInputStream());//??????PF??????
        int n,m;
        long q;
        double sigma;
        //??????n???m???q???sigma
        String[] temp=in.next().split(":");
        n=Integer.valueOf(temp[1]);
        temp=in.next().split(":");
        m=Integer.valueOf(temp[1]);
        temp=in.next().split(":");
        q=Long.valueOf(temp[1]);
        temp=in.next().split(":");
        sigma=Double.valueOf(temp[1]);

        in.next();//??????C1
        long[][] C1=new long[n][1];
        String t="";
        for(int i=0;i<n;i++){//??????C1??????
            t=in.next();
            t=t.substring(1,t.length()-1);
            C1[i][0]=Long.valueOf(t);
        }

        in.next();//??????C2
        long[][] C2=new long[1][1];
        t=in.next();
        t=t.substring(1,t.length()-1);
        C2[0][0]=Long.valueOf(t);

        in=new Scanner(SF.getInputStream());//??????SK??????
        long[][] SK=new long[n][1];
        for(int i=0;i<n;i++){
            t=in.next();
            t=t.substring(1,t.length()-1);
            SK[i][0]=Long.valueOf(t);
        }

        //????????????
        params.setN(n);
        params.setM(m);
        params.setQ(q);
        params.setSigma(sigma);

        lwe.setC1(new Matrix(C1,q));
        lwe.setC2(new Matrix(C2,q));
        lwe.setSK(new Matrix(SK,q));
        lwe.setParams(params);
    }
}
