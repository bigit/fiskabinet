package ru.antelit.fiskabinet.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;

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

//TODO Добавить регитрационный номер
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
    private OfdProvider ofdProvider;

    @Column(name = "ofd_sub_end")
    private LocalDate ofdSubEnd;
}
