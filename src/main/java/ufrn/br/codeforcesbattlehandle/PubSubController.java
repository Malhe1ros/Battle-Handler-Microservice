package ufrn.br.codeforcesbattlehandle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PubSubController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/joinQueuePubSub/{username}")
    public String joinQueue(@PathVariable String username) {
        String message = "User " + username + " joined the queue.";

        // Publish the message to a Redis channel
        redisTemplate.convertAndSend("joinqueue_channel", message);

        return "Message published successfully";
    }
}