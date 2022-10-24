package com.mitocode.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileId;

    @Column(length = 50, nullable = false)
    private String fileName;

    @Column(length = 20, nullable = false)
    private String fileType;

    @Column(name = "content", nullable = false)
    private byte[] value;
}
