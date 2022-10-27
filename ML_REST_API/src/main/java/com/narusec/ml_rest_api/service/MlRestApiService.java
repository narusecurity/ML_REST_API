package com.narusec.ml_rest_api.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
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

    public void pyStart(String test){
        try{
            log.debug("Start");
            String[] command = new String[2];
            command[0] = pythonUrl;
            command[1] = "C:\\test\\tt.py";
/*            command[2] = "10";
            command[3] = "20";*/

            Thread thread = new Thread(new Runnable()  {
                @Override
                public void run()  {
                    excPython(command);
                }
            });
            thread.setName(test);
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
            for (int i = 1, n = command.length; i < n; i++) {
                commandLine.addArgument(command[i]);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
            DefaultExecutor executor = new DefaultExecutor();
            executor.setStreamHandler(pumpStreamHandler);
            executor.execute(commandLine);
 /*           int result = executor.execute(commandLine);
            System.out.println("result: " + result);
            System.out.println("output: " + outputStream.toString());
*/
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }


}
