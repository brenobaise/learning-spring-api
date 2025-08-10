package com.baisebreno.learning_spring_api.api.model;

import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * A representational model for a {@link Kitchen} in XML format.
 * This class is used by a controller to return a resource in form of XML.
 */
@JacksonXmlRootElement(localName = "kitchens")
@Data
public class KitchenXmlModel {

    @JsonProperty("kitchen") // changes the name of the xml tree to kitchen, customisation
    @JacksonXmlElementWrapper(useWrapping = false) // related to xml resources
    @NonNull // to force lombok to generate constructors with declared fields
    private List<Kitchen> kitchens;


}
