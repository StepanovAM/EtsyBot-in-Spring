package com.digital.art.stuidoz.etsybot.controllers;

import com.digital.art.stuidoz.etsybot.models.Listing;
import com.digital.art.stuidoz.etsybot.services.EtsyBotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexController {

    private EtsyBotService bot;

    public IndexController(EtsyBotService bot) {
        this.bot = bot;
    }

    @GetMapping("/")
    public String ask(){
        bot.performTask(List.of(new Listing("867167949", List.of("seasonal sign", "halloween poster"))));
        return "Received";
    }
}
