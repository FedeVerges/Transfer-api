package unsl.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "transfer", uniqueConstraints = {@UniqueConstraint(columnNames = {"nro_tranferencia"})})
public class Transfer {
    public static enum Status {
        PENDIENTE,
        PROCESADA,
        CANCELADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonProperty("id_cuenta_origen")
    private String id_cuenta_origen;

    @JsonProperty("id_cuenta_destino")
    private String id_cuenta_destino;

    @JsonProperty("monto")
    private float monto;

    @Enumerated(EnumType.STRING)
    private Status status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getId_cuenta_origen() {
        return id_cuenta_origen;
    }

    public void setId_cuenta_origen(String id_cuenta_origen) {
        this.id_cuenta_origen = id_cuenta_origen;
    }

    public String getId_cuenta_destino() {
        return id_cuenta_destino;
    }

    public void setId_cuenta_destino(String id_cuenta_destino) {
        this.id_cuenta_destino = id_cuenta_destino;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
