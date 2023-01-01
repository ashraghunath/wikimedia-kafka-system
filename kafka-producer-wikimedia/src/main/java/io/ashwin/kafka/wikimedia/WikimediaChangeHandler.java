package io.ashwin.kafka.wikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikimediaChangeHandler implements EventHandler {

    KafkaProducer<String,String> kafkaProducer;
    String topic;

    private final Logger logger = LoggerFactory.getLogger(WikimediaChangeHandler.class.getSimpleName());

    public WikimediaChangeHandler(KafkaProducer<String,String> kafkaProducer, String topic) {
        this.kafkaProducer=kafkaProducer;
        this.topic=topic;
    }

    @Override
    public void onOpen(){
        //nothing
    }

    @Override
    public void onClosed(){
        kafkaProducer.close();
    }

    @Override
    public void onMessage(String s, MessageEvent messageEvent) throws Exception {
        logger.info(messageEvent.getData());
        //async
        kafkaProducer.send(new ProducerRecord<>(topic,messageEvent.getData()));
    }

    @Override
    public void onComment(String s) {
        //nothing
    }

    @Override
    public void onError(Throwable throwable) {
        logger.error("Error in reading stream "+throwable);
    }
}
