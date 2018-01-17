package mmazurkiewicz.domain;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Getter
@Setter
public class Notes {

    @Id
    private String id;
    //private Recipe recipe;
    private String recipeNotes;

}
