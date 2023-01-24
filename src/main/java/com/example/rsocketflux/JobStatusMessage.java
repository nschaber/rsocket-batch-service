package com.example.rsocketflux;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobStatusMessage {

    private String data;

    private Long id;

}
