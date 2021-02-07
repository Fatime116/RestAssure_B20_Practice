package pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
//@ToString
@Data

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsAPI {

    private String description;
    private String author;
    private String title;
}
