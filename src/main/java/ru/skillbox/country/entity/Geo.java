package ru.skillbox.country.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class Geo implements Comparable<Geo> {
    private Long id;
    @JsonProperty("parent_id")
    private Long parentId;
    private String name;
    private List<Geo> areas;

    @Override
    public int compareTo(Geo o) {
        return name.compareTo(o.name);
    }
}
