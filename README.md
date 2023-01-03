# wikimedia-kafka-system


## High throughput kafka pipeline to read from Wikimedia stream and index into ElasticSearch.

### Source information:
Wikimedia stream real-time URL : https://stream.wikimedia.org/v2/stream/recentchange

### Sink information:

- OpenSearch ( Open source fork of ElasticSearch )
- Console at http://localhost:5601/app/dev_tools#/console

### Container Images

|     Application                 |    Container                                  |
| ------------------------------- | --------------------------------------------- |
| opensearch                      | opensearchproject/opensearch:1.2.4            |
| opensearch-dashboards           | opensearchproject/opensearch-dashboards:1.2.0 |




