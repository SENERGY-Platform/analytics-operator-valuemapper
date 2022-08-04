package org.infai.ses.senergy.operators.valuemapper;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rule {
    @JsonProperty("from")
    public Object from;

    @JsonProperty("to")
    public Object to;
}
