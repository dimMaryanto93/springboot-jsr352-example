package com.maryato.dimas.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataPenduduk {

    private String nik;
    private String namaIdentitas;
    private Date tanggalLahir;
}
