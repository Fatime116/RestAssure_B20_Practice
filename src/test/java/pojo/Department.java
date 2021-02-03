package pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

//@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
//@ToString
@Data
//All together now: A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and @Setter on all non-final fields, and @RequiredArgsConstructor!

@JsonIgnoreProperties(ignoreUnknown = true)
public class Department {
    //we can add the POJO with the minimal effort
//Lombok is  java library,that automatically plugs into your editor and build tools, spicing up your java.
//Never write another getter or equals method again, with one annotation your class has a fully featured builder, Automate your logging variables, and much more.
    private int department_id;
    private String department_name;
    private int manager_id;
    private int location_id;



}
