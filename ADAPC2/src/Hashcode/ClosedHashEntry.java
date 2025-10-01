package Hashcode;

public class ClosedHashEntry {
    String tarjeta;
    String usuario;

    public ClosedHashEntry(String tarjeta, String usuario) {
        this.tarjeta = tarjeta;
        this.usuario = usuario;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public String getUsuario() {
        return usuario;
    }
}
