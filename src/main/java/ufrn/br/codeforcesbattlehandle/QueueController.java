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
import java.util.Objects;

@RestController
public class QueueController {
    private List<User> fila = Collections.synchronizedList(new ArrayList<User>());
    @Autowired
    private UserRepository myRepo;

    @Autowired
    private WebClient webClient;

    private QueueSink queueSink = new QueueSink();

    private QueueSink battleSink = new QueueSink();

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
        User u1,u2;
        u1 = myRepo.findByUsername(user1);


        u2 = myRepo.findByUsername(user2);

        if(u1==null)return Mono.just("Usuario "+user1+" não existe");
        if(u2==null)return Mono.just("Usuario "+user2+" não existe");

        String requestURL = "http://Problem-Handler/getProblem/" + calcRating(u1, u2).toString();

        Mono<String> s =  webClient.get()
                .uri(requestURL)
                .retrieve()
                .bodyToMono(String.class);

        return s;
    }

    @GetMapping("/checkTrue/{user1}&{problem}")
    public Mono<String> checkTrue(@PathVariable(value="user1") String user1, @PathVariable(value="problem") String problem) throws IOException {

        String requestURL = "http://External-Communication-Handler/checkTrue/" + user1+"&"+problem;

        Mono<String> s =  webClient.get()
                .uri(requestURL)
                .retrieve()
                .bodyToMono(String.class);
        //battleSink.produce(s.toString());
        return s;
    }




    }
