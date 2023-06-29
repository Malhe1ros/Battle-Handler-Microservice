package ufrn.br.codeforcesbattlehandle;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PrintQueuePubSubController {

//    private final RedisTemplate<String, String> redisTemplate;
//
//    private final List<String> messages;
//
//    @Autowired
//    public PrintQueuePubSubController(RedisTemplate<String, String> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//        this.messages = new ArrayList<>();
//
//        // Subscribe to the Redis channel
//        redisTemplate.getConnectionFactory().getConnection().subscribe(this, "joinqueue_channel".getBytes());
//    }
//
//    @GetMapping("/printQueuePubSub")
//    public List<String> printQueue() {
//        return messages;
//    }
//
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        String data = new String(message.getBody());
//
//        // Add the message to the list
//        messages.add(data);
//
//        // Process the message or take any other desired actions
//    }
}
