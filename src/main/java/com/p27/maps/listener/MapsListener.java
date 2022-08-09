package com.p27.maps.listener;

import com.p27.maps.request.CreditTransferRequest;
import com.p27.maps.response.CreditTransferResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@EnableRabbit
@AllArgsConstructor
public class MapsListener {

    private AmqpTemplate template;

    @RabbitListener(queues = "${leg.two.pz.to.maps.listener}")
    public void handleInboundMessageFromPzLegTwo(final CreditTransferRequest request) {

        log.info("Message from LEG2.PZ: " + request);
        template.convertAndSend("l2-maps-to-rix", request);
        log.info("LEG 2. Emit object from MAPS to RIX");
    }

    @RabbitListener(queues = "${leg.three.rix.to.maps.listener}")
    public void handleInboundMessageFromRixLegThree(final CreditTransferResponse response) {
        log.info("Message from LEG3.RIX: " + response);
        template.convertAndSend("l3-maps-to-pz", response);
        log.info("LEG 3. Emit object from MAPS to PZ");
    }

    @RabbitListener(queues = "${leg.four.pz.to.maps.listener}")
    public void handleInboundMessageFromPzLegFour(final CreditTransferResponse response) {
        log.info("Message from LEG4.PZ: " + response);
    }
}
