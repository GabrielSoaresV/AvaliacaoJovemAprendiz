package br.com.ong.avaliacao;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Jovem {
	String nome;
	String empresa;
	String emailEmpresa;
	LocalDateTime dataInicio;
	LocalDateTime dataFinal;

	public Jovem(String nome, String empresa, String emailEmpresa, LocalDateTime dataInicio) {
		this.nome = nome;
		this.empresa = empresa;
		this.emailEmpresa = emailEmpresa;
		this.dataInicio = dataInicio;
		this.dataFinal = dataInicio;
	}
}

public class Avaliacao30Segundos {
	private static List<Jovem> jovens = new ArrayList<>();

	public static void main(String[] args) {

		jovens.add(new Jovem("Lucas Silva", "Empresa_1 LTDA", "RH@Empresa_1gmail.com", LocalDateTime.now().minusMonths(7)));
		jovens.add(new Jovem("Ana Paula", "Empresa_2 LTDA", "RH@Empresa_2gmail.com", LocalDateTime.now().minusMonths(3)));
		jovens.add(new Jovem("Gabriel Soares", "Empresa_3 LTDA", "RH@Empresa_3gmail.com", LocalDateTime.now().minusMonths(6).minusDays(2)));

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		scheduler.scheduleAtFixedRate(() -> verificarAvaliacoes(), 0, 1, TimeUnit.DAYS);
	}

	private static void verificarAvaliacoes() {
		LocalDateTime agora = LocalDateTime.now();
		System.out.println("🔄 Verificação em: " + agora + "\n");

		for (Jovem jovem : jovens) {
			if (verificarSeisMeses(jovem.dataFinal, agora)) {
				System.out.println("⚠️ PENDENTE: " + jovem.nome);
				System.out.println("   Empresa: " + jovem.empresa);
				System.out.println("   E-mail: " + jovem.emailEmpresa);
				System.out.println("   Última avaliação: " + jovem.dataFinal);
				System.out.println("   Ação: Enviar e-mail solicitando nova avaliação.\n");

				jovem.dataFinal = agora;
			} else {
				long dias = Duration.between(jovem.dataFinal, agora).toDays();
				System.out.println("✅ CONCLUÍDO: " + jovem.nome);
				System.out.println("   Empresa: " + jovem.empresa);
				System.out.println("   Dias desde última avaliação: " + dias + " dias\n");
			}
		}
		System.out.println("⏳ Aguardando próxima verificação...\n");
	}

	private static boolean verificarSeisMeses(LocalDateTime dataUltimaAvaliacao, LocalDateTime agora) {
		long meses = ChronoUnit.MONTHS.between(dataUltimaAvaliacao, agora);
		return meses >= 6;
	}
}
