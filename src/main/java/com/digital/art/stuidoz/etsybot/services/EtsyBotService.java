package com.digital.art.stuidoz.etsybot.services;

import com.digital.art.stuidoz.etsybot.models.Listing;
import com.digital.art.stuidoz.etsybot.models.ProxyHost;
import com.digital.art.stuidoz.etsybot.services.proxy.ProxyHostsWorker;
import com.digital.art.stuidoz.etsybot.services.tasks.AddingToCartTask;
import com.digital.art.stuidoz.etsybot.services.tasks.FoundBySearchTask;
import com.digital.art.stuidoz.etsybot.services.tasks.Task;
import com.digital.art.stuidoz.etsybot.services.tasks.TaskType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Service
public class EtsyBotService implements InitializingBean {

    private final ExecutorService taskExecutor;
    private final ProxyHostsWorker proxyHostsWorker;
    private Map<TaskType, Task> tasks;
    private ApplicationContext context;

    @Autowired
    public EtsyBotService(
            ExecutorService taskExecutor,
            ProxyHostsWorker proxyHostsWorker,
            Map<TaskType, Task> tasks,
            ApplicationContext context) {
        this.taskExecutor = taskExecutor;
        this.proxyHostsWorker = proxyHostsWorker;
        this.tasks = tasks;
        this.context = context;
    }

    public void launch(List<Listing> listings){
        if(listings == null || listings.size() == 0)
            throw new IllegalArgumentException("There are no tasks should be performed");

        List<ProxyHost> hosts = proxyHostsWorker.getRemoteHosts();

        if(hosts == null || hosts.size() == 0)
            throw new IllegalArgumentException("There are no hosts to connect with");

        hosts.forEach(host -> {
            listings.forEach(listing -> {
                taskExecutor.execute(() -> {
                    System.out.println(Thread.currentThread() + "  " + host.toString());
                    HTTPClient client = context.getBean(HTTPClient.class, host, "HTTP");
                    tasks.get(listing.getType()).perform(listing, client, "");
                });
            });
        });
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        tasks.put(TaskType.SEARCHING, context.getBean(FoundBySearchTask.class));
        tasks.put(TaskType.CARTING, context.getBean(AddingToCartTask.class));

        Task task = context.getBean(FoundBySearchTask.class);
        task.connectWith(context.getBean(AddingToCartTask.class));

        tasks.put(TaskType.SEARCHING_CARTING, task);
    }
}
