
package sortbench.Algorithms;

import sortbench.Model.SortMetrics;

public class QuickSortDualPivot implements SortingAlgorithm {

    @Override
    public String getNome() {
        return "QuickSort Dual-Pivot";
    }

    @Override
    public void ordenar(int[] dados) {
        if (dados == null || dados.length < 2) {
            return;
        }

        quickSortSemMetricas(dados, 0, dados.length - 1);
    }

    @Override
    public void ordenar(int[] dados, SortMetrics metricas) {
        if (dados == null || dados.length < 2) {
            return;
        }

        if (metricas == null) {
            throw new IllegalArgumentException(
                    "As métricas não podem ser nulas."
            );
        }

        quickSort(dados, 0, dados.length - 1, metricas);
    }

    /*
     * Escolhe os pivôs usando cinco posições do trecho.
     * O segundo e o quarto valores da amostra são utilizados
     * para evitar escolher sempre os extremos em dados ordenados.
     */
    private int[] selecionarPivos(
            int[] dados, int inicio, int fim, SortMetrics metricas) {

        int meio = inicio + (fim - inicio) / 2;
        int quarto = inicio + (fim - inicio) / 4;
        int tresQuartos = fim - (fim - inicio) / 4;

        int[] amostra = {
            dados[inicio],
            dados[quarto],
            dados[meio],
            dados[tresQuartos],
            dados[fim]
        };

        // Ordenação por inserção da pequena amostra.
        for (int i = 1; i < amostra.length; i++) {
            int valor = amostra[i];
            int j = i - 1;

            while (j >= 0) {
                if (metricas != null) {
                    metricas.registrarComparacao();
                }

                if (amostra[j] <= valor) {
                    break;
                }

                amostra[j + 1] = amostra[j];
                j--;
            }

            amostra[j + 1] = valor;
        }

        return new int[]{amostra[1], amostra[3]};
    }

    private int[] prepararPivos(
            int[] dados, int inicio, int fim, SortMetrics metricas) {

        int[] pivos = selecionarPivos(
                dados, inicio, fim, metricas
        );

        int pivo1 = pivos[0];
        int pivo2 = pivos[1];

        int indice1 = inicio;

        while (indice1 <= fim) {
            if (metricas != null) {
                metricas.registrarComparacao();
            }

            if (dados[indice1] == pivo1) {
                break;
            }

            indice1++;
        }

        trocar(dados, inicio, indice1, metricas);

        int indice2 = inicio + 1;

        while (indice2 <= fim) {
            if (metricas != null) {
                metricas.registrarComparacao();
            }

            if (dados[indice2] == pivo2) {
                break;
            }

            indice2++;
        }

        trocar(dados, fim, indice2, metricas);

        return new int[]{dados[inicio], dados[fim]};
    }

    /*
     * Versão com métricas.
     * A maior partição é processada por repetição, não por
     * uma nova chamada recursiva. Isso limita a pilha utilizada.
     */
    private void quickSort(
            int[] dados, int inicio, int fim, SortMetrics metricas) {

        while (inicio < fim) {

            int[] pivos = prepararPivos(
                    dados, inicio, fim, metricas
            );

            int pivo1 = pivos[0];
            int pivo2 = pivos[1];

            // Se os pivôs forem iguais, agrupa os valores iguais.
            if (pivo1 == pivo2) {
                int menor = inicio;
                int atual = inicio;
                int maior = fim;

                while (atual <= maior) {
                    metricas.registrarComparacao();

                    if (dados[atual] < pivo1) {
                        trocar(dados, atual, menor, metricas);
                        menor++;
                        atual++;
                    } else {
                        metricas.registrarComparacao();

                        if (dados[atual] > pivo1) {
                            trocar(dados, atual, maior, metricas);
                            maior--;
                        } else {
                            atual++;
                        }
                    }
                }

                int tamanhoEsquerda = menor - inicio;
                int tamanhoDireita = fim - maior;

                if (tamanhoEsquerda < tamanhoDireita) {
                    quickSort(
                            dados, inicio, menor - 1, metricas
                    );
                    inicio = maior + 1;
                } else {
                    quickSort(
                            dados, maior + 1, fim, metricas
                    );
                    fim = menor - 1;
                }

                continue;
            }

            int menor = inicio + 1;
            int maior = fim - 1;
            int atual = menor;

            while (atual <= maior) {
                metricas.registrarComparacao();

                if (dados[atual] < pivo1) {
                    trocar(dados, atual, menor, metricas);
                    menor++;
                } else {
                    metricas.registrarComparacao();

                    if (dados[atual] > pivo2) {
                        while (atual < maior) {
                            metricas.registrarComparacao();

                            if (dados[maior] > pivo2) {
                                maior--;
                            } else {
                                break;
                            }
                        }

                        trocar(dados, atual, maior, metricas);
                        maior--;

                        metricas.registrarComparacao();

                        if (dados[atual] < pivo1) {
                            trocar(dados, atual, menor, metricas);
                            menor++;
                        }
                    }
                }

                atual++;
            }

            menor--;
            maior++;

            trocar(dados, inicio, menor, metricas);
            trocar(dados, fim, maior, metricas);

            int inicio1 = inicio;
            int fim1 = menor - 1;

            int inicio2 = menor + 1;
            int fim2 = maior - 1;

            int inicio3 = maior + 1;
            int fim3 = fim;

            int tamanho1 = Math.max(0, fim1 - inicio1 + 1);
            int tamanho2 = Math.max(0, fim2 - inicio2 + 1);
            int tamanho3 = Math.max(0, fim3 - inicio3 + 1);

            // Recursa nas partições menores e continua na maior.
            if (tamanho1 >= tamanho2 && tamanho1 >= tamanho3) {
                quickSort(dados, inicio2, fim2, metricas);
                quickSort(dados, inicio3, fim3, metricas);
                fim = fim1;

            } else if (tamanho2 >= tamanho1 && tamanho2 >= tamanho3) {
                quickSort(dados, inicio1, fim1, metricas);
                quickSort(dados, inicio3, fim3, metricas);
                inicio = inicio2;
                fim = fim2;

            } else {
                quickSort(dados, inicio1, fim1, metricas);
                quickSort(dados, inicio2, fim2, metricas);
                inicio = inicio3;
            }
        }
    }

    private void trocar(
            int[] dados, int i, int j, SortMetrics metricas) {

        if (i == j) {
            return;
        }

        int temporario = dados[i];
        dados[i] = dados[j];
        dados[j] = temporario;

        if (metricas != null) {
            metricas.registrarMovimentacao();
            metricas.registrarMovimentacao();
            metricas.registrarMovimentacao();
            metricas.registrarTroca();
        }
    }

    /*
     * Versão sem instrumentação para as medições de tempo.
     */
    private void quickSortSemMetricas(
            int[] dados, int inicio, int fim) {

        while (inicio < fim) {

            int[] pivos = prepararPivosSemMetricas(
                    dados, inicio, fim
            );

            int pivo1 = pivos[0];
            int pivo2 = pivos[1];

            if (pivo1 == pivo2) {
                int menor = inicio;
                int atual = inicio;
                int maior = fim;

                while (atual <= maior) {
                    if (dados[atual] < pivo1) {
                        trocarSemMetricas(dados, atual, menor);
                        menor++;
                        atual++;
                    } else if (dados[atual] > pivo1) {
                        trocarSemMetricas(dados, atual, maior);
                        maior--;
                    } else {
                        atual++;
                    }
                }

                int tamanhoEsquerda = menor - inicio;
                int tamanhoDireita = fim - maior;

                if (tamanhoEsquerda < tamanhoDireita) {
                    quickSortSemMetricas(dados, inicio, menor - 1);
                    inicio = maior + 1;
                } else {
                    quickSortSemMetricas(dados, maior + 1, fim);
                    fim = menor - 1;
                }

                continue;
            }

            int menor = inicio + 1;
            int maior = fim - 1;
            int atual = menor;

            while (atual <= maior) {
                if (dados[atual] < pivo1) {
                    trocarSemMetricas(dados, atual, menor);
                    menor++;
                } else if (dados[atual] > pivo2) {
                    while (atual < maior && dados[maior] > pivo2) {
                        maior--;
                    }

                    trocarSemMetricas(dados, atual, maior);
                    maior--;

                    if (dados[atual] < pivo1) {
                        trocarSemMetricas(dados, atual, menor);
                        menor++;
                    }
                }

                atual++;
            }

            menor--;
            maior++;

            trocarSemMetricas(dados, inicio, menor);
            trocarSemMetricas(dados, fim, maior);

            int inicio1 = inicio;
            int fim1 = menor - 1;

            int inicio2 = menor + 1;
            int fim2 = maior - 1;

            int inicio3 = maior + 1;
            int fim3 = fim;

            int tamanho1 = Math.max(0, fim1 - inicio1 + 1);
            int tamanho2 = Math.max(0, fim2 - inicio2 + 1);
            int tamanho3 = Math.max(0, fim3 - inicio3 + 1);

            if (tamanho1 >= tamanho2 && tamanho1 >= tamanho3) {
                quickSortSemMetricas(dados, inicio2, fim2);
                quickSortSemMetricas(dados, inicio3, fim3);
                fim = fim1;

            } else if (tamanho2 >= tamanho1 && tamanho2 >= tamanho3) {
                quickSortSemMetricas(dados, inicio1, fim1);
                quickSortSemMetricas(dados, inicio3, fim3);
                inicio = inicio2;
                fim = fim2;

            } else {
                quickSortSemMetricas(dados, inicio1, fim1);
                quickSortSemMetricas(dados, inicio2, fim2);
                inicio = inicio3;
            }
        }
    }

    private int[] prepararPivosSemMetricas(
            int[] dados, int inicio, int fim) {

        int[] pivos = selecionarPivosSemMetricas(
                dados, inicio, fim
        );

        int indice1 = inicio;

        while (dados[indice1] != pivos[0]) {
            indice1++;
        }

        trocarSemMetricas(dados, inicio, indice1);

        int indice2 = inicio + 1;

        while (dados[indice2] != pivos[1]) {
            indice2++;
        }

        trocarSemMetricas(dados, fim, indice2);

        return new int[]{dados[inicio], dados[fim]};
    }

    private int[] selecionarPivosSemMetricas(
            int[] dados, int inicio, int fim) {

        int meio = inicio + (fim - inicio) / 2;
        int quarto = inicio + (fim - inicio) / 4;
        int tresQuartos = fim - (fim - inicio) / 4;

        int[] amostra = {
            dados[inicio],
            dados[quarto],
            dados[meio],
            dados[tresQuartos],
            dados[fim]
        };

        for (int i = 1; i < amostra.length; i++) {
            int valor = amostra[i];
            int j = i - 1;

            while (j >= 0 && amostra[j] > valor) {
                amostra[j + 1] = amostra[j];
                j--;
            }

            amostra[j + 1] = valor;
        }

        return new int[]{amostra[1], amostra[3]};
    }

    private void trocarSemMetricas(int[] dados, int i, int j) {
        if (i == j) {
            return;
        }

        int temporario = dados[i];
        dados[i] = dados[j];
        dados[j] = temporario;
    }
}
