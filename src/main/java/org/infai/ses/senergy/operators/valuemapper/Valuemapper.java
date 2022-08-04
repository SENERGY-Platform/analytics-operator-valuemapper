/*
 * Copyright 2018 InfAI (CC SES)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.infai.ses.senergy.operators.valuemapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.infai.ses.senergy.exceptions.NoValueException;
import org.infai.ses.senergy.models.AnalyticsMessageModel;
import org.infai.ses.senergy.models.MessageModel;
import org.infai.ses.senergy.operators.BaseOperator;
import org.infai.ses.senergy.operators.FlexInput;
import org.infai.ses.senergy.operators.Helper;
import org.infai.ses.senergy.operators.Message;
import org.infai.ses.senergy.utils.StreamsConfigProvider;
import org.infai.ses.senergy.utils.TimeProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Valuemapper extends BaseOperator {

    private final Map translation;


    public Valuemapper(Map translation) {
        this.translation = translation;
    }

    @Override
    public void run(Message message) {
        FlexInput valueInput = message.getFlexInput("value");
        try {
            final Object value = valueInput.getValue(Object.class);
            final Object output;
            if (translation.containsKey(value)) {
                output = translation.get(value);
            } else {
                output = value;
            }
            message.output("value", output);
        } catch (NoValueException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message configMessage(Message message) {
        message.addFlexInput("value");
        return message;
    }
}
