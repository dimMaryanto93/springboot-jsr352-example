package com.maryato.dimas.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Penduduk {

    private String nik;
    private String namaLengkap;
    private String tanggalLahir;
}
