package io.ashwin.kafka.wikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikimediaChangesProducer {

    private static final String BOOTSTRAP_SERVER = "127.0.0.1:9092";
    private static final String TOPIC = "wikimedia.recentchange";
    private static final String URL = "https://stream.wikimedia.org/v2/stream/recentchange";
    private static final Logger logger = LoggerFactory.getLogger(WikimediaChangesProducer.class.getSimpleName());

    public static void main(String[] args) throws InterruptedException {

        //Create producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVER);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //Set high throughput producer configs
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG,"20");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG,Integer.toString(32*1024));
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG,"snappy");

        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        //To handle events coming from stream and send them to producer
        EventHandler eventHandler=new WikimediaChangeHandler(producer,TOPIC);

        //Standard HTTP3 event source doc
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(URL));
        EventSource eventSource = builder.build();

        //Start producer in another thread
        eventSource.start();

        //Produce for 10 mins and block thread until then else main thread will end before sending.
        logger.info("Producing for 10 mins...");
        TimeUnit.MINUTES.sleep(10);
    }

}
