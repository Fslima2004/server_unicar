package projeto.integrador.iv.Servidor.comunicados;
import java.io.*;

import org.json.JSONObject;

public interface Comunicado extends Serializable
{
    JSONObject toJson();
    // Comunicado fromJson(JSONObject json);
}
