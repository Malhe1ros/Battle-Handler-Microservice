package ufrn.br.codeforcesbattlehandle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class QueueController {
    private List<User> fila = Collections.synchronizedList(new ArrayList<User>());
    @Autowired
    private UserRepository myRepo;
    @GetMapping("/joinQueue/{username}")
    public String joinQueue(@PathVariable(value="username") String username) throws IOException {
        List<User> ans = myRepo.findByUsername(username);
        if(!ans.isEmpty()){
            fila.add(ans.get(0));
            return "Adicionado com sucesso";
        }
        else{
            return "Esse usuario nao existe";
        }
    }
    @GetMapping("/printQueue")
    public String printQueue() throws IOException {
        StringBuilder ans = new StringBuilder("");
        for(User user : fila){
            ans.append("<br>").append(user.getUsername());
        }
        ans.append("<br>");
        if(ans.length()==4)return "Não tem ninguém na fila";
        return ans.toString();
    }
}
