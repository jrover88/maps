package com.p27.maps.controller;

import com.p27.maps.request.CreditTransferRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@AllArgsConstructor
public class MapsController {

    private final RabbitTemplate template; // AmqpTemplate template ?

    @RequestMapping("/emit/maps")
    @ResponseBody
    public String sendRequest(@RequestBody final CreditTransferRequest request) {
        log.info("LEG 1. Emit object from MAPS to PZ");
        template.convertAndSend("l1-maps-to-pz", request);
        log.info("Request from LEG1.MAPS: " + request);
        return "Request sent";
    }
}
