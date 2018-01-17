package mmazurkiewicz.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
public class Category {

    private String id;
    private String description;
    private Set<Recipe> recipes;
}
