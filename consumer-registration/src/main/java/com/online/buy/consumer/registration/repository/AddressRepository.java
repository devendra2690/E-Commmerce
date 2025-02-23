package com.online.buy.consumer.registration.repository;

import com.online.buy.consumer.registration.entity.AddressEntity;
import com.online.buy.consumer.registration.entity.ConsumerEntity;
import com.online.buy.consumer.registration.mapper.AddressMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity,Long> {
}
