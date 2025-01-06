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
    private static final VideoService videoService = new VideoServiceImpl(new FileVideoRepository("videos.txt")) {
        @Override
        public void updateVideo(Video video) {

        }
    };
    private static final SearchStrategy searchStrategy = new TitleSearchStrategy();

    public static void main(String[] args) {

        while (true) {
            exibirMenu();

            int opcao = lerInteiro();

            switch (opcao) {
                case 1 -> adicionarVideo();
                case 2 -> listarVideos();
                case 3 -> pesquisarVideo();
                case 4 -> {
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
        System.out.println("4-Sair");
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
        if (query.isEmpty()) {
            System.out.println("O título não pode estar vazio. Tente novamente.");
            return;
        }
        if (searchStrategy == null) {
            System.out.println("O serviço de vídeos não está disponível.");
        }

    }

    private static String lerString (String mensagem){
            System.out.println(mensagem);
            return leitor.nextLine().trim();
        }
    }