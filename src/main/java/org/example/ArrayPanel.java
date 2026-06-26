package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ArrayPanel.java
 *
 * Responsabilidade: Funcionar como um container gráfico para todas as ArrayCells.
 * Ele recebe o array bruto, limpa o painel, recria e enfileira as células visuais.
 * Fornece métodos facilitadores para alterar as cores de células específicas usando o índice.
 */
public class ArrayPanel extends JPanel {
    
    // Lista de referência para conseguirmos acessar as células pelo índice facilmente depois
    private List<ArrayCell> cells;

    public ArrayPanel() {
        // FlowLayout organiza os componentes um ao lado do outro, 
        // e quebra a linha automaticamente se faltar espaço, ideal para nosso vetor
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setBackground(new Color(240, 240, 240)); // Fundo cinza claro
        cells = new ArrayList<>();
    }

    /**
     * Constrói a visualização gráfica baseada no array de inteiros fornecido.
     * Limpa qualquer array anterior desenhado.
     * @param array Array de inteiros a ser desenhado.
     */
    public void generateArrayCells(int[] array) {
        // Limpa referências e componentes gráficos antigos
        this.removeAll();
        cells.clear();

        // Para cada número no array real, criamos uma representação gráfica ArrayCell
        for (int value : array) {
            ArrayCell cell = new ArrayCell(value);
            cells.add(cell);
            this.add(cell);
        }

        // Informa ao Java para reavaliar o layout e se pintar novamente (refresh)
        this.revalidate();
        this.repaint();
    }

    /**
     * Altera a cor de uma célula específica.
     * @param index Índice do elemento no array original.
     * @param color Nova cor desejada.
     */
    public void setCellColor(int index, Color color) {
        if (index >= 0 && index < cells.size()) {
            cells.get(index).setCellColor(color);
        }
    }
    
    /**
     * Retorna a cor atual de uma célula (usada para não sobrescrever o Verde no fim da animação, caso precise)
     */
    public Color getCellColor(int index) {
        if (index >= 0 && index < cells.size()) {
            return cells.get(index).getBackground();
        }
        return Color.WHITE;
    }

    /**
     * Restaura todas as células para a cor original (Branco).
     */
    public void resetAllColors() {
        for (ArrayCell cell : cells) {
            cell.setCellColor(Color.WHITE);
        }
    }
}
