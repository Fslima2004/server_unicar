package projeto.integrador.iv.Servidor.dadosUsuario;

import java.io.Serializable;

public class Usuario implements Serializable{
    private String id;

    public Usuario(String id) {
        this.id = id;
    }

    public Usuario() {
        this.id = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String novoId) {
        this.id = novoId;
    }

    @Override
    public String toString() {
        return "DadosUsuario{" + "id=" + id + '}';
    }
}
