package com.digital.art.stuidoz.etsybot.controllers;

import com.digital.art.stuidoz.etsybot.models.Listing;
import com.digital.art.stuidoz.etsybot.services.EtsyBotService;
import com.digital.art.stuidoz.etsybot.services.tasks.TaskType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutorService;

@RestController
public class IndexController {

    private final EtsyBotService bot;
    private final ExecutorService taskExecutor;

    public IndexController(EtsyBotService bot, ExecutorService taskExecutor) {
        this.bot = bot;
        this.taskExecutor = taskExecutor;
    }

    @GetMapping("/")
    public String ask(){
        List<Listing> listings = List.of(
                new Listing("867167949", List.of("seasonal sign", "halloween poster"), TaskType.SEARCHING_CARTING));

        bot.launch(listings);
        return "Received";
    }

    @GetMapping("/executor")
    public String askThreadExecutorInfo(){
        return taskExecutor.toString();
    }
}
