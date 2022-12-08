package com.maripavlova.agroserver.meteo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValueForm {
    private String type;
    private List<Float> values;

    public ValueForm(String type) {
        this.type = type;
        this.values = new ArrayList<>();
    }
}
