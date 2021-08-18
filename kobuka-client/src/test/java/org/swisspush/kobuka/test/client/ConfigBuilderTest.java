package org.swisspush.kobuka.test.client;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.swisspush.kobuka.client.BaseCommonClientConfigBuilder;
import org.swisspush.kobuka.client.BaseConsumerConfigBuilder;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ConfigBuilderTest {

    /**
     * This illustrates how to reuse parts of configuration.
     */
    @Test
    public void testCopy() {

        BaseConsumerConfigBuilder<?> original =
                BaseConsumerConfigBuilder.create()
                        .bootstrapServers("localhost:9092,otherhost:9092")
                        .keyDeserializer(StringDeserializer.class)
                        .valueDeserializer(StringDeserializer.class);

        ConsumerConfig config =
                original
                        .map(BaseConsumerConfigBuilder::create)
                        .autoCommitIntervalMs(1234)
                        .build();

        assertEquals(Arrays.asList("localhost:9092", "otherhost:9092"),
                config.getList(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals(1234, config.getInt(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG));
        assertNotEquals(1234, original.build().getInt(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG));
    }

    /**
     * This illustrates how to reuse common configuration properties.
     */
    @Test
    public void testInheritance() {

        BaseCommonClientConfigBuilder<?> commonConfigBuilder =
                BaseCommonClientConfigBuilder.create()
                        .bootstrapServers("localhost:9092,otherhost:9092");

        ConsumerConfig config =
                commonConfigBuilder
                        .map(BaseConsumerConfigBuilder::create)
                        .keyDeserializer(StringDeserializer.class)
                        .valueDeserializer(StringDeserializer.class)
                        .build();

        assertEquals(Arrays.asList("localhost:9092", "otherhost:9092"),
                config.getList(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
    }

    /**
     * List and Password types have also pure-string setters.
     */
    @Test
    public void testStringOverload() {
        ConsumerConfig conf1 =
                BaseConsumerConfigBuilder
                        .create()
                        .bootstrapServers("localhost:9092,otherhost:9092")
                        .keyDeserializer(StringDeserializer.class)
                        .valueDeserializer(StringDeserializer.class)
                        .build();

        ConsumerConfig conf2 =
                BaseConsumerConfigBuilder
                        .create()
                        .bootstrapServers(Arrays.asList("localhost:9092", "otherhost:9092"))
                        .keyDeserializer(StringDeserializer.class)
                        .valueDeserializer(StringDeserializer.class)
                        .build();

        assertEquals(
                conf1.getList(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG),
                conf2.getList(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
    }
}