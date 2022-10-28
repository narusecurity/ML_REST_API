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

    @RequestMapping(value = "Start", method = RequestMethod.POST)
    public void executor(@RequestBody Map<String, Object> map)throws InterruptedException{
        log.debug("start");
        String [] parameter = new String[6];
        parameter[0] = map.get("m").toString();
        parameter[1] = map.get("s").toString();
        parameter[2] = map.get("e").toString();
        parameter[3] = map.get("p").toString();
        parameter[4] = map.get("c").toString();
        parameter[5] = map.get("r").toString();
        mlRestApiService.pyStart(parameter);
        System.out.println(parameter);
        System.out.println(parameter[0]);
    }

    @RequestMapping(value = "End", method = RequestMethod.GET)
    public void stop(@RequestParam(value = "test")String test)throws InterruptedException{
        log.debug("start");
        mlRestApiService.pyStop(test);
        System.out.println(test);
    }

    @RequestMapping(value = "TEST", method = RequestMethod.GET)
    public void executor(@RequestParam(value = "date")String date)throws Exception{
        log.debug("Date");
        Map<String,Object> requestParam = new HashMap<>();
        requestParam.put("m","ST-MODEL1");
        requestParam.put("s",date); //2022-10-22
        requestParam.put("e","2022-10-24");
        requestParam.put("p","1");
        requestParam.put("c","{\"l7\":true}");
        requestParam.put("r","{\"bandwith\":2}");
        String json = new ObjectMapper().writeValueAsString(requestParam);
        sendREST("http://localhost:8088/Start/",json);
    }

    public static void sendREST(String sendUrl, String jsonValue) throws IllegalStateException {
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
