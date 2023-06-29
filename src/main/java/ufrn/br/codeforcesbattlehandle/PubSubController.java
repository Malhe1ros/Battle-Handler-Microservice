package ufrn.br.codeforcesbattlehandle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class PubSubController {

    private final ReactiveStringRedisTemplate reactiveRedisTemplate;
    private final ReactiveRedisMessageListenerContainer redisMessageListenerContainer;
    private final Topic joinQueueTopic;

    @Autowired
    public PubSubController(ReactiveStringRedisTemplate reactiveRedisTemplate,
                           ReactiveRedisMessageListenerContainer redisMessageListenerContainer) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.joinQueueTopic = new ChannelTopic("joinqueue_channel");
    }

    @GetMapping("/joinQueuePubSub/{username}")
    public Mono<String> joinQueue(@PathVariable String username) {
        String message = "User " + username + " joined the queue.";

        // Publish the message to a Redis channel
        return reactiveRedisTemplate.convertAndSend(joinQueueTopic.getTopic(), message)
                .map(count -> "Message published successfully");
    }

    @GetMapping("/printQueuePubSub")
    public Flux<String> printQueue() {
        // Subscribe to the Redis channel using ReactiveRedisTemplate
        return reactiveRedisTemplate.listenTo(PatternTopic.of(joinQueueTopic.getTopic()))
                .map(message -> new String(message.getMessage()))
                .map(message -> (message+"<br/>"));

    }
}
