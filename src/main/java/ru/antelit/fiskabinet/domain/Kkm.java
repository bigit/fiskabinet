package ru.antelit.fiskabinet.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity(name = "kkm")
@Table(schema = "org", name = "kkm")
public class Kkm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "inner_name")
    private String innerName;

    @OneToOne
    private Model model;

    @Column(name = "fn_number")
    private Long fnNumber;

    @Column(name = "fn_end")
    @Temporal(TemporalType.DATE)
    private Date fnEnd;

    @ManyToOne
    @JoinColumn(name = "tradepoint_id")
    private Tradepoint tradepoint;

//    private Ofd ofd;
//    private Date ofdEnd;
}
