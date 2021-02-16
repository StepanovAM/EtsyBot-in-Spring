package com.digital.art.stuidoz.etsybot;

import static org.assertj.core.api.Assertions.assertThat;

import com.digital.art.stuidoz.etsybot.models.Listing;
import com.digital.art.stuidoz.etsybot.services.tasks.TaskType;
import com.digital.art.stuidoz.etsybot.services.EtsyBotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class EtsyBotServiceTests {

    @Autowired
    private EtsyBotService bot;

    @Test
    void etsyBot() {
        List<Listing> listings = List.of(
                new Listing("867167949", List.of("seasonal sign", "halloween poster"),
                TaskType.SEARCHING_CARTING));

        bot.launch(listings);
    }

}
