package com.narusec.ml_rest_api.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "model_info")
public class ModelInfoEntity {

    @Id
    @Column(name = "SEQ")
    private Integer seq;

    @Column(name = "MODEL_TYPE")
    private String modelType;

    @Column(name = "SETTINGS")
    private String settings;

    @Column(name = "PATH")
    private String path;

    @Column(name = "PERIOD")
    private Integer period;

    @Column(name = "TRAIN_STARTTIME")
    private Timestamp train_starttime;

    @Column(name = "TRAIN_ENDTIME")
    private Timestamp train_endtime;

    @Column(name = "UPDATE_TIME")
    private Timestamp update_time;

    @Column(name = "IS_ACTIVE")
    private boolean is_active;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "NETWORK")
    private String network;

}
