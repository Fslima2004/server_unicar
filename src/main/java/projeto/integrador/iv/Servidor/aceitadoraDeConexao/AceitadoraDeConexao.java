package projeto.integrador.iv.Servidor.aceitadoraDeConexao;
import java.net.*;
import java.util.*;

import projeto.integrador.iv.Servidor.grupoDeCarona.GrupoDeCarona;
import projeto.integrador.iv.Servidor.supervisoraDeConexao.SupervisoraDeConexao;

public class AceitadoraDeConexao extends Thread
{
    private ServerSocket        pedido;

    // aqui vou precisar de um map de id : grupoCarona
    // para poder notificar os membros de uma carona
    private Map<String, GrupoDeCarona> gruposDeCarona;

    public AceitadoraDeConexao
    (String porta, Map<String, GrupoDeCarona> gruposDeCarona)
    throws Exception
    {
        if (porta==null)
            throw new Exception ("Porta ausente");

        try
        {
            this.pedido =
            new ServerSocket (Integer.parseInt(porta));
        }
        catch (Exception  erro)
        {
            throw new Exception ("Porta invalida");
        }

        if (gruposDeCarona==null)
            throw new Exception ("Usuarios ausentes");

        this.gruposDeCarona = gruposDeCarona;
    }

    public void run ()
    {
        for(;;)
        {
            Socket conexao=null;
            try
            {
                conexao = this.pedido.accept();
            }
            catch (Exception erro)
            {
                System.err.println('n'+"Não há pedido de conexão");
                continue; // pula para proximo loop, já que nao há pedido de conexao
            }

            SupervisoraDeConexao supervisoraDeConexao=null;
            try
            {
                supervisoraDeConexao =
                new SupervisoraDeConexao (conexao, gruposDeCarona);
            }
            catch (Exception erro)
            {
                System.err.println("Conexao invalida");
            } // sei que passei parametros corretos para o construtor

            
            System.out.println("Conexao estabelecida com: "+conexao.getInetAddress().getHostAddress());
            System.out.println("supervisoraDeConexao executando");
            supervisoraDeConexao.start();
        }
    }
}
