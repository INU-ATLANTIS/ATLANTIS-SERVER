package com.atl.map.entity;

import com.atl.map.entity.primaryKey.FavoritePk;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="favortie")
@Table(name="favorite")
@IdClass(FavoritePk.class)
public class FavoriteEntity {

    @Id
    private Integer userId;
    @Id
    private Integer postId;

    
}
