package com.herringbone.backpropagationnetwork;

import com.herringbone.backpropagationnetwork.model.Network;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/networks")
public class NetworkController {

    private final NetworkRepository networkRepository;


    public NetworkController(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }

    @GetMapping
    public @ResponseBody
    Flux<Network> getAllNetworks() {
        return networkRepository.findAll();
    }

    @PostMapping
    public void loadNetwork(@RequestBody Network network) {

    }
}
