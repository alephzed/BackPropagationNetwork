package com.herringbone.backpropagationnetwork;

import com.herringbone.backpropagationnetwork.model.Network;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NetworkRepository extends ReactiveMongoRepository<Network, Long> {
}
