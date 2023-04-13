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
import java.util.Date;

@Getter
@Setter
@Entity(name = "kkm")
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

    @Column(name = "reg_num")
    private String regNumber;

    @Column(name = "fn_number")
    private Long fnNumber;

    @Column(name = "fn_end")
    @Temporal(TemporalType.DATE)
    private Date fnEnd;

    @ManyToOne
    @JoinColumn(name = "tradepoint_id")
    private Tradepoint tradepoint;

    @ManyToOne
    @JoinColumn(name = "ofd_id")
    private OfdProvider ofdProvider;

}
