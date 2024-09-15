5. Grafana Configuration
Grafana will be used to visualize both metrics from Prometheus and logs from Loki.

Data Sources:

Prometheus Data Source
Name: Prometheus
URL: http://prometheus:9090
Loki Data Source
Name: Loki
URL: http://loki:3100
Provisioning Data Sources and Dashboards (Optional):

You can automate data source and dashboard provisioning by placing configuration files in the /etc/grafana/provisioning/ directory inside the container.
For simplicity, you can manually add data sources and import dashboards through the Grafana web UI.
6. Starting the Services
Run the following command in the directory containing your docker-compose.yml file:

bash
Copy code
docker-compose up -d
Check the Status:

bash
Copy code
docker-compose ps
7. Accessing the Services
Grafana: http://localhost:3000
Default username: admin
Default password: admin
Prometheus: http://localhost:9090
RabbitMQ Management Console: http://localhost:15672
8. Configuring Grafana
Add Prometheus Data Source:

Navigate to Configuration > Data Sources.
Click Add data source.
Select Prometheus.
Set the URL to http://prometheus:9090.
Click Save & Test.
Add Loki Data Source:

Navigate to Configuration > Data Sources.
Click Add data source.
Select Loki.
Set the URL to http://loki:3100.
Click Save & Test.
Import Dashboards:

Use existing community dashboards for Prometheus metrics.
Create custom dashboards for your specific metrics and logs.
9. Verifying Logs in Grafana
Go to Explore.
Select Loki as the data source.
Use queries like {job="fluent-bit"} to view logs.
10. Testing the Microservices and RabbitMQ
Send requests to your microservices to generate activity.
Verify that the microservices can communicate via RabbitMQ.
Check that metrics and logs are being collected and displayed in Grafana.
