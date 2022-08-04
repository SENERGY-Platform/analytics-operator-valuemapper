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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.infai.ses.senergy.operators.Config;
import org.infai.ses.senergy.operators.Stream;

import java.util.HashMap;

public class Operator {

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Rule[]> typeRef = new TypeReference<>() {
        };
        Rule[] rules = mapper.readValue(new Config().getConfigValue("rules", "[]"), typeRef);
        HashMap<Object, Object> map = new HashMap<>();
        for (Rule r : rules) {
            map.put(r.from, r.to);
        }

        Stream stream = new Stream();
        stream.start(new Valuemapper(map));
    }
}
