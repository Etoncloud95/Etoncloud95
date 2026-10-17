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

## Estilos visuais (v11)

Uma única configuração (`Ajustes → Estilo visual`, salva como `visualStyle`) escolhe como tudo é desenhado. As regras do jogo não mudam.

| | Realismo (padrão) | Animação |
|---|---|---|
| Blocos | Texturas naturais + chanfro iluminado (alto/esquerda), borda escura fina, brilho no canto | Cores chapadas com sombreamento em camadas, contorno marrom grosso, brilho estilizado e um motivo por material |
| Casas vazias | Furos rebaixados (sombra interna) | Casas creme arredondadas com borda suave |
| Encaixe | Squash sutil (0,93 × 1,06) | Squash elástico (0,7 × 1,3 → 1,14 × 0,86) |
| Eliminação | Bloco "racha" (clarão → encolhe); estilhaços com textura, raios de luz, brilho | Bloco "estoura" (cresce, gira, some); pedaços com contorno, estrelas, anel de impacto |
| Textos | Folha de ouro com contorno escuro | Contorno cartoon com sombra dura |

Os dois estilos usam o mesmo código de jogo, as mesmas partículas e o mesmo agendamento. Só mudam os parâmetros e os pintores de textura (`Skins`), a explosão (`Scene.explode`) e as animações CSS (`body.vs-real` / `body.vs-anim`).

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
