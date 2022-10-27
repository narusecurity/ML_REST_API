package com.narusec.ml_rest_api.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
@Slf4j
public class MlRestApiService {

    private String pythonUrl = "python";

    private ArrayList<Thread> threads = new ArrayList<>();


    MlRestApiService(){

    }

    public void pyStart(String parStrings){
        try{
            log.debug("Start");
            String[] command = new String[3];
            command[0] = "ssh conse@192.168.103.70 python3 /app/ad/train.py -m 'ST-MODEL1' -s '2022-10-22' -e '2022-10-24' -p 1 -c '{\"l7\":true}' -r '{\"bandwith\":2}'";
            /*JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse()*/
            /*
            String[] command = new String[8];
            command[0] = "ssh conse@192.168.103.70 python3 ";
            command[1] = "-m " + parStrings[0];
            command[2] = "-s " + parStrings[1];
            command[3] = "-e " + parStrings[2];
            command[4] = "-p " + parStrings[3];
            command[5] = "-c " + parStrings[4];
            command[6] = "-r " + parStrings[5];
*/

            /*            String[] command = new String[8];
            command[0] = pythonUrl;
            command[1] = "-m " + parStrings[0];
            command[2] = "-s " + parStrings[1];
            command[3] = "-e " + parStrings[2];
            command[4] = "-p " + parStrings[3];
            command[5] = "-c " + parStrings[4];
            command[6] = "-r " + parStrings[5];*/
            Thread thread = new Thread(new Runnable()  {
                @Override
                public void run()  {
                    excPython(command);
                 }
            });
            thread.setName(parStrings);
            thread.start();
            threads.add(thread);
        }catch (Exception e){
            System.out.println(e.getMessage());
            log.error(e.getMessage());
        }

    }

    public void pyStop(String test){
        for (int i = 0 ; i<threads.size();i++){
            if(threads.get(i).getName().equals(test)){
                threads.get(i).interrupt();
            }
        }
    }

    private void excPython(String[] command) {
        try {
            CommandLine commandLine = CommandLine.parse(command[0]);
/*            for (int i = 1, n = command.length; i < n; i++) {
                commandLine.addArgument(command[i]);
            }*/

            DefaultExecutor executor = new DefaultExecutor();

            int result = executor.execute(commandLine);
            System.out.println("Start result: " + result);

        }catch (Exception e){
            log.error(e.getMessage());
        }
    }


}
