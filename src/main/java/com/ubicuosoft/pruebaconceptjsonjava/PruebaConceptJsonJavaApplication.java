package com.ubicuosoft.pruebaconceptjsonjava;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pojos.Address;
import pojos.Coordinates;
import pojos.NeoNameAndSpeed;
import sourceData.SourceData;

import javax.sound.midi.MidiFileFormat;

@SpringBootApplication
public class PruebaConceptJsonJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PruebaConceptJsonJavaApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() throws JsonProcessingException {
        // jsonNode();
        treeModel();
        return null;
    }

    void treeModel() throws JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
        JsonNode neoJsonNode=mapper.readTree(SourceData.asString());
        //System.out.println(neoJsonNode);
        System.out.println("-------Total de asteroides-------");
        System.out.println(getNeoCount(neoJsonNode));

        System.out.println("-------Total de objetos cerca de la tierra-------");
        System.out.println(getPotentiallyHazardousAsteroidCount(neoJsonNode));

        System.out.println("-------Cual es el nombre y la velocidad del Neo mas rapido-------");
        System.out.println(getFastedNeo(neoJsonNode));

    }

    private NeoNameAndSpeed getFastedNeo(JsonNode neoJsonNode) {
        NeoNameAndSpeed fastestNeo=null;
        JsonNode nearEarthObject=neoJsonNode.path("near_earth_objects");
        for (JsonNode neoClosesApproacheDate: nearEarthObject){
            for (JsonNode neo: neoClosesApproacheDate){
                double speed=neo
                        .get("close_approach_data")
                        .get(0)
                        .get("relative_velocity")
                        .get("kilometers_per_second").asDouble();

                if (fastestNeo==null || speed>fastestNeo.speed){
                       fastestNeo=new NeoNameAndSpeed(speed,neo.get("name").asText());
                }
            }
        }
        return fastestNeo;
    }


    private int getPotentiallyHazardousAsteroidCount(JsonNode neoJsonNode) {
        int potentiallyHazardousAsteroidCount=0;
        JsonNode nearEarthObject=neoJsonNode.path("near_earth_objects");
        //JsonNode nearEarthObject=neoJsonNode.path("links");
        //JsonNode nearEarthObject=neoJsonNode.path("element_count");
        System.out.println(nearEarthObject);

        for (JsonNode neoClosestApproachDate: nearEarthObject){
            for (JsonNode neo: neoClosestApproachDate){
                if (neo.get("is_potentially_hazardous_asteroid").asBoolean()){
                    potentiallyHazardousAsteroidCount+=1;
                }
            }
        }

        return potentiallyHazardousAsteroidCount;
    }

    private int getNeoCount(JsonNode neoJsonNode) {
        return neoJsonNode.get("element_count").asInt();

    }

    void dataBinding(){}

    void pathQueries(){}

    void jsonNode() throws JsonProcessingException {
        String objetoJson="{\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":{\"street\":\"21 2nd Street\",\"city\":\"New York\",\"postalCode\":\"10021-3100\",\"coordinates\":{\"latitude\":40.7250387,\"longitude\":-73.9932568}}}";
        System.out.println(objetoJson);

        ObjectMapper mapper=new ObjectMapper();

        JsonNode nodo=mapper.readTree(objetoJson);

        //-------Obteniendo las coordenadas
        JsonNode coordinadasNodo=nodo.at("/address/coordinates");

        Coordinates coordinadas=mapper.treeToValue(coordinadasNodo, Coordinates.class);

        System.out.println(coordinadas);

        //-------Obteniendo el last name
        JsonNode lastNameNode=nodo.at("/lastName");
        System.out.println(lastNameNode.toString());

        //-------Obteniendo el address
        JsonNode addressNode=nodo.at("/address");

        Address direccion=mapper.treeToValue(addressNode, Address.class);

        System.out.println(direccion);
        System.out.println(direccion.getCoordinates());
        System.out.println(direccion.getCoordinates().getLatitude());
    }

}
