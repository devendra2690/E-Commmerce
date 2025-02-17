package com.online.buy.consumer.registration.repository;

import com.online.buy.consumer.registration.entity.ConsumerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerRepository extends CrudRepository<ConsumerEntity,Long> {
}
