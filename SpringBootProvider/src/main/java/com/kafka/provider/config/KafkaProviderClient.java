package com.kafka.provider.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Método producerConfig():
 *
 * Define y configura un mapa de propiedades para la conexión del productor Kafka.
 * Establece las direcciones del servidor de arranque y los serializadores de clave y valor.
 * Devuelve el mapa de propiedades configurado.
 * Método producerFactory():
 *
 * Crea y configura una fábrica de productores (ProducerFactory) utilizando la configuración obtenida de producerConfig().
 * Devuelve la fábrica configurada.
 * Método kafkaTemplate():
 *
 * Utiliza la fábrica de productores de producerFactory() para crear un KafkaTemplate configurado.
 * Proporciona una interfaz simplificada para enviar mensajes a los temas de Kafka.
 * Devuelve el KafkaTemplate configurado.
 * En resumen, la clase KafkaProviderClient configura y proporciona componentes esenciales para enviar mensajes a Kafka utilizando Spring Kafka.
 */

@Configuration
public class KafkaProviderClient {

    @Value("${spring.kafka.bootstrapServers}")  // Esta tomando el valor de la ip y el host que esta en el archivo "properties"
    private String bootstrapServers;

    public Map<String, Object> producerConfig(){   // la clave de este mapa va a se "String" y el valor va a ser "Object" para que se le pueda pasar cualquier cosa practicamente.
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);  // Estamos diciendo que el server que se va a usar va a ser el de la propiedad que creamos arriba "bootstrapServers"
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);  // Va a convertir nuestra clave string en bites para ser enviado por kafka.
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);  // Va a convertir nuestro valor object en bites para ser enviado por kafka.
        return properties;
    }

    @Bean                                                     // Al tener la annotation de bean Spring puede inyectarlo por paramétro en cualquier lado
    public ProducerFactory<String, String> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * En la anotación @Bean, como se utiliza en el segundo método kafkaTemplate(),
     * los parámetros de los métodos que representan otros beans administrados por
     * Spring se pasan automáticamente al momento de invocar el método. No es necesario que escribas
     * producerFactory() en el parámetro. Spring se encarga de identificar que hay un método
     * producerFactory() anotado con @Bean y lo llama para obtener la instancia del bean.
     * Luego, esa instancia se pasa como argumento al método kafkaTemplate().
     *
     * El parámetro producerFactory se llena automáticamente con la instancia del bean creado por el
     * método producerFactory() (el primer método), gracias a la magia de la inyección de dependencias
     * de Spring.
     *
     * En resumen, no necesitas escribir producerFactory() en el parámetro del método kafkaTemplate().
     * Spring maneja esa inyección automáticamente basándose en los nombres de los métodos anotados con
     * @Bean.
     */
}