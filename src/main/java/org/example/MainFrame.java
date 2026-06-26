package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * MainFrame.java
 *
 * Responsabilidade: Gerenciar a interface gráfica principal do software educativo.
 * Contém os controles de entrada, botões de ação, o visualizador do array,
 * o painel de logs (passo a passo) e a lógica da animação (Timer).
 */
public class MainFrame extends JFrame {

    // Componentes de Controle
    private JTextField txtLista;
    private JTextField txtAlvo;
    private JButton btnIniciar;
    private JButton btnLimpar;
    private JButton btnAleatorio;
    private JSlider sldVelocidade;

    // Componente de Visualização
    private ArrayPanel arrayPanel;

    // Componente de Logs e Estatísticas
    private JTextArea txtLog;

    // Variáveis de Estado da Animação
    private Timer timer;
    private int[] arrayAtual;
    private int alvoAtual;
    private int animIndex;
    
    // Variáveis para as métricas reais
    private long tempoExecucaoMs;
    private boolean encontrou;
    private int indiceRealEncontrado;

    public MainFrame() {
        setTitle("Algoritmo de Busca Sequencial - Simulador Educativo");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
    }

    /**
     * Monta toda a estrutura visual da janela.
     */
    private void inicializarComponentes() {
        // ==========================================
        // PAINEL SUPERIOR: Controles e Inputs
        // ==========================================
        JPanel pnlTop = new JPanel(new BorderLayout(5, 5));
        pnlTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sub-painel para os Inputs de Texto
        JPanel pnlInputs = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlInputs.add(new JLabel("Lista de números (separados por espaço):"));
        txtLista = new JTextField();
        txtLista.setFont(new Font("Arial", Font.PLAIN, 16));
        pnlInputs.add(txtLista);

        pnlInputs.add(new JLabel("Número a buscar:"));
        txtAlvo = new JTextField();
        txtAlvo.setFont(new Font("Arial", Font.PLAIN, 16));
        pnlInputs.add(txtAlvo);

        // Sub-painel para os Botões
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnIniciar = new JButton("Iniciar Busca");
        btnLimpar = new JButton("Limpar");
        btnAleatorio = new JButton("Gerar Lista Aleatória");
        
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 14));
        btnLimpar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAleatorio.setFont(new Font("Arial", Font.BOLD, 14));

        pnlBotoes.add(btnIniciar);
        pnlBotoes.add(btnLimpar);
        pnlBotoes.add(btnAleatorio);

        // Sub-painel para o Slider (Velocidade)
        JPanel pnlVelocidade = new JPanel(new BorderLayout());
        pnlVelocidade.add(new JLabel("Velocidade da Animação: Lento -> Rápido", SwingConstants.CENTER), BorderLayout.NORTH);
        sldVelocidade = new JSlider(JSlider.HORIZONTAL, 100, 2000, 1000); // 100ms a 2000ms
        sldVelocidade.setInverted(true); // Para que à direita seja mais rápido (menor delay)
        pnlVelocidade.add(sldVelocidade, BorderLayout.CENTER);

        // Agrupando os sub-paineis do topo
        JPanel pnlTopAgrupado = new JPanel(new BorderLayout());
        pnlTopAgrupado.add(pnlInputs, BorderLayout.NORTH);
        pnlTopAgrupado.add(pnlBotoes, BorderLayout.CENTER);
        pnlTopAgrupado.add(pnlVelocidade, BorderLayout.SOUTH);
        pnlTop.add(pnlTopAgrupado, BorderLayout.CENTER);
        
        add(pnlTop, BorderLayout.NORTH);

        // ==========================================
        // PAINEL CENTRAL: Representação do Vetor
        // ==========================================
        arrayPanel = new ArrayPanel();
        // Usamos um JScrollPane caso a lista seja gigantesca
        JScrollPane scrollArray = new JScrollPane(arrayPanel);
        scrollArray.setBorder(BorderFactory.createTitledBorder("Visualização do Vetor (Vetor Original)"));
        scrollArray.setPreferredSize(new Dimension(800, 150));
        add(scrollArray, BorderLayout.CENTER);

        // ==========================================
        // PAINEL INFERIOR: Logs de Execução
        // ==========================================
        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtLog.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Log de Execução Passo a Passo"));
        scrollLog.setPreferredSize(new Dimension(800, 250));
        add(scrollLog, BorderLayout.SOUTH);

        // ==========================================
        // AÇÕES DOS BOTÕES (Event Listeners)
        // ==========================================
        btnAleatorio.addActionListener(e -> gerarListaAleatoria());
        btnLimpar.addActionListener(e -> limparTudo());
        btnIniciar.addActionListener(e -> prepararEIniciarBusca());
    }

    /**
     * Gera uma lista aleatória preenchendo o campo de texto automaticamente.
     */
    private void gerarListaAleatoria() {
        Random rand = new Random();
        int tamanho = rand.nextInt(8) + 5; // Entre 5 e 12 elementos
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            sb.append(rand.nextInt(100)).append(" ");
        }
        txtLista.setText(sb.toString().trim());
        
        // Sorteia um número que tenha alta chance de estar na lista ou não
        if (rand.nextBoolean()) {
            String[] nums = sb.toString().trim().split(" ");
            txtAlvo.setText(nums[rand.nextInt(nums.length)]);
        } else {
            txtAlvo.setText(String.valueOf(rand.nextInt(100)));
        }
    }

    /**
     * Reseta a interface inteira para o estado inicial.
     */
    private void limparTudo() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        txtLista.setText("");
        txtAlvo.setText("");
        txtLog.setText("");
        arrayPanel.removeAll();
        arrayPanel.revalidate();
        arrayPanel.repaint();
        habilitarControles(true);
    }

    /**
     * Valida os campos, processa as strings e dá início ao processo de execução e animação.
     */
    private void prepararEIniciarBusca() {
        // Validações Base (Campos Vazios e Múltiplos Espaços tratados com trim/split)
        String textoLista = txtLista.getText().trim();
        String textoAlvo = txtAlvo.getText().trim();

        if (textoLista.isEmpty() || textoAlvo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha a lista e o número que deseja buscar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Separação segura ignorando espaços múltiplos
            String[] partes = textoLista.split("\\s+");
            arrayAtual = new int[partes.length];
            
            for (int i = 0; i < partes.length; i++) {
                arrayAtual[i] = Integer.parseInt(partes[i]); // Pode jogar NumberFormatException (letras)
            }
            
            if (arrayAtual.length == 0) {
                JOptionPane.showMessageDialog(this, "A lista fornecida está vazia.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            alvoAtual = Integer.parseInt(textoAlvo); // Valida se alvo contém letras
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Entrada inválida! Digite apenas números separados por espaços. Não utilize letras.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- MEDIÇÃO DE TEMPO DA CPU REAL ANTES DA ANIMAÇÃO LENTA ---
        // Aqui chamamos o algoritmo "puro" para saber exatamente quanto tempo a CPU levou
        // antes de simularmos isso devagarzinho visualmente
        long startNano = System.nanoTime();
        indiceRealEncontrado = SequentialSearch.search(arrayAtual, alvoAtual);
        long endNano = System.nanoTime();
        
        tempoExecucaoMs = (endNano - startNano) / 1000000; // Converte nanosegundos para milisegundos
        encontrou = (indiceRealEncontrado != -1);

        // --- PREPARAÇÃO VISUAL ---
        txtLog.setText("");
        arrayPanel.generateArrayCells(arrayAtual);
        habilitarControles(false); // Impede o usuário de clicar em Iniciar repetidamente
        
        // Inicia a Rotina da Animação
        iniciarAnimacao();
    }

    /**
     * Controla os estados de cada passo (Tick) do algoritmo visualmente usando Timer.
     */
    private void iniciarAnimacao() {
        animIndex = 0;
        adicionarLog("Iniciando busca...\n");

        int delay = sldVelocidade.getValue();
        timer = new Timer(delay, e -> tickAnimacao());
        timer.start();
    }

    /**
     * O Método chamado a cada "Tick" do timer.
     * Representa as iterações do laço 'for' da Busca Sequencial, uma de cada vez.
     */
    private void tickAnimacao() {
        // Se estamos além do índice 0, significa que no Tick anterior analisamos alguém.
        // O elemento anterior deve ficar vermelho (pois já foi analisado e sabemos que não era o alvo).
        if (animIndex > 0 && arrayPanel.getCellColor(animIndex - 1) == Color.YELLOW) {
            arrayPanel.setCellColor(animIndex - 1, Color.RED);
        }
        
        // Condição do 'for' (i < lista.length)
        if (animIndex < arrayAtual.length) {
            // Pinta o índice atual de Amarelo
            arrayPanel.setCellColor(animIndex, Color.YELLOW);
            adicionarLog("Comparando posição " + animIndex);
            adicionarLog(arrayAtual[animIndex] + " == " + alvoAtual + " ?");

            // Condição do 'if (lista[i] == chave)'
            if (arrayAtual[animIndex] == alvoAtual) {
                adicionarLog("Elemento encontrado!");
                adicionarLog("Busca encerrada.\n");
                arrayPanel.setCellColor(animIndex, Color.GREEN);
                
                finalizarAnimacao(); // Encerra tudo
            } else {
                adicionarLog("Não encontrado.\n");
                animIndex++; // Avança iterador
            }
        } else {
            // Caso chegue ao fim do vetor e não tenha encontrado
            adicionarLog("Elemento não encontrado.");
            adicionarLog("Busca finalizada.\n");
            finalizarAnimacao(); // Encerra tudo
        }
    }

    /**
     * Para o timer e imprime as Estatísticas exigidas.
     */
    private void finalizarAnimacao() {
        timer.stop();
        habilitarControles(true);
        
        // O número de comparações é o próprio índice atual + 1 (caso tenha encontrado), 
        // ou o tamanho do vetor (caso tenha percorrido tudo e falhado).
        int comparacoes = encontrou ? (animIndex + 1) : arrayAtual.length;

        adicionarLog("=========================");
        adicionarLog("ESTATÍSTICAS DA EXECUÇÃO");
        adicionarLog("=========================");
        adicionarLog("Resultado: " + (encontrou ? "Elemento ENCONTRADO." : "Elemento NÃO ENCONTRADO."));
        adicionarLog("Posição: " + (encontrou ? String.valueOf(animIndex) : "-1 (Inexistente)"));
        adicionarLog("Comparações realizadas: " + comparacoes);
        adicionarLog("Tempo (Lógica Pura CPU): " + tempoExecucaoMs + " ms");
        adicionarLog("");
        adicionarLog("Complexidade Teórica do Algoritmo O(n):");
        adicionarLog(" - Melhor Caso: O(1)   (Se o item estivesse na posição 0)");
        adicionarLog(" - Caso Médio: O(n)    (Meio do vetor)");
        adicionarLog(" - Pior Caso: O(n)     (Se o item estiver no final ou não existir)");
        adicionarLog(" - Espaço: O(1)        (Não criamos novos vetores na memória na busca pura)");
    }

    /**
     * Adiciona texto à caixa de log e rola para baixo automaticamente.
     */
    private void adicionarLog(String texto) {
        txtLog.append(texto + "\n");
        // Garante que o scroll desça sempre para a última linha escrita
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }

    /**
     * Habilita ou desabilita botões e campos de entrada para impedir interferência durante a animação.
     */
    private void habilitarControles(boolean habilitado) {
        txtLista.setEditable(habilitado);
        txtAlvo.setEditable(habilitado);
        btnIniciar.setEnabled(habilitado);
        btnAleatorio.setEnabled(habilitado);
    }
}
