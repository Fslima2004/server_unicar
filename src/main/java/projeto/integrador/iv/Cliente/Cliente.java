package projeto.integrador.iv.Cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import projeto.integrador.iv.Servidor.dadosUsuario.Usuario;
import projeto.integrador.iv.Servidor.grupoDeCarona.GrupoCarona;
import projeto.integrador.iv.Servidor.parceiro.Parceiro;
import projeto.integrador.iv.Servidor.pedidos.PedidoCriarGrupoDeCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoEntrarNoGrupoDeCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoMeuGrupoCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoSairDoGrupoDeCarona;
import projeto.integrador.iv.Servidor.pedidos.PedidoTodosGruposDisponiveis;

public class Cliente {
	public static final String HOST_PADRAO = "localhost";
	public static final int PORTA_PADRAO = 3000;

	public static void main(String[] args) {
		if (args.length > 2) {
			System.err.println("Uso esperado: java Cliente [HOST [PORTA]]\n");
			return;
		}

		Socket conexao = null;
		try {
			String host = Cliente.HOST_PADRAO;
			int porta = Cliente.PORTA_PADRAO;

			if (args.length > 0)
				host = args[0];

			if (args.length == 2)
				porta = Integer.parseInt(args[1]);

			conexao = new Socket(host, porta);
		} catch (Exception erro) {
			System.err.println("Indique o servidor e a porta corretos!\n");
			return;
		}

		PrintWriter transmissor = null;
		try {
			transmissor = new PrintWriter(conexao.getOutputStream(), true);
		} catch (Exception erro) {
			System.err.println("Indique o servidor e a porta corretos!\n");
			return;
		}

		BufferedReader receptor = null;
		try {
			receptor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
		} catch (Exception erro) {
			System.err.println("Indique o servidor e a porta corretos!\n");
			return;
		}

		Parceiro servidor = null;
		try {
			servidor = new Parceiro(conexao, receptor, transmissor);
		} catch (Exception erro) {
			System.err.println("Indique o servidor e a porta corretos!\n");
			return;
		}

		// TratadoraDeComunicadoDeDesligamento tratadoraDeComunicadoDeDesligamento =
		// null;
		// try {
		// tratadoraDeComunicadoDeDesligamento = new
		// TratadoraDeComunicadoDeDesligamento(servidor);
		// } catch (Exception erro) {
		// } // sei que servidor foi instanciado

		// // a thread tratadoraDeComunicadoDeDesligamento vai tratar
		// // os comunicados de desligamento que chegam do servidor
		// tratadoraDeComunicadoDeDesligamento.start();

		TratadorDeComunicados tratadorDeComunicados = null;
		try {
			tratadorDeComunicados = new TratadorDeComunicados(servidor);
		} catch (Exception erro) {
		} // sei que servidor foi instanciado

		tratadorDeComunicados.start();

		char opcao = ' ';

		do {

			System.out.print("Sua opcao ([E]ntrar, [S]air, [C]riar, [T]erminar, [M]eu grupo, [Z]Todas caronas)? ");

			try {
				opcao = Character.toUpperCase(Teclado.getUmChar());
			} catch (Exception erro) {
				System.err.println("Opcao invalida!\n");

			}

			if ("ESCTMZ".indexOf(opcao) == -1) {
				System.err.println("Opcao invalida!\n");

			}

			try {

				switch (opcao) {
					case 'E':
						System.out.print("Qual o id do grupo em que deseja ingressar? ");
						String idGrupo = Teclado.getUmString();
						System.out.println();

						System.out.print("Qual a parada? ");
						String parada = Teclado.getUmString();
						System.out.println();

						Usuario usuario = obterUsuario();

						servidor.receba(new PedidoEntrarNoGrupoDeCarona(idGrupo, parada, usuario));
						break;
					case 'S':
						servidor.receba(new PedidoSairDoGrupoDeCarona());
						break;
					case 'C':
						GrupoCarona grupoCarona = obterCarona();
						servidor.receba(new PedidoCriarGrupoDeCarona(grupoCarona));
						break;
					case 'M':
						System.out.print("Qual o id do usuario? ");
						String idUsuario = Teclado.getUmString();
						System.out.println();

						// categoria: motorista ou passageiro
						System.out.print("Você é [M]otorista ou [P]assageiro? ");
						char categoria = Teclado.getUmChar();
						System.out.println();

						String categoriaString = "";

						if (categoria == 'M') {
							categoriaString = "motorista";
						} else if (categoria == 'P') {
							categoriaString = "passageiro";
						} else {
							System.out.println("Categoria inválida!");
							break;
						}

						servidor.receba(new PedidoMeuGrupoCarona(idUsuario, categoriaString));
						break;
					case 'Z':
						servidor.receba(new PedidoTodosGruposDisponiveis());
						break;
				}
			} catch (Exception erro) {
				System.err.println("Erro de comunicacao com o servidor;");
				System.err.println("Tente novamente!");
				System.err.println("Caso o erro persista, termine o programa");
				System.err.println("e volte a tentar mais tarde!\n");
			}

		} while (opcao != 'T');

		try {
			servidor.receba(new PedidoSairDoGrupoDeCarona());
		} catch (Exception erro) {
		}

		System.out.println("Obrigado por usar este programa!");
		System.exit(0);
	}

	public static Usuario obterUsuario() {
		System.out.print("Seu id de usuario? ");
		String id = Teclado.getUmString();
		System.out.println();

		System.out.print("Seu nome? ");
		String nome = Teclado.getUmString();
		System.out.println();

		System.out.print("Seu contato? ");
		String contato = Teclado.getUmString();
		System.out.println();

		return new Usuario(id, nome, contato);
	}

	public static GrupoCarona obterCarona() {
		System.out.print("ID da carona: ");
		String idCarona = Teclado.getUmString();
		System.out.println();

		System.out.println("Informações do motorista:");
		Usuario motorista = obterUsuario();

		System.out.print("Local de partida: ");
		String localPartida = Teclado.getUmString();
		System.out.println();

		System.out.print("Horário de saída: ");
		String horarioSaida = Teclado.getUmString();
		System.out.println();

		System.out.print("Preço: ");
		double preco = 0.0;
		try {
			preco = Teclado.getUmDouble();
		} catch (Exception e) {
			System.out.println("Preço inválido");
		}

		System.out.println();

		System.out.print("Vagas totais: ");
		int vagasTotais = 0;
		try {
			vagasTotais = Teclado.getUmInt();
		} catch (Exception e) {
			System.out.println("Vagas inválidas!");
		}

		System.out.println();

		return new GrupoCarona(idCarona, motorista, localPartida, horarioSaida, preco, vagasTotais);
	}
}
