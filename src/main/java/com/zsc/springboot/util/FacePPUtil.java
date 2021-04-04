package com.zsc.springboot.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.SSLException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 *
 *  封装face++第三方人脸识别平台工具
 *@Author：黄港团
 *@Since：2021/1/14 16:47
 */
@Component
public class FacePPUtil {
    private static String API_KEY;
    private static String API_SECRET;

    @Value("${zsc.face.apiKey}")
    public void setApiKey(String apiKey){
        API_KEY = apiKey;
    }
    @Value("${zsc.face.apiSecret}")
    public void setApiSecret(String apiSecret){
        API_SECRET = apiSecret;
    }

    private final static String FACE_ADD_USERID_TO_FACETOKEN_URL = "https://api-cn.faceplusplus.com/facepp/v3/face/setuserid";
    private final static String FACE_SEARCH_URL = "https://api-cn.faceplusplus.com/facepp/v3/search";
    private final static String GET_FACETOKEN_FROM_IMAGE_URL = "https://api-cn.faceplusplus.com/facepp/v3/detect";
    private final static String FACE_ADD_FACETOKEN_TO_FACESET_URL = "https://api-cn.faceplusplus.com/facepp/v3/faceset/addface";
    private final static String CREATE_FACESET_URL = "https://api-cn.faceplusplus.com/facepp/v3/faceset/create";
    private final static String GET_FACESET_DETAIL_URL = "https://api-cn.faceplusplus.com/facepp/v3/faceset/getdetail";
    private final static String REMOTE_FACE_FROM_FACESET = "https://api-cn.faceplusplus.com/facepp/v3/faceset/removeface";
    private final static String GET_FACESETS_URL = "https://api-cn.faceplusplus.com/facepp/v3/faceset/getfacesets";

    /**
     *  给对应的faceToken添加userId  以方便人脸登录的时候检索出该用户id
     * @param userId
     * @param faceToken
     */
    public static boolean addUserIDToFaceToken(String userId,String faceToken) throws Exception {
        // 给用户添加标识
        HashMap<String,String> addUserIdMap = new HashMap<>();
        addUserIdMap.put("api_key",API_KEY);
        addUserIdMap.put("api_secret",API_SECRET);
        addUserIdMap.put("face_token",faceToken);
        addUserIdMap.put("user_id",userId);
        byte[] ret = post(FACE_ADD_USERID_TO_FACETOKEN_URL,addUserIdMap,null);
        String retStr = new String(ret);
        if(retStr.indexOf("error_message") != -1)
            throw new Exception("人脸识别请求错误");
        return true;
    }

    public static String imgToFaceToken(byte[] file) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", API_KEY);
        map.put("api_secret", API_SECRET);
        map.put("return_landmark", "1");
        map.put("return_attributes", "gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity,beauty,mouthstatus,eyegaze,skinstatus");
        byteMap.put("image_file", file);
        byte[] bacd = post(GET_FACETOKEN_FROM_IMAGE_URL,map,byteMap);
        String str = new String(bacd);
        if(str.indexOf("error_message") != -1)
            throw new Exception("人脸识别请求失败");
        //str转换为json对象
        JSONObject jsonObject = JSONObject.parseObject(str);
        int num = jsonObject.getIntValue("face_num");
        if(num == 1){
            JSONArray array = (JSONArray) jsonObject.get("faces");
            JSONObject face = (JSONObject) array.get(0);
            String faceToken = face.getString("face_token");
            return faceToken;
        }else{
            return "-1";
        }
    }

    /**
     *  根据图片文件将图片生成对应的facetoken
     * @param file
     * @return faceToken值 "-1"为非单人人脸
     */
    public static String imgToFaceToken(File file) throws Exception{
        //File file = new File(pathName);
        byte[] buff = getBytesFromFile(file);
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", API_KEY);
        map.put("api_secret", API_SECRET);
        map.put("return_landmark", "1");
        map.put("return_attributes", "gender,age,smiling,headpose,facequality,blur,eyestatus,emotion,ethnicity,beauty,mouthstatus,eyegaze,skinstatus");
        byteMap.put("image_file", buff);
        byte[] bacd = post(GET_FACETOKEN_FROM_IMAGE_URL,map,byteMap);
        String str = new String(bacd);
        if(str.indexOf("error_message") != -1)
            throw new Exception("人脸识别请求失败");
        //str转换为json对象
        JSONObject jsonObject = JSONObject.parseObject(str);
        int num = jsonObject.getIntValue("face_num");
        if(num == 1){
            JSONArray array = (JSONArray) jsonObject.get("faces");
            JSONObject face = (JSONObject) array.get(0);
            String faceToken = face.getString("face_token");
            return faceToken;
        }else{
            return "-1";
        }

    }

    /**
     * 根据图片MultipartFile对象将图片生成对应的facetoken
     * @param multipartFile
     * @return faceToken值
     * @throws Exception
     */
    public static String imgToFaceToken(MultipartFile multipartFile) throws Exception{
        File file = getFileFromMultipartFile(multipartFile);
        String faceToken = imgToFaceToken(file);
        file.delete(); //删除该临时file
        return faceToken;
    }

    /**
     *  根据图片路径将图片生成对应的facetoken
     * @param pathname
     * @return
     */
    public static String imgToFaceToken(String pathname) throws Exception {
        File file = new File(pathname);
        return imgToFaceToken(file);
    }

    /**
     *  使用缓冲区实现MultipartFile转化为file
     * @param multipartFile
     * @return
     */
    public static File getFileFromMultipartFile(MultipartFile multipartFile) throws IOException {
        File file = null;
        String originalFilename = multipartFile.getOriginalFilename();
        // 注意 这里不能用 originalFilename.split(".");
        String filename[] = originalFilename.split("\\.");
        // 此方案会不断创建，执行一次就创建一个
        file = File.createTempFile(filename[0],filename[1],new File("D:/software_HGT/IntelliJ IEDA/project/Spring Boot/campus_helper_upload_image/temp/"));
        multipartFile.transferTo(file);
        // 在程序正常结束后才会删除该file，此处springboot项目就不在这删除了待处理完该file处删除
        //file.deleteOnExit();
        return file;
    }

    /**
     *  根据faceToken和outerId 进行人脸检测是否通过
     * @param faceToken
     * @param outerId
     * @return    通过则返回该faceToken对应的用户标识（如手机号码或用户id）,"-1"为检测失败，不通过
     * @throws Exception
     */
    public static String searchFaceByFaceToken(String faceToken,String outerId) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        map.put("api_key", API_KEY);
        map.put("api_secret", API_SECRET);
        map.put("face_token",faceToken);
        map.put("outer_id",outerId);
        byte[] bacd = post(FACE_SEARCH_URL, map, null);
        String str = new String(bacd);
        //System.out.println(str);
        if(str.indexOf("error_message") != -1){
            throw new Exception("请求出错");
        }
        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONObject thresholdsObject = (JSONObject) jsonObject.get("thresholds");
        //String le5 = thresholdsObject.getString("1e-5"); // 十万分之一的误信率阈值
        double le5 = thresholdsObject.getDoubleValue("1e-5");
        JSONArray resArr = (JSONArray) jsonObject.get("results");
        if(resArr != null && resArr.size() >= 1){
            JSONObject res = (JSONObject) resArr.get(0);  // 取出数组的第一个对象
            double confidence = res.getDoubleValue("confidence");
            if(confidence > le5){
                String userId = res.getString("user_id");
                return userId;
            }
        }
        return "-1";
    }

    /**
     *  将faceToken加入到faceSet中
     * @param faceToken
     * @param outerId
     * @return
     */
    public static boolean addFaceTokenToFaceSet(String faceToken,String outerId) throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("api_key",API_KEY);
        map.put("api_secret",API_SECRET);
        map.put("outer_id",outerId);
        map.put("face_tokens",faceToken);
        byte[] bacd = post(FACE_ADD_FACETOKEN_TO_FACESET_URL,map,null);
        String str = new String(bacd);
        System.out.println(str);
        if(str.indexOf("error_message") != -1) {
            throw new Exception("人脸识别请求错误");
        }
        return true;
    }

    /**
     *  将faceToken 从 outerId对应的faceSet中移除
     * @param faceToken
     * @param outerId
     * @return
     */
    public static boolean remoteFaceFromFaceSet(String faceToken,String outerId) throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("api_key", API_KEY);
        map.put("api_secret", API_SECRET);
        map.put("face_tokens",faceToken);
        map.put("outer_id",outerId);
        byte[] bacd = post(REMOTE_FACE_FROM_FACESET,map,null);
        String str = new String(bacd);
        //System.out.println(str);
        if(str.indexOf("error_message")!=-1){
            //System.out.println("请求发生错误！");
            return false;
        }
        return true;
    }

    /**
     *  查询所有的faceset
     * @return
     * @throws Exception
     */
    public static JSONObject getFaceSets() throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("api_key",API_KEY);
        map.put("api_secret",API_SECRET);
        byte[] bacd = post(GET_FACESETS_URL,map,null);
        String str = new String(bacd);
        JSONObject jsonObject = JSONObject.parseObject(str);
        return jsonObject;
    }

    /**
     *  通过自定义的outerId创建一个FaceSet
     * @param outerId
     * @return
     */
    public static boolean createFaceSetByOuterId(String outerId) throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("api_key", API_KEY);
        map.put("api_secret", API_SECRET);
        map.put("outer_id",outerId);
        byte[] bacd = post(CREATE_FACESET_URL,map,null);
        String str = new String(bacd);
        //System.out.println(str);
        if(str.indexOf("error_message")!=-1)
            return false;
        return true;
    }

    /**
     *  查询 faceset的outerId 查询该faceset的详细信息
     * @param outerId
     * @return
     * @throws Exception
     */
    public static Object getFaceSetDetailByOuterId(String outerId) throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("api_key", API_KEY);
        map.put("api_secret", API_SECRET);
        map.put("outer_id",outerId);
        byte[] bacd = post(GET_FACESET_DETAIL_URL,map,null);
        String str = new String(bacd);
        JSONObject jsonObject = JSONObject.parseObject(str);
        return jsonObject;
    }





    //官方配置
    private final static int CONNECT_TIME_OUT = 30000;
    private final static int READ_OUT_TIME = 50000;
    private static String boundaryString = getBoundary();

    protected static byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {
        HttpURLConnection conne;
        URL url1 = new URL(url);
        conne = (HttpURLConnection) url1.openConnection();
        conne.setDoOutput(true);
        conne.setUseCaches(false);
        conne.setRequestMethod("POST");
        conne.setConnectTimeout(CONNECT_TIME_OUT);
        conne.setReadTimeout(READ_OUT_TIME);
        conne.setRequestProperty("accept", "*/*");
        conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        conne.setRequestProperty("connection", "Keep-Alive");
        conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            obos.writeBytes("--" + boundaryString + "\r\n");
            obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n");
            obos.writeBytes("\r\n");
            obos.writeBytes(value + "\r\n");
        }
        if(fileMap != null && fileMap.size() > 0){
            Iterator fileIter = fileMap.entrySet().iterator();
            while(fileIter.hasNext()){
                Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
                obos.writeBytes("--" + boundaryString + "\r\n");
                obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                        + "\"; filename=\"" + encode(" ") + "\"\r\n");
                obos.writeBytes("\r\n");
                obos.write(fileEntry.getValue());
                obos.writeBytes("\r\n");
            }
        }
        obos.writeBytes("--" + boundaryString + "--" + "\r\n");
        obos.writeBytes("\r\n");
        obos.flush();
        obos.close();
        InputStream ins = null;


        int code = conne.getResponseCode();
        try{
            if(code == 200){
                ins = conne.getInputStream();
            }else{
                ins = conne.getErrorStream();
            }
        }catch (SSLException e){
            e.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while((len = ins.read(buff)) != -1){
            baos.write(buff, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        ins.close();
        return bytes;
    }
    private static String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }
    private static String encode(String value) throws Exception{
        return URLEncoder.encode(value, "UTF-8");
    }

    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }
}
