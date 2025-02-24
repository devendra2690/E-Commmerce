package com.online.buy.registration.processor.repository;

import com.online.buy.registration.processor.entity.ConsumerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerRepository extends CrudRepository<ConsumerEntity,Long> {
}
