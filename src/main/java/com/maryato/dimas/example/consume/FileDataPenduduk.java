package com.maryato.dimas.example.consume;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDataPenduduk {

    private String id;
    private String name;
    private String birtDate;
    private String address;
}
