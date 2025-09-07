package com.baisebreno.learning_spring_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class ProductPhoto {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "product_id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    private String description;

    @Column(name = "content_type")
    private String contentType;

    private Long size;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Product product;

    public Long getRestaurantId(){
        if(getProduct() != null){
            return getProduct().getRestaurant().getId();
        }

        return  null;
    }

}
