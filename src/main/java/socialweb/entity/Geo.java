package socialweb.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class Geo {
    private Long id;
    @JsonProperty("parent_id")
    private Long parentId;
    private String name;
    private List<Geo> areas;
}
