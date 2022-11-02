package com.narusec.ml_rest_api;

import com.narusec.ml_rest_api.entity.ModelInfoEntity;
import com.narusec.ml_rest_api.repository.ModelInfoRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MlRestApiApplicationTests {

    @Autowired
    private ModelInfoRepository modelInfoRepository;
    @Test
    void contextLoads() {
    }


    @Disabled
    @Test
    void testDB(){
        ModelInfoEntity modelInfoEntity = modelInfoRepository.findBySeq(178);
        System.out.println(modelInfoEntity.toString());
    }

}
