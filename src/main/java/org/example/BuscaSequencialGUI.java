package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Classe responsável por construir e gerenciar a Interface Gráfica de Usuário (GUI).
 * Ela coleta os dados, os valida, trata erros via JOptionPane, e chama a lógica da BuscaSequencial.
 */
public class BuscaSequencialGUI extends JFrame {

    // Declaração dos componentes da interface
    private JTextField txtLista;
    private JTextField txtAlvo;
    private JTextArea txtResultado;
    private JButton btnBuscar;

    /**
     * Construtor da janela gráfica.
     * Configura o layout, os componentes (rótulos, campos de texto, botão) e seus eventos.
     */
    public BuscaSequencialGUI() {
        // Configurações básicas da janela da interface
        setTitle("Sistema de Busca Sequencial");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha o programa ao fechar a janela
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setLayout(new BorderLayout(10, 10)); // Define o layout principal com margens de 10px

        // ==========================================
        // PAINEL SUPERIOR: ENTRADA DE DADOS
        // ==========================================
        // Usando GridLayout para alinhar os labels e os textfields (2 linhas, 2 colunas)
        JPanel pnlEntrada = new JPanel(new GridLayout(2, 2, 5, 10));
        pnlEntrada.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        // Rótulo e campo de texto para a Lista
        pnlEntrada.add(new JLabel("Lista (separada por espaço):"));
        txtLista = new JTextField();
        pnlEntrada.add(txtLista);

        // Rótulo e campo de texto para o número a buscar (Alvo)
        pnlEntrada.add(new JLabel("Número para buscar:"));
        txtAlvo = new JTextField();
        pnlEntrada.add(txtAlvo);

        // Adiciona o painel de entrada ao topo (NORTH) do JFrame
        add(pnlEntrada, BorderLayout.NORTH);

        // ==========================================
        // PAINEL CENTRAL: BOTÃO DE BUSCAR
        // ==========================================
        JPanel pnlBotao = new JPanel();
        btnBuscar = new JButton(" Buscar ");
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Muda o cursor ao passar o mouse
        
        // Define o evento (ação) que ocorrerá quando o botão for clicado
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executarBusca(); // Chama o método principal de validação e execução
            }
        });
        
        pnlBotao.add(btnBuscar);
        // Adiciona o painel do botão ao centro (CENTER) do JFrame
        add(pnlBotao, BorderLayout.CENTER);

        // ==========================================
        // PAINEL INFERIOR: ÁREA DE RESULTADO
        // ==========================================
        JPanel pnlResultado = new JPanel(new BorderLayout());
        pnlResultado.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        pnlResultado.add(new JLabel("Resultado:"), BorderLayout.NORTH);

        // Configuração da área de texto que mostrará os resultados do processamento
        txtResultado = new JTextArea();
        txtResultado.setEditable(false); // O usuário não deve digitar aqui, serve apenas para saída
        txtResultado.setFont(new Font("Monospaced", Font.PLAIN, 13)); // Usa fonte monoespaçada
        txtResultado.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // Adiciona um painel de rolagem para quando o texto da lista for muito grande
        JScrollPane scrollPane = new JScrollPane(txtResultado);
        scrollPane.setPreferredSize(new Dimension(400, 120));
        pnlResultado.add(scrollPane, BorderLayout.CENTER);

        // Adiciona a área de resultados à base (SOUTH) do JFrame
        add(pnlResultado, BorderLayout.SOUTH);
    }

    /**
     * Método acionado pelo botão 'Buscar'.
     * Realiza todas as validações de dados (campos vazios, números inválidos, lista vazia)
     * e após garantir que os dados estão corretos, invoca a lógica de busca do algoritmo.
     */
    private void executarBusca() {
        // 1. Inicialmente, limpa a área de resultados antes de qualquer nova tentativa
        txtResultado.setText("");

        // Pega os textos digitados e aplica 'trim()' para remover espaços inúteis no início e fim
        String textoLista = txtLista.getText().trim();
        String textoAlvo = txtAlvo.getText().trim();

        // 2. Validação: Verifica se os campos estão vazios
        if (textoLista.isEmpty() || textoAlvo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, preencha todos os campos corretamente.",
                    "Atenção - Campo Vazio",
                    JOptionPane.WARNING_MESSAGE);
            return; // Interrompe a execução aqui caso vazio
        }

        try {
            // 3. Tratamento de espaços extras
            // O split("\\s+") garante que dividiremos a string por espaços, ignorando múltiplos espaços seguidos
            String[] partesString = textoLista.split("\\s+");
            
            // 4. Validação extra de lista vazia
            // (pode ocorrer se a string continha apenas espaços que foram removidos pelo split)
            if (partesString.length == 0 || (partesString.length == 1 && partesString[0].isEmpty())) {
                JOptionPane.showMessageDialog(this,
                    "A lista informada não contém itens válidos.",
                    "Atenção - Lista Vazia",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 5. Conversão: Tenta converter os elementos String capturados para o tipo Inteiro
            // Essa iteração pode lançar NumberFormatException se o usuário digitar letras ao invés de números.
            int[] listaInteiros = new int[partesString.length];
            for (int i = 0; i < partesString.length; i++) {
                listaInteiros[i] = Integer.parseInt(partesString[i]); // Converter a parte para inteiro
            }

            // Da mesma forma, converte o texto do alvo buscado para número Inteiro.
            int alvo = Integer.parseInt(textoAlvo);

            // ==========================================
            // INTEGRAÇÃO COM A LÓGICA DO ALGORITMO
            // ==========================================
            // Chama o método estático contendo o algoritmo, passando o array e o alvo.
            int indiceEncontrado = BuscaSequencial.buscaSequencial(listaInteiros, alvo);

            // ==========================================
            // CONSTRUÇÃO E EXIBIÇÃO DA MENSAGEM FINAL
            // ==========================================
            StringBuilder resultadoFinal = new StringBuilder();
            
            // Exibe a lista digitada e formatada
            resultadoFinal.append("Sua lista: ").append(Arrays.toString(listaInteiros)).append("\n\n");

            // Avalia a resposta do algoritmo
            if (indiceEncontrado != -1) {
                // Se foi retornado um índice, logo, elemento existe
                resultadoFinal.append("Sucesso!\n");
                resultadoFinal.append("Elemento ").append(alvo)
                              .append(" encontrado no índice ").append(indiceEncontrado).append(".");
            } else {
                // Se o retorno foi -1, o elemento não existe no array
                resultadoFinal.append("O elemento ").append(alvo)
                              .append(" não existe na lista.");
            }

            // Substitui o texto da caixa de resultados pelas informações geradas
            txtResultado.setText(resultadoFinal.toString());

        } catch (NumberFormatException e) {
            // 6. Tratamento de erro: Caracteres Inválidos (ex: Letras e Simbolos onde deveria ser Número)
            JOptionPane.showMessageDialog(this,
                    "Erro: Certifique-se de digitar apenas NÚMEROS inteiros válidos, separados por espaço.\n\nExemplo de uso: 10 20 30 40 50",
                    "Erro de Entrada",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
