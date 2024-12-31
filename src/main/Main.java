package main;
import model.Video;
import model.illegalArgumentException;
import repository.FileVideoRepository;
import service.VideoService;
import service.VideoServiceImpl;
import strategy.SearchStrategy;
import strategy.TitleSearchStrategy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;

public class Main {

    private static final Scanner leitor = new Scanner(System.in);
    private static final VideoService videoService = new VideoServiceImpl(new FileVideoRepository("videos.txt"));
    private static final SearchStrategy searchStrategy = new TitleSearchStrategy();

    public static void main(String[] args) {

        while (true) {
            exibirMenu();

            int opcao = lerInteiro();

            switch (opcao) {
                case 1 -> adicionarVideo();
                case 2 -> listarVideos();
                case 3 -> pesquisarVideo();
                case 4 -> editarVideo();
                case 5 -> excluirVideo();
                case 6 -> filtrarPorCategoria();
                case 7 -> ordenarPorData();
                case 8 -> gerarRelatorio();
                case 9 -> {
                    System.out.println("Saindo do sistema...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("\n--- Sistema de Gerenciamento de Vídeos ----");
        System.out.println("1-Adicionar vídeo");
        System.out.println("2-Listar vídeos");
        System.out.println("3-Pesquisar vídeo por título");
        System.out.println("4-Editar vídeo");
        System.out.println("5-Excluir vídeo");
        System.out.println("6-Filtrar vídeos por categoria");
        System.out.println("7-Ordenar vídeos por data de publicação");
        System.out.println("8-Relatório de estatísticas");
        System.out.println("9-Sair");
    }

    private static int lerInteiro() {
        while (true) {
            try {
                System.out.print("Escolha uma opção: ");
                return leitor.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                leitor.nextLine();
            }
        }
    }

    private static void adicionarVideo() {
        leitor.nextLine();
        System.out.print("Digite o título do vídeo: ");
        String titulo = leitor.nextLine();
        System.out.print("Digite a descrição do vídeo: ");
        String descricao = leitor.nextLine();
        System.out.print("Digite a duração do vídeo (em minutos): ");
        int duracao = leitor.nextInt();
        leitor.nextLine();
        System.out.print("Digite a categoria do vídeo: ");
        String categoria = leitor.nextLine();
        System.out.print("Digite a data de publicação (dd/MM/yyyy): ");
        String dataStr = leitor.nextLine();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dataPublicacao = sdf.parse(dataStr);
            Video video = new Video(titulo, descricao, duracao, categoria, dataPublicacao);
            videoService.addVideo(video);
            System.out.println("Vídeo adicionado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao adicionar vídeo.");
        }
    }

    private static void listarVideos() {
        String query = "";
        List<Video> videos = videoService.listVideos(query);
        for (Video video : videos) {
            System.out.println(video);
        }
    }

    private static void pesquisarVideo() {
        leitor.nextLine();

        String query = lerString("Digite o título para busca: ");
        List<Video> resultados = searchStrategy.search(videoService.listVideos(query), query);

        if (resultados.isEmpty()) {
            System.out.println("Nenhum vídeo encontrado para o título: " + query);
        } else {
            for (Video video : resultados) {
                System.out.println(video);
            }
        }

        // Validar se o título foi fornecido
        if (query.isEmpty()) {
            System.out.println("O título não pode estar vazio. Tente novamente.");
            return;
        }

        // Verificar se o serviço de vídeos está inicializado
        if (searchStrategy == null) {
            System.out.println("O serviço de vídeos não está disponível.");
            return;
        }

    }

    private static void editarVideo() {
        leitor.nextLine();
        System.out.print("Digite o título do vídeo que deseja editar: ");
        String titulo = leitor.nextLine();
        Video video = videoService.findVideoByTitle(titulo);

        if (video != null) {
            System.out.print("Digite o novo título (ou pressione Enter para manter): ");
            String novoTitulo = leitor.nextLine();

        if (!novoTitulo.isEmpty()) video.setTitulo(novoTitulo);

            System.out.print("Digite a nova descrição (ou pressione Enter para manter): ");
            String novaDescricao = leitor.nextLine();
            if (!novaDescricao.isEmpty()) video.setDescricao(novaDescricao);

            System.out.print("Digite a nova duração (ou pressione Enter para manter): ");
            String novaDuracao = leitor.nextLine();
            if (!novaDuracao.isEmpty()) video.setDuracao(Integer.parseInt(novaDuracao));

            System.out.print("Digite a nova categoria (ou pressione Enter para manter): ");
            String novaCategoria = leitor.nextLine();
            if (!novaCategoria.isEmpty()) video.setCategoria(novaCategoria);

            System.out.print("Digite a nova data de publicação (dd/MM/yyyy) (ou pressione Enter para manter): ");
            String novaData = leitor.nextLine();
            if (!novaData.isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    video.setDataPublicacao(sdf.parse(novaData));
                } catch (Exception e) {
                    System.out.println("Data inválida. Mantendo a data atual.");
                }
            }
            System.out.println("Vídeo editado com sucesso!");
        } else {
            System.out.println("Vídeo não encontrado.");
        }
    }

    private static void excluirVideo() {
        leitor.nextLine();
        System.out.print("Digite o título do vídeo que deseja excluir: ");
        String titulo1 = leitor.nextLine();
        if (videoService.removeVideoList(Video.fromString(titulo1))) {
            System.out.println("Vídeo removido com sucesso!");
        } else {
            System.out.println("Vídeo não encontrado.");
        }
    }

    private static void filtrarPorCategoria() {
        leitor.nextLine(); // Consumir quebra de linha
        System.out.print("Digite a categoria para filtrar: ");
        String categoria = leitor.nextLine();
        List<Video> videos = videoService.filterByCategory(categoria);
        for (Video video : videos) {
            System.out.println(video);
        }
    }

    private static void ordenarPorData() {
        List<Video> videos = videoService.sortByDate();
        for (Video video : videos) {
            System.out.println(video);
        }
    }

    private static void gerarRelatorio() {
        System.out.println("=== Relatório de Estatísticas ===");
        System.out.println("Número total de vídeos: " + videoService.getTotalVideos());
        System.out.println("Duração total de todos os vídeos: " + videoService.getTotalDuration() + " minutos");
        System.out.println("Quantidade de vídeos por categoria: " + videoService.getVideosByCategory());
    }

    private static Date lerDataValida(String mensagem) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // Garante que apenas datas válidas sejam aceitas
        while (true) {
            try {
                String dataStr = lerString(mensagem);
                return sdf.parse(dataStr);
            } catch (Exception e) {
                System.out.println("Data inválida. Por favor, insira uma data no formato dd/mm/yyyy");
            }
        }
    }
        private static String lerString (String mensagem){
            System.out.println(mensagem);
            return leitor.nextLine().trim();
        }

        private static String lerTitulo (String mensagem) throws illegalArgumentException {
            String titulo = lerString(mensagem);
            if (titulo.isEmpty()) {
                throw new illegalArgumentException("Preencha o título.");
            }
            return titulo;
        }
    }