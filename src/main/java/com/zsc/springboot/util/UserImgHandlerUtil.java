package com.zsc.springboot.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;

/**
 *
 *
 *@Author：黄港团
 *@Since：2021/2/21 14:38
 */
@Component
public class UserImgHandlerUtil {

    private static String localPath;
    @Value("${zsc.userImg}")
    public void setLocalPath(String localPath){
        this.localPath = localPath;
        rootLocation = Paths.get(this.localPath);
    }

    private static Path rootLocation;

    // 获取一个临时文件
    private static Path tempPath = null;
    private static Path dir = Paths.get("D:/software_HGT/IntelliJ IEDA/project/Spring Boot/campus_helper_upload_image/temp");

    /**
     *
     * @param request
     * @param file
     * @param additionalName
     * @return
     */
    public static String handleImg(HttpServletRequest request, MultipartFile file, Object additionalName){

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (originalFilename.contains("..") || !originalFilename.contains(".")) {
            throw new RuntimeException("图片格式有误");
        }
        MessageDigest digest = DigestUtils.getSha1Digest();

        try {
            tempPath = Files.createTempFile(dir,"temp_", null);
            InputStream in = file.getInputStream();
            OutputStream out = Files.newOutputStream(tempPath);
            byte[] bytes = new byte[8192];
            int n;

            digest.update(originalFilename.getBytes());
            while (-1 != (n = in.read(bytes))) {
                digest.update(bytes);
                out.write(bytes);
            }

            // 到此，要处理的MultiPartFile对象就处理完成了
            String imgNewName = Hex.encodeHexString(digest.digest()) + additionalName +suffix;

            Path newPath = rootLocation.resolve(imgNewName);

            /*
            // 新文件路径(包括文件名后缀等)如果存在，就删除临时文件，不会进行覆盖
            if (Files.exists(newPath)) {
                Files.delete(tempPath);
            } else {
                Files.move(tempPath, newPath);
            }
            */
            // 进行文件覆盖
            Files.move(tempPath, newPath, StandardCopyOption.REPLACE_EXISTING);


            return getHost(new URI(request.getRequestURI() + "")) + "/userImg/" + imgNewName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("图片处理出错");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("图片处理出错");
        }


    }

    private static URI getHost(URI uri) throws URISyntaxException {
        return new URI(uri.getScheme(),
                uri.getUserInfo(),
                uri.getHost(),
                uri.getPort(),
                null, null, null);

    }
}
