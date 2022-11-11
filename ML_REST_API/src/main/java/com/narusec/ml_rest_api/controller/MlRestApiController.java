package com.narusec.ml_rest_api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.narusec.ml_rest_api.service.MlRestApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public void executor(@RequestBody Map<String, Object> map)throws InterruptedException{
        log.debug("start");
        String [] parameter = new String[7];
        parameter[0] = map.get("seq").toString();
        parameter[1] = map.get("m").toString();
        parameter[2] = map.get("s").toString();
        parameter[3] = map.get("e").toString();
        parameter[4] = map.get("p").toString();
        parameter[5] = map.get("c").toString();
        parameter[6] = map.get("r").toString();

        mlRestApiService.pyStart(parameter);
        System.out.println(parameter);
    }

    @RequestMapping(value = "MLRESTAPISTOP", method = RequestMethod.GET)
    public void stop(@RequestParam(value = "seq")String seq)throws InterruptedException{
        log.debug("start");
        mlRestApiService.pyStop(seq);
        System.out.println(seq);
    }

    @RequestMapping(value = "TEST", method = RequestMethod.GET)  //데이터 받는 부분
    public void executor()throws Exception{
        Map<String,Object> requestParam = new HashMap<>();
        requestParam.put("m","ST-MODEL1");  //모델이름
        requestParam.put("s","2022-10-22"); //2022-10-22  //시작 날짜
        requestParam.put("e","2022-10-27"); //끝나는 날짜
        requestParam.put("p","1");  //주기
/*
        requestParam.put("c",""); //전처리 관련 파라미터
        requestParam.put("r",""); //모델 관련 파라미터
*/
        requestParam.put("seq","1231");
                requestParam.put("c","{\"l7\":true}"); //전처리 관련 파라미터
        requestParam.put("r","{\"bandwith\":2}"); //모델 관련 파라미터
        //이름 추가 필요
        String json = new ObjectMapper().writeValueAsString(requestParam);
        sendREST("http://localhost:8088/MLRESTAPISTART/",json);
    }

    public static void sendREST(String sendUrl, String jsonValue) throws IllegalStateException {  //json 형식으로 변환 하여 POST로 데이터 날리는 부분
        try{
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
            if(conn.getResponseCode() == 200){
                log.debug("REST API SUCCESS");
            }else{
                log.debug("REST API FAIL");
            }
            conn.disconnect();
            log.debug("REST API End");
        }catch(Exception e){
            log.error(e.getMessage(), e); e.printStackTrace();
        }
    }
}
