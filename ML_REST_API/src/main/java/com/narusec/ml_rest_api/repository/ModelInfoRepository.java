package com.narusec.ml_rest_api.repository;

import com.narusec.ml_rest_api.entity.ModelInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelInfoRepository extends JpaRepository<ModelInfoEntity,Integer> {
    ModelInfoEntity findBySeq(Integer id); // 값 필요
}
