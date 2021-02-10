package com.digital.art.stuidoz.etsybot.services;

import com.digital.art.stuidoz.etsybot.models.Listing;
import com.digital.art.stuidoz.etsybot.services.proxy.ProxyHostsWorker;
import com.digital.art.stuidoz.etsybot.services.tasks.AddingToCartTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EtsyBotService {

    private final TaskExecutor taskExecutor;
    private final ProxyHostsWorker proxyHostsWorker;

    @Autowired
    public EtsyBotService(TaskExecutor taskExecutor, ProxyHostsWorker proxyHostsWorker) {
        this.taskExecutor = taskExecutor;
        this.proxyHostsWorker = proxyHostsWorker;
    }

    public void performTask(List<Listing> listings){
        if(listings == null || listings.size() == 0)
            throw new IllegalArgumentException("There are not tasks should be performed");

        Map<String, Integer> hosts = proxyHostsWorker.getRemoteHosts();
        listings.forEach(listing -> taskExecutor.execute(new AddingToCartTask(listing, hosts)));
    }
}
