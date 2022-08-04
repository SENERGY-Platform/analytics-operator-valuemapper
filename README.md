# operator-valuemapper

Reads values and maps them to other values using a configuration map. Any values not found in the map will remain unchanged.

## Inputs

* value (any): Any value. Only primitive types and String are supported as input.

## Outputs

* value (any): Any value

## Configs
* rules (String): A JSON encoded rule list. No two rules may use the same from value. Example: [{"from": 12, "to": false}]
