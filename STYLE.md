# BLOCK ALT UNO — guia de estilo

Uma frase: **blocos de materiais reais sobre uma mesa de madeira quente, iluminados por uma única luz dourada vinda do alto à esquerda, em mundos que se movem ao fundo.**

Tudo no jogo é desenhado por código (texturas, cenários, ícones, explosões e música). Não há imagens nem áudio baixados; o app funciona sem internet.

## Paleta-âncora

| Nome | Hex | Função |
|---|---|---|
| Nogueira | `#241912` | Fundo do app, painéis escuros |
| Painel | `#33241a` | Botões, cartões, barras |
| Bordo | `#d6ae7b` | Tabuleiro |
| Creme | `#f4e7d4` | Texto principal |
| Âmbar | `#eaa53c` | Destaque, botão principal, pontuação |
| Ouro | `#ffd36b` | Celebração (recorde, combo, poder) |

### Cores com significado (nunca trocar)

| Cor | Hex | Significa |
|---|---|---|
| Ouro | `#ffd36b` | Celebração: recorde, combo, poder liberado |
| Verde-folha | `#6cc070` | Sucesso: fase concluída, próxima fase |
| Vermelho-cereja | `#e0674b` | Perigo: derrota, sem espaço |
| Âmbar | `#eaa53c` | Ação principal |

## Luz

1. **Uma direção:** a luz vem do alto à esquerda. Todo bloco tem brilho em cima/esquerda e sombra embaixo/direita.
2. **Subtom quente:** tudo recebe uma lavagem dourada leve (camada `#light`), inclusive mundos frios.
3. **Contraste real:** cada bloco tem plano de luz, meio-tom e sombra definidos.
4. **Profundidade:** o fundo é sempre mais calmo e escuro que o tabuleiro; uma vinheta escurece as bordas e um foco de luz fica sobre o tabuleiro.

## Letras

- **Display:** condensada e pesada (`sans-serif-condensed` no Android), maiúsculas, espaçamento levemente aberto. O logotipo é preenchido com a textura de madeira do jogo e tem contorno marrom.
- **Texto:** sem serifa do sistema (Roboto no Android), 12–15 px.
- **Números:** sempre `tabular-nums`, para não "pular" quando a pontuação muda.

## Movimento

- **Toque → reação em 1 quadro:** a peça cresce ao ser tocada; todo botão tem som de clique.
- **Peças assentam:** ao encaixar, os blocos fazem um squash (≈0,78 × 1,12, ancorado embaixo, ~0,1 s) e soltam poeira para cima.
- **Impacto grande = pausa curta:** limpezas grandes congelam os efeitos por ~70 ms (hitstop) antes de explodir.
- **Nada no fundo compete com o tabuleiro:** fragmentos caem, quicam e somem; peças na bandeja só respiram de leve.
- **Telas entram com transição** (fade + leve subida, 0,22 s).

## Som

- Efeitos curtos (< 0,5 s), volume 0,3–0,5; música de fundo bem mais baixa.
- Cada efeito varia o tom em ±5% para não cansar.
- Toda ação tem som: tocar botão, pegar peça, encaixar, errar o encaixe, limpar linha, combo, subir de nível, recorde, derrota.
- Músicas: clássicas de domínio público. No menu, calmas; nos modos, com percussão leve que acelera com o nível.

## Estilos visuais (v11, Pixel na v12)

Uma única configuração (`Ajustes → Estilo visual`, salva como `visualStyle`) escolhe como tudo é desenhado. As regras do jogo não mudam.

| | Realismo (padrão) | Animação | Pixel (v12) |
|---|---|---|---|
| Blocos | Texturas naturais + chanfro iluminado (alto/esquerda), borda escura fina, brilho no canto | Cores chapadas com sombreamento em camadas, contorno marrom grosso, brilho estilizado e um motivo por material | Grade 16×16 em 5 tons por material, chanfro de 1 px, pontilhado só nas bordas de luz e sombra, escala sem suavização |
| Casas vazias | Furos rebaixados (sombra interna) | Casas creme arredondadas com borda suave | Casas quadradas com chanfro duro de 3 px |
| Encaixe | Squash sutil (0,93 × 1,06) | Squash elástico (0,7 × 1,3 → 1,14 × 0,86) | Squash em 3 passos (`steps`) |
| Eliminação | Bloco "racha" (clarão → encolhe); estilhaços com textura, raios de luz, brilho | Bloco "estoura" (cresce, gira, some); pedaços com contorno, estrelas, anel de impacto | Bloco "esfarela" em 4 passos; quadradinhos caem com gravidade (lava brilha e solta fumaça, gelo/metal faíscam, água espirra), anel quadrado |
| Textos | Folha de ouro com contorno escuro | Contorno cartoon com sombra dura | Fonte monoespaçada com sombra dura de pixel |

Os três estilos usam o mesmo código de jogo, as mesmas partículas e o mesmo agendamento. Só mudam os parâmetros e os pintores de textura (`Skins`), a explosão (`Scene.explode`) e as animações CSS (`body.vs-real` / `body.vs-anim` / `body.vs-pixel`).

Realismo (v13): madeira, cimento, tijolo, gelo e água voltaram às texturas da v10 (as mais naturais), agora geradas em 256 px para telas Full HD. Lava, mármore e metal seguem os pintores da v11/v12.

Fragmentos do Realismo (v13): cada bloco quebra em 4 pedaços sólidos e serrilhados (um por canto), com face texturizada e paredes laterais para baixo/direita (longe da luz). Eles giram só no plano, sem "virar" como papel; a face brilha por um instante ao girar e as bordas voltadas para a luz ficam claras. Junto: pedrinhas facetadas, faíscas com rastro, clarão no ponto do impacto, raios de luz e poeira. As animações de encaixe não mudaram.

## Paleta dos blocos (v13)

15 conjuntos de 6 cores que cobrem o círculo cromático inteiro: madeiras, arco-íris, floresta, oceano, fogo e lava, pôr do sol, joias, gelo glacial, tropical, realeza, outono, doce, cosmos, terra, pedra e aço. Os tons de madeira, gelo, fogo e pedra foram tirados das fotos de referência. Dentro de cada conjunto as cores variam em matiz e em claridade, para os blocos não se confundirem. O Clássico troca de conjunto a cada nível; cada mundo da História alterna dois conjuntos.

## Cor de cada modo

| Modo | Cor-assinatura |
|---|---|
| Clássico | Âmbar `#eaa53c` |
| Sudoku | Verde-azulado `#3fb59a` (divisórias 3×3 desenhadas) |
| Tetris | Roxo `#a66be0` |
| História | Cobre `#d9824a` |

A cor do modo aparece no nome do modo, na barra de progresso, no filete da barra superior e no contorno do tabuleiro.

## Leitura do tabuleiro

- Prévia válida: a peça na sua cor com borda tracejada; linhas que vão sumir pulsam em ouro.
- Prévia inválida: casas livres em vermelho, blocos no caminho com contorno vermelho; a peça arrastada perde a cor.
- Tetris: a peça fantasma é contornada na cor da peça.

## Combos

×2 texto · ×3–4 texto maior + anel · ×5+ "Combo épico" + anel + confete + clarão + hitstop.

## História: recompensas

- 3 estrelas por fase: concluir · desafio especial da fase · não usar o poder. Estrelas só somam (repetir nunca duplica nem remove).
- Medalha por mundo (bronze/prata/ouro) ao concluir as 10 fases, conforme as estrelas.
- Texturas desbloqueadas por estrelas: Mármore (15) e Metal (40).

## História: mundos e materiais (v12)

| Mundo | Nome | Material dos blocos |
|---|---|---|
| 1 | Superfície | Madeira |
| 2 | Céu | Mármore |
| 3 | Lua | Cimento |
| 4 | Espaço Profundo | Metal |
| 5 | Cidade do Futuro | Gelo |
| 6 | Vulcão | Lava |
| 7 | Mar Aberto | Água |
| 8 | Abismo Oceânico | Água |
| 9 | Vale Assombrado | Cimento |
| 10 | Campo de Batalha | Tijolo |

Cada mundo alterna duas paletas entre as fases e gira a ordem das cores, então nenhuma fase repete as cores da anterior. O material da História é do mundo e não depende da textura escolhida no menu (que vale para Clássico e Sudoku).
