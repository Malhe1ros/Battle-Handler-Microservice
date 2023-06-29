package ufrn.br.codeforcesbattlehandle;

import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

public class QueueSink implements Consumer<FluxSink<String>> {
    private FluxSink<String> fluxSink;
    @Override
    public void accept(FluxSink<String> e) {
        this.fluxSink = e;
    }

    public void produce(String text){
        this.fluxSink.next(text);
    }
}
