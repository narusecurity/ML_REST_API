package com.narusec.ml_rest_api.controller;


import com.narusec.ml_rest_api.service.MlRestApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MlRestApiController {


    @Autowired
    MlRestApiService mlRestApiService;

    @RequestMapping(value = "Start", method = RequestMethod.GET)
    public void executor(@RequestParam(value = "model")String model)throws InterruptedException{
        log.debug("start");
/*        String [] parameter = new String[6];
        mlRestApiService.pyStart(parameter);
        System.out.println(parameter);*/

        mlRestApiService.pyStart(model);
        System.out.println(model);
    }

    @RequestMapping(value = "End", method = RequestMethod.GET)
    public void stop(@RequestParam(value = "test")String test)throws InterruptedException{
        log.debug("start");
        mlRestApiService.pyStop(test);
        System.out.println(test);
    }
}
