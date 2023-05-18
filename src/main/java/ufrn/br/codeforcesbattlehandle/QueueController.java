package ufrn.br.codeforcesbattlehandle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ufrn.br.codeforcesbattlehandle.configuration.ApplicationConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class QueueController {
    private List<User> fila = Collections.synchronizedList(new ArrayList<User>());
    @Autowired
    private UserRepository myRepo;

    @Autowired
    private WebClient webClient;

    private QueueSink queueSink = new QueueSink();

    Flux<String> queueFlux = Flux.create(queueSink).cache();

    @GetMapping("/joinQueue/{username}")
    public Mono<String> joinQueue(@PathVariable(value="username") String username) throws IOException {
        User ans = myRepo.findByUsername(username);
        if(ans!=null){
            queueSink.produce("Usuario: "+username+"<br/>");
            fila.add(ans);
            return Mono.just("Adicionado com sucesso");
        }
        else{
            return Mono.just("Esse usuario nao existe");
        }
    }
    @GetMapping("/printQueue")
    public Flux<String> printQueue() throws IOException {

        return queueFlux;
//        StringBuilder ans = new StringBuilder("");
//        for(User user : fila){
//            ans.append("<br>").append(user.getUsername());
//        }
//        ans.append("<br>");
//        if(ans.length()==4)return Mono.just("Não tem ninguém na fila");
//        return Mono.just(ans.toString());
    }
    Integer calcRating(User u1,User u2){
        int r1 = u1.getRating();
        int r2 = u2.getRating();
        int mid = (r1+r2)/2;
        mid/=100;
        mid*=100;
        assert mid%100==0;
        return Math.max(mid,800);
    }


    @GetMapping("/battle/{user1}&{user2}")
    public Mono<String> batalha(@PathVariable(value="user1") String user1, @PathVariable(value="user2") String user2) throws IOException {
        User u1 = myRepo.findByUsername(user1);
        User u2 = myRepo.findByUsername(user2);
        String requestURL = "/getProblem/" + calcRating(u1, u2).toString();

        return  webClient.get()
                .uri(requestURL)
                .retrieve()
                .bodyToMono(String.class);

    }


    }
