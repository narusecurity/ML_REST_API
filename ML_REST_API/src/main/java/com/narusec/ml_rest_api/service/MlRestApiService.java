package com.narusec.ml_rest_api.service;


import com.narusec.ml_rest_api.entity.ModelInfoEntity;
import com.narusec.ml_rest_api.repository.ModelInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MlRestApiService {

    private String pythonUrl = "python";

    private ArrayList<Thread> threads = new ArrayList<>();

    private String[] pythonPM = new String[]{"-q ", "-m", "-s", "-e", "-p ", "-c ", "-r "};

    private List<String> command = new LinkedList<>();

    @Autowired
    private ModelInfoRepository modelInfoRepository;


    public void pyStart(Map<String, Object> parStrings) {
        try {
            log.debug("Start");
            command = new LinkedList<>();
//            command.add("ssh conse@192.168.103.70 python3 /app/ad/train.py");  // 테스트시 사용
            command.add("python3 /app/ad/train.py");
            for (int i = 0; i < pythonPM.length; i++) {
                if (!parStrings.get(pythonPM[i]).equals("\"\"") && !parStrings.get(pythonPM[i]).equals("")) {
                    command.add(pythonPM[i]);
                    command.add(parStrings.get(pythonPM[i]).toString());
                }
            }

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    excPython(command);
                }
            });
            thread.setName(parStrings.get("-q ").toString());
            thread.start();

            threads.add(thread);
            removeThread(threads);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error(e.getMessage());
        }

    }

    public void pyStop(String seq) {
        for (int i = 0; i < threads.size(); i++) {
            if (threads.get(i).getName().equals(seq)) {
                threads.get(i).interrupt();

                //중단 코드 작성 필요
                stopThread(seq);
                threads.remove(i);
            }
        }

    }

    private void excPython(List<String> command) {
        try {
            CommandLine commandLine = CommandLine.parse(command.get(0));
            for (int i = 1, n = command.size(); i < n; i++) {
                commandLine.addArgument(command.get(i), false);
            }

            DefaultExecutor executor = new DefaultExecutor();

            int result = executor.execute(commandLine);
            System.out.println("Start result: " + result);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void removeThread(ArrayList<Thread> threads) {
        for (int i = 0; i < threads.size(); i++) {
            System.out.println(i + "번째 쓰레드:" + threads.get(i).getState().toString() + " 상태 :" + threads.get(i).isAlive());
            if (threads.get(i).isAlive() == false) {
                threads.remove(i);
                i = 0;
            }
            System.out.println(i + "번째 쓰레드:" + threads.get(i).getState().toString() + " 상태 :" + threads.get(i).isAlive());
        }
    }

    private void stopThread(String model_id) {
        ModelInfoEntity modelInfoEntity = modelInfoRepository.findBySeq(Integer.valueOf(model_id));
        modelInfoEntity.setStatus("중단");
        modelInfoRepository.save(modelInfoEntity);
    }

    public String pythonResponse(String seq, String data) throws Exception{
        java.io.ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String pythonCommand = null;
        CommandLine commandLine = null;
        if (data.equals("")) {
            pythonCommand = "python3 /app/ad/api/get_model_data.py -m " + Integer.valueOf(seq);
            commandLine = CommandLine.parse(pythonCommand);
        } else {
            pythonCommand = "python3 /app/ad/api/TholdChange.py -seq " + Integer.valueOf(seq) + " -data ";
            commandLine = CommandLine.parse(pythonCommand);
            commandLine.addArgument(data, false);
        }
        System.out.println(commandLine);
        DefaultExecutor exec = new DefaultExecutor();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        exec.setStreamHandler(streamHandler);

        exec.execute(commandLine);
        return outputStream.toString();
    }
}
