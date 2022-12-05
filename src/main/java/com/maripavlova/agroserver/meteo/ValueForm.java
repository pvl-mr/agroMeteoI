package com.maripavlova.agroserver.meteo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValueForm {
    private String type;
    private List<Integer> values;
}
