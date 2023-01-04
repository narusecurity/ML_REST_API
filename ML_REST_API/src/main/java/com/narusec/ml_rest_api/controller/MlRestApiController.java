package com.narusec.ml_rest_api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.narusec.ml_rest_api.service.MlRestApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class MlRestApiController {


    @Autowired
    MlRestApiService mlRestApiService;

    @RequestMapping(value = "MLRESTAPISTART", method = RequestMethod.POST)
    public void executor(@RequestBody Map<String, Object> map) throws Exception {
        System.out.println(map);
        log.debug("start");
/*
        String [] parameter = new String[7];

        parameter[0] = map.get("seq").toString();
        parameter[1] = map.get("m").toString();
        parameter[2] = map.get("s").toString();
        parameter[3] = map.get("e").toString();
        parameter[4] = map.get("p").toString();
*/

        Map<String, Object> jsonParam = new HashMap<>();
        jsonParam.put("-q ", map.get("seq").toString());
        jsonParam.put("-m", map.get("m").toString());
        jsonParam.put("-s", map.get("s").toString());
        jsonParam.put("-e", map.get("e").toString());
        jsonParam.put("-p ", map.get("p").toString());
        jsonParam.put("-c ", new ObjectMapper().writeValueAsString(map.get("c")));
        jsonParam.put("-r ", new ObjectMapper().writeValueAsString(map.get("r")));
/*
        parameter[5] = map.get("c").toString();
        parameter[6] = map.get("r").toString();
*/

        mlRestApiService.pyStart(jsonParam);
        System.out.println(jsonParam.toString());
    }

    @RequestMapping(value = "MLRESTAPISTOP", method = RequestMethod.POST)
    public void stop(@RequestBody Map<String, Object> map) throws InterruptedException {
        log.debug("start");
        mlRestApiService.pyStop(map.get("seq").toString());
        System.out.println(map.get("seq"));
    }

    @RequestMapping(value = "MLRESPONSE", method = RequestMethod.GET)
    @ResponseBody
    public String responseApi(@RequestParam(value = "seq") String seq) throws Exception {
        log.debug("responseApiStart");

        return mlRestApiService.pythonResponse(seq, "");
    }

    @RequestMapping(value = "TholdChange", method = RequestMethod.POST)
    @ResponseBody
    public String threadhold(@RequestBody Map<String, Object> map) throws Exception {
        log.debug("start");
//        System.out.println(map);
        return mlRestApiService.pythonResponse(map.get("seq").toString(), new ObjectMapper().writeValueAsString(map.get("data")));

//        mlRestApiService.pythonResponse(map.get("seq").toString(),new ObjectMapper().writeValueAsString(map.get("data")));

    }

    @RequestMapping(value = "STOPTEST", method = RequestMethod.GET)  //데이터 받는 부분
    public void stopTest(@RequestParam(value = "seq") String seq) throws Exception {
        Map<String, Object> requestParam = new HashMap<>();

        requestParam.put("seq", seq);  //모델이름

        String json = new ObjectMapper().writeValueAsString(requestParam);
        System.out.println(json);
        sendREST("http://localhost:8088/MLRESTAPISTOP/", json);
    }

    @RequestMapping(value = "TEST", method = RequestMethod.GET)  //데이터 받는 부분
    public void executor() throws Exception {
        Map<String, Object> requestParam = new HashMap<>();
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();

        String a = "l7";
        String b = "bandwith";

        map1.put(a, true);
        map2.put(b, 2);

        requestParam.put("m", "ST-MODEL1");  //모델이름
        requestParam.put("s", "2022-10-22"); //2022-10-22  //시작 날짜
        requestParam.put("e", "2022-10-27"); //끝나는 날짜
        requestParam.put("p", "");  //주기
        requestParam.put("seq", "1231231");
        //   requestParam.put("c",""); //전처리 관련 파라미터
        //   requestParam.put("r",""); //모델 관련 파라미터
        requestParam.put("c", map1); //전처리 관련 파라미터
        requestParam.put("r", map2); //모델 관련 파라미터
        //이름 추가 필요
        String json = new ObjectMapper().writeValueAsString(requestParam);
        System.out.println(json);
        sendREST("http://localhost:8088/MLRESTAPISTART/", json);
    }

    @RequestMapping(value = "TESTholdChange", method = RequestMethod.GET)  //데이터 받는 부분
    public void executorTest() throws Exception {
        Map<String, Object> requestParam = new HashMap<>();
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("192.168.102.101", 3);
//        map1.put("threshold","3");
        map1.put("threshold", map2);
        requestParam.put("seq", "221127");  //모델이름
        requestParam.put("data", "{\'threshold\': {\'192.168.102.101\': -3.0671114350578588}}"); //모델 관련 파라미터
//        requestParam.put("c",map1); //전처리 관련 파라미터
//        requestParam.put("r",map2); //모델 관련 파라미터
        //이름 추가 필요
        String json = new ObjectMapper().writeValueAsString(requestParam);
        System.out.println("testStart");
        sendREST("http://localhost:8088/TholdChange/", json);
    }

    public static void sendREST(String sendUrl, String jsonValue) throws IllegalStateException {  //json 형식으로 변환 하여 POST로 데이터 날리는 부분
        try {
            System.out.println("rrrrrrarrrstart");
            log.debug("REST API Start");
            URL url = new URL(sendUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            OutputStream os = conn.getOutputStream();
            os.write(jsonValue.getBytes("UTF-8"));
            os.flush();
            if (conn.getResponseCode() == 200) {
                log.debug("REST API SUCCESS");
            } else {
                log.debug("REST API FAIL");
            }
            conn.disconnect();
            log.debug("REST API End");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
