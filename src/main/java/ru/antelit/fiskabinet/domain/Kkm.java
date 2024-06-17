package ru.antelit.fiskabinet.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "Kkm")
@Table(schema = "equip", name = "kkm")
public class Kkm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "inner_name")
    private String innerName;

    @OneToOne()
    @JoinColumn(name = "model_id")
    private KkmModel kkmModel;

    @Column(name = "serial_number")
    private String serialNumber;

//TODO Добавить регистрационный номер
//    @Column(name = "reg_num")
//    private String regNumber;

    @Column(name = "fn_number")
    private String fnNumber;

    @Column(name = "fn_end")
    private LocalDate fnEnd;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "tradepoint_id")
    private Tradepoint tradepoint;

    @ManyToOne
    @JoinColumn(name = "ofd_id")
    private Vendor.OfdProvider ofdProvider;

    @Column(name = "ofd_sub_end")
    private LocalDate ofdSubEnd;
}
