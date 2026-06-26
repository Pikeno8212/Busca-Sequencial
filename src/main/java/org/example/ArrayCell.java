package org.example;

import javax.swing.*;
import java.awt.*;

/**
 * ArrayCell.java
 *
 * Responsabilidade: Representar visualmente uma única "célula" ou elemento
 * do nosso vetor na interface gráfica. Ele controla seu próprio valor numérico
 * e sua cor de fundo para indicar o estado da busca (Branco, Amarelo, Vermelho, Verde).
 */
public class ArrayCell extends JPanel {
    
    private JLabel valueLabel;
    
    /**
     * Construtor da célula do array.
     * @param value O valor numérico que esta célula exibirá.
     */
    public ArrayCell(int value) {
        // Layout para centralizar o texto dentro do quadrado
        setLayout(new BorderLayout());
        
        // Define o tamanho fixo para manter aspecto de "quadrado" uniforme
        setPreferredSize(new Dimension(60, 60));
        
        // Configurações da borda (parecendo uma tabela/célula)
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        // O fundo padrão inicia Branco (ainda não analisado)
        setBackground(Color.WHITE);
        
        // Configurações do texto
        valueLabel = new JLabel(String.valueOf(value), SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        add(valueLabel, BorderLayout.CENTER);
    }
    
    /**
     * Muda a cor de fundo desta célula específica.
     * Cores planejadas: Amarelo (comparando), Vermelho (já analisado), Verde (encontrado).
     * @param color Cor desejada.
     */
    public void setCellColor(Color color) {
        setBackground(color);
        // Garante que o componente se redesenhe com a nova cor imediatamente
        repaint();
    }
}
