package com.narusec.ml_rest_api.service;


import com.narusec.ml_rest_api.entity.ModelInfoEntity;
import com.narusec.ml_rest_api.repository.ModelInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
@Slf4j
public class MlRestApiService {

    private String pythonUrl = "python";

    private ArrayList<Thread> threads = new ArrayList<>();

    private String[] pythonPM = new String[]{"-m ","-s ","-e ","-p ","-c ","-r "};

    private String[] command = new String[9];

    @Autowired
    private ModelInfoRepository modelInfoRepository;


    MlRestApiService(){

    }

    public void pyStart(String parStrings[]){
        try{
            log.debug("Start");
            command[0] = "ssh conse@192.168.103.70 python3 /app/ad/train.py";
            for(int i = 0 ; i<pythonPM.length;i++) {
                if (!parStrings[i].equals("")) {
                    command[i + 1] = pythonPM[i] + "\'" + parStrings[i] + "\'";
                }
            }

            Thread thread = new Thread(new Runnable()  {
                @Override
                public void run()  {
                    excPython(command);
                    while(true){

                    }
                 }
            });
            thread.setName(parStrings[1]);
            thread.start();

            threads.add(thread);
            removeThread(threads);

        }catch (Exception e){
            System.out.println(e.getMessage());
            log.error(e.getMessage());
        }

    }

    public void pyStop(String test){
        for (int i = 0 ; i<threads.size();i++){
            if(threads.get(i).getName().equals(test)){
                threads.get(i).interrupt();

                //중단 코드 작성 필요
                stopThread(test);
            }
        }

    }

    private void excPython(String[] command) {
        try {
            CommandLine commandLine = CommandLine.parse(command[0]);
            for (int i = 1, n = command.length; i < n; i++) {
                commandLine.addArgument(command[i],false);
            }

            DefaultExecutor executor = new DefaultExecutor();

            int result = executor.execute(commandLine);
            System.out.println("Start result: " + result);

        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    private void removeThread(ArrayList<Thread> threads){
        for(int i = 0 ; i <threads.size();i++) {
            System.out.println(i +"번째 쓰레드:"+threads.get(i).getState().toString()+ " 상태 :"+ threads.get(i).isAlive());
            if(threads.get(i).isAlive()==false){
                threads.remove(i);
                i=0;
            }
            System.out.println(i +"번째 쓰레드:"+threads.get(i).getState().toString()+ " 상태 :"+ threads.get(i).isAlive());
        }
    }

    private void stopThread(String model_id){
        ModelInfoEntity modelInfoEntity = modelInfoRepository.findByModelId(Integer.valueOf(model_id));
        modelInfoEntity.setStatus("중단");
        modelInfoRepository.save(modelInfoEntity);
    }
}
