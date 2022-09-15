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

import org.infai.ses.senergy.exceptions.NoValueException;
import org.infai.ses.senergy.operators.BaseOperator;
import org.infai.ses.senergy.operators.FlexInput;
import org.infai.ses.senergy.operators.Message;

import java.text.ParseException;
import java.util.Map;


public class Valuemapper extends BaseOperator {

    private final Map translation;
    private final IntervalRule[] intervalRules;


    public Valuemapper(Map translation, IntervalRule[] intervalRules) {
        this.translation = translation;
        this.intervalRules = intervalRules;
    }

    @Override
    public void run(Message message) {
        FlexInput valueInput = message.getFlexInput("value");
        try {
            final Object value = valueInput.getValue(Object.class);
            Object output = null;
            if (translation.containsKey(value)) {
                output = translation.get(value);
            } else {
                boolean matched = false;
                for (IntervalRule intervalRule : intervalRules) {
                    if (intervalRule.triggers(value)) {
                        output = intervalRule.to;
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    output = value;
                }
            }
            message.output("value", output);
        } catch (NoValueException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message configMessage(Message message) {
        message.addFlexInput("value");
        return message;
    }
}
