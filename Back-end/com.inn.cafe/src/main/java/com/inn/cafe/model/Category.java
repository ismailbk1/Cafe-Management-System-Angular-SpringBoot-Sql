package com.inn.cafe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name="Category.getAllCategory" , query = "select c from Category c where c.id in (select p.category from Product p where p.status='true') ")


@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "category")
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;
 @Column(name="name")
   private String name;
}
